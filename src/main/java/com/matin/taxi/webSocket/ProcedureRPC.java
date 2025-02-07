package com.matin.taxi.webSocket;

import java.util.ArrayList;

import java.util.UUID;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.socket.WebSocketSession;

import com.matin.taxi.AppConfig;
import com.matin.taxi.db.model.Orders;
import com.matin.taxi.db.model.Person;
import com.matin.taxi.db.model.PersonDAO;

public class ProcedureRPC {
	
		
	SignalingSocketHandlerRPC signalingSocketHandlerRPC=null;
	
	public ProcedureRPC(SignalingSocketHandlerRPC signalingSocketHandlerRPC) {
		this.signalingSocketHandlerRPC=signalingSocketHandlerRPC;
	}


	public boolean isLognned(ArrayList arg, String sessionId)
	{
		String user=(String)arg.get(0);
		String token=(String)arg.get(1);
	       
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		
		try {
			Person ret = personDAO.getLognned(user,token);
			
			if(ret==null) 
			{
				return false;
			}
			else 
			{
				if(!sessionId.equals(token))
				{
					ret.setToken(sessionId);
				}
				return true;
			  }
		}
		catch (Exception e)
		{     
			return false;
		}
			
	}
	
	
	public String login(ArrayList arg, String sessionId)
	{
		String user=(String)arg.get(0);
		String passw=(String)arg.get(1);
	       
	       
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
			try {
				Person p = personDAO.getPersonByPrincipal(user);
		       
		        if(p.getPassw().equals(passw))
				{
		        	 //UUID uuid = UUID.randomUUID();
				     //   String uuidAsString = uuid.toString();
				        //p.setToken(uuidAsString);
		        	 
		        	
		        	 p.setToken(sessionId);
				        personDAO.updatePersonToken(p); 
				        
				        return sessionId;				
				    
				}
				else
				{
					return null;
				}
					
				
			}catch (Exception e)
			{
				return null;
			}
		
	}
	
	public boolean logOut(ArrayList arg, String sessionId)
	{
		String user=(String)arg.get(0);
		String token=(String)arg.get(1);
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		
		return  personDAO.getPersonLogOutIdByUserToken(user,token);
	}
	
	public String loadOrder(ArrayList arg, String sessionId)
	{
		String user=(String)arg.get(0);
		String token=(String)arg.get(1);
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		//clientid
		Long clientId = personDAO.getPersonIdByUserToken(user,token);
		Orders o=personDAO.getOrdersByClientIdState(clientId, 0);
		
		return o.getRoute();
	}
	
	
	//private void handleCreateOrder(WebSocketSession session, SignalMessage signalMessage) throws Exception{
		public boolean createOrder(ArrayList arg,String sessionId) {
		
			String user=(String)arg.get(0);
			String token=(String)arg.get(1);
			String order=(String)arg.get(2);
				
				AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
				PersonDAO personDAO = context.getBean(PersonDAO.class);
				
			       				
				//clientid
				Long clientId = personDAO.getPersonIdByUserToken(user,token);

				Orders o= new Orders(  clientId,  (long) 0,  0,  order);
				if(personDAO.createOrders(o))
		    		 return true;
				else 
					return false;
		 }
		
		
		public String loadOrders(ArrayList arg, String sessionId)
		{
			String user=(String)arg.get(0);
			String token=(String)arg.get(1);
			
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			//clientid
			Long clientId = personDAO.getPersonIdByUserToken(user,token);
			String  orders=personDAO.getOrdersByClientId(clientId);
			
			return orders;
		}
		
		
		public String loadOrderById(ArrayList arg, String sessionId)
		{
			Integer id=(Integer)arg.get(0);
				
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
			
			Orders o=personDAO.getOrderById(id.longValue());
			
			return o.getRoute();
		//	return orders;
		}
		
		
		
		public boolean deleteOrderById(ArrayList arg, String sessionId) {
			
			Integer id=(Integer)arg.get(2);
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
			
			boolean res=personDAO.deleteOrderById(id.longValue());
			return res;
		}
		
		
		public boolean acceptOrder(ArrayList arg, String sessionId) {
		
			Integer id=(Integer)arg.get(0);
			
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
			Person p = personDAO.getPersonByToken(sessionId);
			
			Long personId = p.getId();
			
			Orders o=personDAO.getOrderById(id.longValue());

			o.setState(2);
			o.setTaxiId(personId);
			
			boolean res=personDAO.updateOrders(o);
			
			
			//signalingSocketHandlerRPC.
			
			return res;
		}
		
}