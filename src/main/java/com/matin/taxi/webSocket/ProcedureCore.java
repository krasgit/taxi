package com.matin.taxi.webSocket;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matin.taxi.db.model.Orders;
import com.matin.taxi.db.model.Person;
import com.matin.taxi.db.model.PersonDAOImpl;
import com.matin.taxi.db.model.Proffer;

public class ProcedureCore {
	
	
	// m
	protected static final int MAXDISTANCE = 10000;
	protected static final Logger LOG = LoggerFactory.getLogger(ProcedureRPC.class);

	protected SignalingSocketHandlerRPC signalingSocketHandlerRPC = null;
	AnnotationConfigApplicationContext context = null;
	PersonDAOImpl personDAO = null;
	
	
	
	
	void client(Entry<String, WebSocketSession> entry){
		
		Person person = personDAO.getPersonByToken(entry.getKey());
		
		Orders orders = personDAO.getOrdersByClientIdState(person.getId(),Orders.STATE_CLIENT_START);
		
		
		List<Proffer> f = personDAO.getActiveProfferOrderId(orders.getId());
		Proffer proffer=f.get(0);
	
		
		//Orders orders = personDAO.getOrderById(proffer.getOrderId());
		int diff = proffer.getDiff();
		
		if(diff<100)
		{
		
		String arg=new ArgFeatures("'").addFeatures("info"+orders.getId(),"Wait:"+diff+" from Proffer"+f.size()).get();
		   sendToPersonUI(entry.getValue(),"RouteControl.updateElementContent("+arg+");");
		}
		else
		{
		
			orders.setState(Orders.STATE_EXPIRED);
			personDAO.updateOrders(orders);
			
			//
			String arg=new ArgFeatures("'").addFeatures(orders.getId()).get();
			   sendToPersonUI(entry.getValue(),"RouteControl.removeOffer("+arg+");");
			
		}
	}
	
	void taxi(Entry<String, WebSocketSession> entry){
	{
		Person person = personDAO.getPersonByToken(entry.getKey());
		
		List<Proffer> proffers=personDAO.getActiveProfferByPersonId(person.getId()); 
		
		for(Proffer proffer:proffers)
		{
		
			Orders orders = personDAO.getOrderById(proffer.getOrderId());
			
	 		if(orders.getState()==Orders.STATE_TAXI_ACCEPTED)
	 			continue;
			
	 		if(orders.getState()==Orders.STATE_DELETED)
	 			continue;
			
	 		
	 		if(orders.getState()==Orders.STATE_EXPIRED)
	 		{
	 			String arg=new ArgFeatures("'").addFeatures(orders.getId()).get();
				   sendToPersonUI(entry.getValue(),"RouteControl.removeOffer("+arg+");");
	 			continue;
	 		}
		String target="progress"+proffer.getOrderId();
		
		//   sendToPersonUI(entry.getValue(),"RouteControl.updateOffer('"+target+"','+"+proffer.getDiff()+"+');");
		
		
		int diff = proffer.getDiff();
		//value="0" max="100"
		if(diff<100)
			{
			String arg=new ArgFeatures("'").addFeatures(target,"value",proffer.getDiff()).get();
		   sendToPersonUI(entry.getValue(),"RouteControl.updateElementAttr("+arg+");");
		}
		else 
		{
			String arg=new ArgFeatures("'").addFeatures(orders.getId()).get();
			   sendToPersonUI(entry.getValue(),"RouteControl.removeOffer("+arg+");");
			
			   sendToPersonUI(person, "RouteControl.loadOrders();");
		}
		}
	}	
	
	}
	protected void init() {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	    Runnable periodicTask = new Runnable() {
	    		    public void run() {
	    		
	    		    	Map<String, WebSocketSession> connectedUsers = signalingSocketHandlerRPC.getConnectedUsers();
	    		    	LOG.info("ScheduledExecutorService RUN active connections "+connectedUsers.size());
	    		    	for (var entry : connectedUsers.entrySet()) {
	    		    		System.out.println(entry.getKey() + "/" + entry.getValue());
	    		    		
	    		    		
	    		    		Person p = personDAO.getPersonByToken(entry.getKey());
                              if(p==null) {
                            	  LOG.info("executor Person is null");
                            	  continue;
                              }
	    		    		boolean isTaxi = p.getRole().equals("taxi");
	    		    		
	    		    		if(isTaxi)
	    		    		{
	    		    			taxi(entry);
	    		    		}
	    		    		else
	    		    		{
	    		    			client(entry);    			
	    		    		}

	    		    	}
	    		    }
	    		};
	    		executor.scheduleAtFixedRate(periodicTask, 0, 5, TimeUnit.SECONDS);    

	}
	
