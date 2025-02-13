package com.matin.taxi.webSocket;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
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


	public String  isLognned(ArrayList arg, String sessionId)
	{
		String user=(String)arg.get(0);
		String token=(String)arg.get(1);
	       
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		
		try {
			Person ret = personDAO.getLognned(user,token);
			
			if(ret==null) 
			{
				return "";
			}
			else 
			{
				if(!sessionId.equals(token))
				{
					ret.setToken(sessionId);
					personDAO.updatePerson(ret);
					
				}
				return sessionId;
			  }
		}
		catch (Exception e)
		{     
			return "";
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
		Orders orders=personDAO.getOrdersByClientIdState(clientId, 0);
		
		if(orders == null )
			return "";
		
		return orders.getRoute();
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
		
			getOrdersFn(clientId);
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
		
		
		
		public boolean acceptOrderClient(ArrayList arg, String sessionId) {
			
			Integer id=(Integer)arg.get(0);
			
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
			Person person = personDAO.getPersonByToken(sessionId);
			
			Long personId = person.getId();
			
			Orders order=personDAO.getOrderById(id.longValue());

			order.setState(1);
			//order.setTaxiId(personId);
			
			boolean res=personDAO.updateOrders(order);
			
			
			signalingSocketHandlerRPC.acceptOrderClientCB( person,order);  //notify current and taxis
			
			return res;
		}
		
		
		public boolean acceptOrder(ArrayList arg, String sessionId) {
		
			Integer id=(Integer)arg.get(0);
			
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
			Person person = personDAO.getPersonByToken(sessionId);
			
			Long personId = person.getId();
			
			Orders order=personDAO.getOrderById(id.longValue());

			order.setState(2);
			order.setTaxiId(personId);
			
			boolean res=personDAO.updateOrders(order);
			
			
			signalingSocketHandlerRPC.acceptOrderCB( person,order);  //notify current and taxis
			return res;
		}
		
		
		public boolean finishOrder(ArrayList arg, String sessionId) {
			
			Integer id=(Integer)arg.get(0);
			
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
			Person person = personDAO.getPersonByToken(sessionId);
			
			Long personId = person.getId();
			
			Orders order=personDAO.getOrderById(id.longValue());

			order.setState(4);
			//order.setTaxiId(personId);
			
			boolean res=personDAO.updateOrders(order);
			
			
			signalingSocketHandlerRPC.acceptOrderCB( person,order);  //notify current and taxis
			return res;
		}
		
		public String  getOrders(String  where ) 
		{
			return "";
		}
		public String  getOrdersFn(Long clientId) {
			

			
			try {
				
				AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
				PersonDAO personDAO = context.getBean(PersonDAO.class);

				//personDAO.geJjdbcTemplate();
				
				Connection cn = personDAO.geJjdbcTemplate().getDataSource().getConnection();
			
			Array sqlArray = cn.createArrayOf("int4", new Integer[] { 75,85 });
			
	        

	        CallableStatement cstmt = cn.prepareCall("{? = call getOrders(?)}");
	        cstmt.registerOutParameter(1, Types.VARCHAR);
	        try {
	            cstmt.setArray(2, sqlArray);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        cstmt.execute();
	        String result = cstmt.getString(1);// cstmt.getInt(1);
	        return result;
	       // System.out.println("Result: "+result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        return null;
		
		}
		
		/*
		 CREATE OR replace FUNCTION getOrders(number integer[]) RETURNS text
    AS $$
    BEGIN
    
      
     
    RETURN (
    
    
      SELECT json_agg( json_build_object(
'id', orders.id
,'person.id',person.id,'person.name',person.name
,'taxi.id',person.id,'taxi.name',person.name
,'state', orders.state,'create',orders.createtime ,'acepted',orders.createtime 
,'route', orders.route  
)

)FROM orders
left join person on person.id =orders.clientid
--where  orders.id in(number)
where  orders.id  IN (SELECT unnest(number) )
    
    ); 
END;$$
    LANGUAGE plpgsql;


SELECT getOrders('{75,85}')
SELECT getOrders('{85,85}')



		 */
		
		
		public String  getTaxuOrdersByClientId(Long clientId) {
			//String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";
			String sql = "SELECT json_agg( json_build_object(\n"
					+ "'id', orders.id\n"
					+ ",'state', orders.state\n"
					+ ",'taxi.id',persontaxi.id,'taxi.name',persontaxi.name\n"
					+ ",'createperson.id',person.id,'person.name',person.name\n"
					+ ",'',orders.createtime ,'acepted',orders.createtime \n"
					+ ",'route', orders.route  \n"
					+ "))\n"
					+ "FROM orders\n"
					+ "left join person on person.id =orders.clientid\n"
					+ "left join person persontaxi on persontaxi.id =orders.taxiid \n"
					+ "\n"
					+ "\n"
					+ "where  orders.state not in(0,4,2) --NOT temporal ,finish\n"
					+ "		or  ( state=1)			--all Active	 \n"
					+ " or ( persontaxi.id=? and state=2) ";
				//	+ " order by state";		

			
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			
						
			String  cnt = personDAO.geJjdbcTemplate().queryForObject(sql, String.class, new Object[] { clientId });

			return cnt;

			// this.jdbcTemplate.queryForObject(sql, Integer.class, user, token);
			// this.jdbcTemplate.queryForObject( sql, Integer.class, new Object[] {
			// user,token });
		}
		
		public String loadTaxiOrders(ArrayList arg, String sessionId)
		{
			
		
			
			String user=(String)arg.get(0);
			String token=(String)arg.get(1);
			
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
			PersonDAO personDAO = context.getBean(PersonDAO.class);
			//clientid
			Long clientId = personDAO.getPersonIdByUserToken(user,token);
			String  orders=getTaxuOrdersByClientId(clientId);
		
			getOrdersFn(clientId);
			return orders;
		}
		
		
}