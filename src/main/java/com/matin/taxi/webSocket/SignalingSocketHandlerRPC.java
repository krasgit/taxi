package com.matin.taxi.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.matin.taxi.db.model.Orders;
import com.matin.taxi.db.model.Person;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SignalingSocketHandlerRPC extends TextWebSocketHandler {

	private static final Logger LOG = LoggerFactory.getLogger(SignalingSocketHandlerRPC.class);

	private static final String TYPE_INIT = "init";
	private static final String TYPE_LOGOUT = "logout";

	/**
	 * Cache of sessions by users.
	 */
	private final Map<String, WebSocketSession> connectedUsers = new HashMap<>();

	
	
	public boolean command(String sentdToClientByToken, String command ) {
		
		ResultMessage resultMessage = new ResultMessage(null, command,null);
		
			WebSocketSession webSocket = connectedUsers.get(sentdToClientByToken);
			//webSocket.getPrincipal().s
			try {
				
				if(webSocket!=null) {
					   webSocket.sendMessage(new TextMessage(Utils.getString(resultMessage)));
					   return true;
				}
			} catch (Exception e) {
				LOG.warn("Error while message sending.", e);
			
			}
		return false;
	}

	
	
  public SignalingSocketHandlerRPC() {
    	
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    Runnable periodicTask = new Runnable() {
    		    public void run() {
    		    	 connectedUsers.values().forEach(webSocketSession -> {
    		             try {
    		            	 final SignalMessage updateInfo = new SignalMessage();
    		            	 updateInfo.setType("UpdateInfo");
    		            	 updateInfo.setSender("app");
    		            	 
    		            	 //updateInfo.setData(getCurrentDateTime()+" online "+connectedUsers.size());
    		            	// LOG.info(updateInfo.toString())  ;  	 
    		                 webSocketSession.sendMessage(new TextMessage(Utils.getString(updateInfo)));
    		             } catch (Exception e) {
    		                 LOG.warn("Error while message sending.", e);
    		             }
    		         });
    		    	
    		    }
    		};
    		executor.scheduleAtFixedRate(periodicTask, 0, 10, TimeUnit.SECONDS);    
	}
	
	public Map<String, WebSocketSession> getConnectedUsers() {
	
		return connectedUsers;
	}
	
	
	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
	    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	    
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return query_pairs;
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOG.info("[" + session.getId() + "] Connection established " + session.getId());
		String query = session.getUri().getQuery();
		
		boolean update=false;
		if(query!=null) {
		Map<String, String> s = splitQuery(query);
		
		
		update=pro.reconnect(s.get("user"), s.get("token"), session.getId());
		
		
		}
		// send the message to all other peers, that new men its being registered
		final SignalMessage newMenOnBoard = new SignalMessage();
		
		if(update)
				newMenOnBoard.setType("sessionUpdate");
			else 
				newMenOnBoard.setType("session");
		
		newMenOnBoard.setSender(session.getId());

		
		session.sendMessage(new TextMessage(Utils.getString(newMenOnBoard)));

		newMenOnBoard.setType(TYPE_INIT);
		connectedUsers.values().forEach(webSocketSession -> {
			try {
				webSocketSession.sendMessage(new TextMessage(Utils.getString(newMenOnBoard)));
			} catch (Exception e) {
				LOG.warn("Error while message sending.", e);
			}
		});

		// put the session to the "cache".
		connectedUsers.put(session.getId(), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOG.info("[" + session.getId() + "] Connection closed " + session.getId() + " with status: "
				+ status.getReason());
		removeUserAndSendLogout(session.getId());
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		LOG.info("[" + session.getId() + "] Connection error " + session.getId() + " with status: "
				+ exception.getLocalizedMessage());
		removeUserAndSendLogout(session.getId());
	}

	void SendMessage(WebSocketSession destSocket, SignalMessage message) throws Exception {
		if (destSocket != null && destSocket.isOpen()) {
			// set the sender as current sessionId.
			message.setSender(destSocket.getId());
			final String resendingMessage = Utils.getString(message);
			LOG.info("send message {} to {}", resendingMessage, message);
			destSocket.sendMessage(new TextMessage(resendingMessage));
		}
	}

	private void removeUserAndSendLogout(final String sessionId) {

		connectedUsers.remove(sessionId);

		// send the message to all other peers, somebody(sessionId) leave.
		final SignalMessage menOut = new SignalMessage();
		menOut.setType(TYPE_LOGOUT);
		menOut.setSender(sessionId);

		connectedUsers.values().forEach(webSocket -> {
			try {
				webSocket.sendMessage(new TextMessage(Utils.getString(menOut)));
			} catch (Exception e) {
				LOG.warn("Error while message sending.", e);
			}
		});
	}

	public void listProcedureArgs(String name, ArrayList args) {
		System.out.print(name);
		System.out.print("(");
		for (Object arg : args) {
			System.out.print("" + arg.getClass().getSimpleName() + " " + arg);
			System.out.print(", ");
		}
		System.out.print(")");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		String sessionId = session.getId();
		
		String dd = session.getUri().getQuery();
		
		LOG.info("SignalingSocketHandlerRPC::handleTextMessage sessionID " + sessionId + " : {}", message.getPayload());

		String payload = message.getPayload();

		// {"id":"9cb8392625585","procedure":"multiplyByOneThousand","args":[3]}
		SignalMessageRPC request = Utils.getObjectRPC(payload);

		Object result = null;
		String error = null;
		try {
			// result= multiplyByOneThousand(request.getArgs());

			ArrayList args = (ArrayList) request.getArgs();

			listProcedureArgs(request.getProcedure(), args);

			result = invokePublicMethod(request.getProcedure(), args, sessionId);
			System.out.println("->return " + result);

		} catch (Exception e) {
			System.out.println("->return " + e.getMessage());
			error = e.getMessage();
		}

		ResultMessage resultmessage = new ResultMessage(request.getId(), result, error);

		final String resendingMessage = Utils.getString(resultmessage);

		session.sendMessage(new TextMessage(resendingMessage));

		// postAction(sessionId,request.getProcedure());

	}

	private void postAction(String sessionId, String procedure) {
		connectedUsers.values().forEach(webSocketSession -> {
			try {
				ResultMessage resultMessage = new ResultMessage(null, "TaxiControl.loadOrders()", null);

				final String resendingMessage = Utils.getString(resultMessage);

				webSocketSession.sendMessage(new TextMessage(resendingMessage));
			} catch (Exception e) {
				LOG.warn("Error while message sending.", e);
			}
		});

	}

	private final ProcedureRPC pro = new ProcedureRPC(this);

	public Object invokePublicMethod(String methodName, ArrayList args, String sessionId) throws Exception {

		Method sumInstanceMethod = ProcedureRPC.class.getMethod(methodName, args.getClass(), String.class);

		Object result = (Object) sumInstanceMethod.invoke(pro, args, sessionId);

		return result;
	}

	public void acceptOrderClientCB(Person p, Orders o) {

		String sessionId = p.getToken();

		// WebSocketSession webSocket = connectedUsers.get(sessionId);

		// if(webSocket==null)
		// {
		// LOG.error("Missing Connection " + sessionId);
		// return;
		// }

		// TODO Send To current user and free taxi

		ResultMessage resultMessage = new ResultMessage(null, "RouteControl.loadOrders();",	null);

		
		connectedUsers.values().forEach(webSocket -> {
			try {
				webSocket.sendMessage(new TextMessage(Utils.getString(resultMessage)));
			} catch (Exception e) {
				LOG.warn("Error while message sending.", e);
			}
		});
		/*
		 * try { resendingMessage = Utils.getString(resultMessage);
		 * webSocket.sendMessage(new TextMessage(resendingMessage)); } catch (Exception
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}

	public void acceptOrderCB(Person p, Orders o) {

		String sessionId = p.getToken();
		WebSocketSession webSocket = connectedUsers.get(sessionId);

		if (webSocket == null) {
			LOG.error("Missing Connection  " + sessionId);
			return;
		}

		ResultMessage resultMessage = new ResultMessage(null, "RouteControl.loadOrders();",null);

		String resendingMessage;
		try {
			resendingMessage = Utils.getString(resultMessage);
			webSocket.sendMessage(new TextMessage(resendingMessage));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param sendFrom
	 * @param sendTo
	 * @param postion
	 * @throws Exception 
	 */
	public void handleUpdatePostion(Person sendFrom, List<Person> sendTo, Object postion)  {

		ResultMessage resultMessage = new ResultMessage("handleUpdatePostion", postion, sendFrom.getName());
		TextMessage message = null;
		try {
			message = new TextMessage(Utils.getString(resultMessage));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//self
		
		try {
			WebSocketSession webSocket = connectedUsers.get(sendFrom.getToken());
			if (webSocket == null) {
				LOG.error("Missing Connection  " + sendFrom.getToken());
				return;
			}
			
			webSocket.sendMessage(message);
		} catch (Exception e) {
			LOG.warn("Error while message sending.", e);
		}
		
		
		for (Person p : sendTo) {
			WebSocketSession webSocket = connectedUsers.get(p.getToken());
			try {
				if(webSocket!=null)
				   webSocket.sendMessage(message);
				else 
					LOG.error("null webSocket");
			} catch (Exception e) {
				LOG.warn("Error while message sending.", e);
			}
		}

	}

	
	/**
	 * 
	 * @param sendFrom
	 * @param sendTo
	 * @param postion 
	 * acceptOrderCB
	 */
	public void handleUpdateOrder(Person sendFrom, List<Person> sendTo, Object postion) {

		
		sendTo.add(sendFrom) ;//self notify
		
		ResultMessage resultMessage = new ResultMessage(null, "RouteControl.loadOrders();",sendFrom.getName());
		
		for (Person p : sendTo) {
			WebSocketSession webSocket = connectedUsers.get(p.getToken());
			try {
				
				if(webSocket!=null)
					   webSocket.sendMessage(new TextMessage(Utils.getString(resultMessage)));
					else 
						LOG.error("null webSocket");
			} catch (Exception e) {
				LOG.warn("Error while message sending.", e);
			}
		}

	}

	

	
}