	/*
	 * call from afterConnectionEstablished
	 */
	public boolean reconnect(String user, String token, String sessionId) {
		try {
			Person ret = personDAO.getLognned(user, token);

			if (ret == null) {
				return false;
			} else {
				if (!sessionId.equals(token)) {
					ret.setToken(sessionId);
					LOG.info("Update Token " + ret);
					personDAO.updatePerson(ret);

				}
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
//--------------------------------------------------------------------------------------------------------
	/*
	 * Person person=getPersonUI( arg);
	 */
	Person getPersonUI(ArrayList arg) {
		String user = (String) arg.get(0);
		String token = (String) arg.get(1);
		return personDAO.getPersonByUserToken(user, token);
	}
//--------------------------------------------------------------------------------------------------------
	
	boolean sendToPersonUI(WebSocketSession webSocket,String msg )
	{
		 if (webSocket != null) {
			 try {
				 ResultMessage resultMessage = new ResultMessage(msg);	
				 webSocket.sendMessage(new TextMessage(resultMessage.toObjectMapperString()));
				 
				 LOG.error("sendToPersonUI::OK");
				 return true;
				} catch (Exception e) {
					LOG.error("sendToPersonUI::ERROR->"+e.getMessage());
					e.printStackTrace();
				}
		 }
		 LOG.error("sendToPersonUI::ERROR ");
		return false;
	}
	
	boolean sendToPersonUI(Person person,String msg )
	{
		LOG.info("sendToPersonUI::sendToPersonUI >to"+person.getName()+"  msg"+msg);	
		
		 WebSocketSession webSocket =signalingSocketHandlerRPC.getConnectedUsers().get(person.getToken());	

		 if (webSocket != null) {
			 try {
				 ResultMessage resultMessage = new ResultMessage(msg);	
				 webSocket.sendMessage(new TextMessage(resultMessage.toObjectMapperString()));
				 
				 LOG.info("sendToPersonUI::OK");
				 return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
		 LOG.error("sendToPersonUI::ERROR");
		return false;
	}

	// ------------------------------
		public interface Callback {
			void call(String id);
		}

		public void doWork(Callback callback) {

			Map<String, WebSocketSession> cu = signalingSocketHandlerRPC.getConnectedUsers();
			cu.values().forEach(webSocketSession -> {
				String id = webSocketSession.getId();
				callback.call(id);
			});
		}

		protected ArrayList<Person> getAllActiveTaxi() {
			ArrayList<Person> personList = new ArrayList<Person>(); // Create an ArrayList object

			doWork(new Callback() {
				@Override
				public void call(String id) {
					LOG.info("callback called arg" + id);
					Person person = personDAO.getPersonByToken(id);

					if (person != null)
						if (person.getRole().equals("taxi"))
							personList.add(person);
				}
			});
			return personList;
		}

		private ArrayList<Person> getAllPerson() {
			ArrayList<Person> personList = new ArrayList<Person>(); // Create an ArrayList object

			doWork(new Callback() {
				@Override
				public void call(String id) {
					Person person = personDAO.getPersonByToken(id);
					if (person != null)
						personList.add(person);
				}
			});
			return personList;
		}

		/**
		 * 
		 * @param start
		 * @param end
		 */
		int getDistance(String start, String end) {

			int distance = Integer.MAX_VALUE;

			String urlg = "http://127.0.0.1:5000/route/v1/driving/" + start + ";" + end + "?alternatives=false";

			int BUFFER_SIZE = 4096;

			try {
				URL url = new URL(urlg);

				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				int responseCode = httpConn.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream inputStream = httpConn.getInputStream();

					// ServletOutputStream outputStream = response.getOutputStream();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();

					int bytesRead = -1;
					byte[] buffer = new byte[BUFFER_SIZE];
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						baos.write(buffer, 0, bytesRead);
					}

					baos.close();
					inputStream.close();

					String json = baos.toString();

					distance = getRouteDistance(json);

				} else {
					LOG.info("Server replied HTTP code: " + responseCode);
					return distance;
				}
				httpConn.disconnect();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return distance;
		}
		
		int getRouteDistance(String route) {
			try {
				ObjectMapper mapper = new ObjectMapper();

				JsonNode routeObj = mapper.readTree(route);

				JsonNode coords = routeObj.path("routes");

				JsonNode coord = coords.get(0);

				// long duration = coord.path("duration").asLong();
				int distance = coord.path("distance").asInt();

				return distance;

			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}
			return -1;
		}

}
