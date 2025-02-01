package com.matin.taxi.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.matin.taxi.owner.OwnerRepository;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matin.taxi.AppConfig;
import com.matin.taxi.db.*;
import com.matin.taxi.db.model.Orders;
import com.matin.taxi.db.model.Person;
import com.matin.taxi.db.model.PersonDAO;
public class SignalingSocketHandlerRPC extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SignalingSocketHandlerRPC.class);

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

    
    
    
     
   
     
    void SendMessage(WebSocketSession destSocket,SignalMessage message ) throws Exception{
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
	
	
	
	 
	
	 
	 
		 
  
	public void listProcedureArgs(String name, ArrayList args)
	{
		System.out.print(name); 
		System.out.print("("); 
		for(Object arg:args)
		{
		 	System.out.print("" + arg.getClass().getSimpleName()+" " +arg);
		 	System.out.print(", ");
		}
		System.out.print(")");
	}
	
 @Override
 protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
     LOG.info("SignalingSocketHandlerRPC::handleTextMessage sessionID "+session.getId()+" : {}", message.getPayload());

     
     String payload = message.getPayload();
     
    // {"id":"9cb8392625585","procedure":"multiplyByOneThousand","args":[3]}
     SignalMessageRPC request = Utils.getObjectRPC(payload);

     Object result=null;
      String error=null;
     try {
       //  result= multiplyByOneThousand(request.getArgs());
    	 
    	 ArrayList args =(ArrayList) request.getArgs();
    	 
    	 listProcedureArgs(request.getProcedure(), args);
    	 
    	 result= invokePublicMethod(request.getProcedure(),args);
    	 System.out.println("->return "+ result); 
    	 
     } catch (Exception e) {
    	 System.out.println("->return "+ e.getMessage()); 
         error = e.getMessage();
       }
    
     ResultMessage resultMessage= new ResultMessage(request.getId(),result,error);
     
     final String resendingMessage =Utils.getString(resultMessage);
     
	session.sendMessage(new TextMessage(resendingMessage));
     
   //  String type = signalMessage.getType();
     
    /*
  
     let request = JSON.parse(msg);
     let procedure = this[request.procedure];
     if (!procedure) {
       console.log(`No such procedure: ${request.procedure}`);
       return;
     }
     let result, error;
     try {
       result = procedure(...request.args);
     } catch (e) {
       error = e;
     }

     this.socket.send(
       JSON.stringify({
         id: request.id,
         payload: result,
         error: error,
       })
     );
     */
      
     
     // with the destinationUser find the targeted socket, if any
    
 }
 
 public Object invokePublicMethod(String methodName,ArrayList args) throws Exception {
	    
	 Method sumInstanceMethod = ProcedureRPC.class.getMethod(methodName, ArrayList.class);

	    ProcedureRPC operationsInstance = new ProcedureRPC();
	    Object result = (Object) sumInstanceMethod.invoke(operationsInstance, args);

	    return result;
	}

private Object multiplyByOneThousand(Object number) {
	  return 3 * 1000;
	
}
 
}
