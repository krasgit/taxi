package com.matin.taxi.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.matin.taxi.owner.OwnerRepository;

import java.util.HashMap;
import java.util.Map;


import com.matin.taxi.db.*;
public class SignalingSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SignalingSocketHandler.class);

    private static final String TYPE_INIT = "init";
    
    private static final String TYPE_LOGOUT = "logout";

    /**
     * Cache of sessions by users.
     */
    private final Map<String, WebSocketSession> connectedUsers = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("[" + session.getId() + "] Connection established " + session.getId());

        // send the message to all other peers, that new men its being registered
        final SignalMessage newMenOnBoard = new SignalMessage();
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
        LOG.info("[" + session.getId() + "] Connection closed " + session.getId() + " with status: " + status.getReason());
        removeUserAndSendLogout(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.info("[" + session.getId() + "] Connection error " + session.getId() + " with status: " + exception.getLocalizedMessage());
        removeUserAndSendLogout(session.getId());
    }

    //private final SimulateRepository simulate=new SimulateRepository();
     void handleLogPosition(SignalMessage signalMessage, String sessionId) {
    	
    	 
    	  SimulateRepository rc = SimulateController.get();
    	  
    	  Simulate log =new Simulate();
    	  log.setDescription((String)signalMessage.getData());
    	  log.setSessionId(sessionId);
		rc.save(log );
    	 
    	 System.out.print(signalMessage);
    }
    
    
     
   
     
    void SendMessage(WebSocketSession destSocket,SignalMessage message ) throws Exception{
    if (destSocket != null && destSocket.isOpen()) {
        // set the sender as current sessionId.
    	message.setSender(destSocket.getId());
        final String resendingMessage = Utils.getString(message);
        LOG.info("send message {} to {}", resendingMessage, message);
        destSocket.sendMessage(new TextMessage(resendingMessage));
    }
    }
     
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.info("handleTextMessage : {}", message.getPayload());

        String payload = message.getPayload();
        
        
        SignalMessage signalMessage = Utils.getObject(payload);


        if(signalMessage.getType().equals("updatePostion"))
        {
        	handleUpdatePostion(session,signalMessage);
        	return ;
        }
        
        
        if(signalMessage.getType().equals("login"))
        {
        	handleLogin(session,signalMessage);
        	return ;
        }
        
        if(signalMessage.getType().equals("ping"))
        	signalMessage.setType("pong");
        
        if(signalMessage.getType().equals("logPosition"))
        {
        	handleLogPosition(signalMessage,session.getId());
        	return;
        }
        
        // with the destinationUser find the targeted socket, if any
        String destinationUser = signalMessage.getReceiver();
        WebSocketSession destSocket = connectedUsers.get(destinationUser);
        // if the socket exists and is open, we go on
        if (destSocket != null && destSocket.isOpen()) {
            // set the sender as current sessionId.
            signalMessage.setSender(session.getId());
            final String resendingMessage = Utils.getString(signalMessage);
            LOG.info("send message {} to {}", resendingMessage, destinationUser);
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
	
	 void handleLogin(WebSocketSession session, SignalMessage signalMessage) throws Exception
	    {
		 	System.err.println(signalMessage.getSender());
	       WebSocketSession destSocket = connectedUsers.get(signalMessage.getSender());
	       
	       SignalMessage responceMessage=new SignalMessage();
	       responceMessage.setType("login");
	       /*
	       HashMap<?, ?> map = signalMessage.getMap();
	       Object u = map.get("username");
	       String username = (String) u;
	       */
	       if(signalMessage.getMap().get("username").equals(""))
	         responceMessage.setData(SignalMessage.logned);
	       else 
	    	 responceMessage.setData(SignalMessage.notlogned);
	       //
	       
	       //
	       SendMessage(destSocket,responceMessage);
	    }
	 
	 private void handleUpdatePostion(WebSocketSession session, SignalMessage signalMessage) throws Exception{
		 System.err.println(signalMessage.getSender());
   	  //WebSocketSession destSocket = connectedUsers.get(signalMessage.getReceiver());
         
         SignalMessage responceMessage=new SignalMessage();
         responceMessage.setType("UpdatePostion");
         /*
         HashMap<?, ?> map = signalMessage.getMap();
         Object u = map.get("username");
         String username = (String) u;
         */
         
      	 responceMessage.setData(signalMessage.getData());
         //
         
      	for (var entry : connectedUsers.entrySet()) {
      	    //System.out.println(entry.getKey() + "/" + entry.getValue());
      		if(!entry.getKey().equals(signalMessage.getSender()))
      		{
      			WebSocketSession destSocket=entry.getValue();
      	        SendMessage(destSocket,responceMessage);
      		}
      	}
 	}
}
