package com.matin.taxi.webSocket;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
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
import com.matin.taxi.db.model.Position;
import com.matin.taxi.db.model.Proffer;

public class ProcedureCore {

	// m
	protected static final int MAXDISTANCE = 10000;
	// s
	protected static final int PROFFER_TIMEOUTO = 15;

	/**	 * count of PROFFER 1 one n	 */
	protected static final int PROFFER_COUNT = 1;
	
	protected static final Logger LOG = LoggerFactory.getLogger(ProcedureRPC.class);

	protected SignalingSocketHandlerRPC signalingSocketHandlerRPC = null;
	AnnotationConfigApplicationContext context = null;
	PersonDAOImpl personDAO = null;
	private TaxiManager tm = null;
	

	void client(Entry<String, WebSocketSession> entry) {

		Person person = personDAO.getPersonByToken(entry.getKey());

		Orders orders = personDAO.getOrdersByClientIdState(person.getId(), Orders.STATE_CLIENT_START);
		if (orders == null) {
			LOG.info("ProcedureCore::client no STATE_CLIENT_START order");
			return;
		}

		List<Proffer> f = personDAO.getActiveProfferOrderId(orders.getId());
		Proffer proffer = f.get(0);

		// Orders orders = personDAO.getOrderById(proffer.getOrderId());
		int diff = proffer.getDiff();

		if (diff < PROFFER_TIMEOUTO) {

			String arg = new ArgFeatures("'")
					.addFeatures("info" + orders.getId(), "Wait:" + diff + " from Proffer" + f.size()).get();
			sendToPersonUI(person, "RouteControl.updateElementContent(" + arg + ");");
		} else {
//			orders.setState(Orders.STATE_EXPIRED);
//			personDAO.updateOrders(orders);
//			//
//			String arg=new ArgFeatures("'").addFeatures(orders.getId()).get();
//			   sendToPersonUI(entry.getValue(),"RouteControl.removeOffer("+arg+");");

		}
	}

	void taxi(Entry<String, WebSocketSession> entry) {
		{
			Person person = personDAO.getPersonByToken(entry.getKey());

			List<Proffer> proffers = personDAO.getActiveProfferByPersonIdEXPIRED(person.getId());

			for (Proffer proffer : proffers) {

				Orders orders = personDAO.getOrderById(proffer.getOrderId());

				if (orders.getState() == Orders.STATE_TAXI_ACCEPTED)
					continue;

				if (orders.getState() == Orders.STATE_DELETED)
					continue;

				// if(orders.getState()==Orders.STATE_EXPIRED)//see getActiveProfferByPersonId
				// {
				// String arg=new ArgFeatures("'").addFeatures(orders.getId()).get();
				// sendToPersonUI(person,"RouteControl.removeOffer("+arg+");");
				// continue;
				// }
				String target = "progress" + proffer.getOrderId();

				// sendToPersonUI(entry.getValue(),"RouteControl.updateOffer('"+target+"','+"+proffer.getDiff()+"+');");

				int diff = proffer.getDiff();
				// value="0" max="100"
				if (diff < PROFFER_TIMEOUTO) {
					String arg = new ArgFeatures("'").addFeatures(target, "value", proffer.getDiff()).get();
					sendToPersonUI(person, "RouteControl.updateElementAttr(" + arg + ");");
				} else {
					String arg = new ArgFeatures("'").addFeatures(orders.getId()).get();
					sendToPersonUI(person, "RouteControl.removeOffer(" + arg + ");");

					boolean ret=true;
					try {
					 ret = createNewOffer(orders);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(ret) {
					orders.setState(Orders.STATE_EXPIRED);
					personDAO.updateOrders(orders);
					}
					Person clientPerson = personDAO.getPersonById(orders.getClientId());
					sendToPersonUI(clientPerson, "RouteControl.clientLoadOrders();");
				}
			}
		}

	}
	protected boolean createNewOffer(Orders order) throws Exception{
		ArrayList<Person>  allActiveTaxi=getTaxiValidate(order.getId());

		ListIterator<Person> it=allActiveTaxi.listIterator();
		while (it.hasNext()) {
			int index = it.nextIndex();
			
			if(index>=PROFFER_COUNT)
				return true;
			
			Person person = it.next();
			Proffer proffer = new Proffer();
			proffer.setOrderId(order.getId());
			proffer.setState(0);
			proffer.setPersonId(person.getId());
			proffer.setMessage("distance " + person.getAge());
			personDAO.createProffer(proffer);

			WebSocketSession webSocket = signalingSocketHandlerRPC.getConnectedUsers().get(person.getToken());
			if (webSocket == null) {
				LOG.error("Missing Connection to Person " + person.toString());

			} else {

				//Auto assign
				if (!person.isManual())
				{
					order.setState(Orders.STATE_TAXI_ACCEPTED);
					order.setTaxiId(person.getId());
					order.setAcceptedTime(new Timestamp(System.currentTimeMillis()));

					boolean ress = personDAO.updateOrders(order);
					
					proffer.setState(Proffer.APPROVED);
					personDAO.updateProffer(proffer);

					String orderId = new ArgsFeaturesJson().addFeatures("orderId", order.getId()).get();

					String routeName = personDAO.getRouteName(order.getId());

					Person personClient = personDAO.getPersonById(order.getClientId());

					String ret = new ArgsFeaturesJson().addFeatures("profferId", proffer.getId())
							.addFeatures("orderId", order.getId()).addFeatures("orderState", order.getState())
							.addFeatures("clientId", personClient.getId()).addFeatures("clientName", personClient.getName())

							.addFeatures("taxiId", person.getId()).addFeatures("taxi", person.getName())
							.addFeatures("routeName", routeName)
							.get();

					sendToPersonUI(person, "RouteControl.AddOffer('" + ret + "');");

					sendToPersonUI(personClient, "RouteControl.loadOrders();");

					//System.err.print("RouteControl.AddOffer");

					return true;
					
				}
				
				
				String routeName = personDAO.getRouteName(order.getId());

				String ret = new ArgsFeaturesJson().addFeatures("profferId", proffer.getId())
						.addFeatures("orderId", order.getId()).addFeatures("orderState", order.getState())
						.addFeatures("clientId", person.getId()).addFeatures("clientName", person.getName())

						.addFeatures("taxiId", person.getId()).addFeatures("taxi", person.getName())
						.addFeatures("routeName", routeName)

						.get();

				sendToPersonUI(person, "RouteControl.AddOffer('" + ret + "');");
				// ResultMessage resultMessage = new ResultMessage(null,
				// "RouteControl.AddOffer('"+ret+"');",null);
				// webSocket.sendMessage(new TextMessage(resultMessage.toObjectMapperString()));
				proffer.setState(1);
				personDAO.updateProffer(proffer);

			}

		}
		return false;
		
		
		
		}
	private ArrayList<Person> getTaxiValidate(Long orderId) throws Exception
	{
		ArrayList<Person> allActiveTaxi = getAllActiveTaxi();
		
		filterTaxiForOffer(orderId,allActiveTaxi);

		if (allActiveTaxi.isEmpty())
			throw new Exception("No Taxi in range");

		
		ListIterator<Person> it = allActiveTaxi.listIterator();
		
		while (it.hasNext()) {
			Person p = it.next();

			Proffer proffer = personDAO.getProfferPersonIdOrderId(orderId,p.getId());
			
			if(proffer!=null)
			{
			LOG.info("ghgh");
			it.remove();
			}
		}
		if (allActiveTaxi.isEmpty())
			throw new Exception("No Taxi in range");
		
		return allActiveTaxi;
	}
	private void filterTaxiForOffer(Long orderId, ArrayList<Person>allActiveTaxi)
	{
		ListIterator<Person> it=allActiveTaxi.listIterator();
		while (it.hasNext()) {
			Person p = it.next();

			Proffer proffer = personDAO.getProfferPersonIdOrderIdState(orderId,p.getId(),Proffer.CREATE);
			
			if(proffer!=null)
			{
			it.remove();
			}
		}
	}

	
	
	
	protected ArrayList<Person> getTaxiValidate(Person person,Long orderId) throws Exception
	{
		
		List<Orders>  active=personDAO.getOrderByClientIdandState(person.getId(),1);
		
		if(active.size()>=3)
			throw new Exception("Too many request");
		
		ArrayList<Person> allActiveTaxi = getAllActiveTaxi();
		
		
		filterTaxiForOffer(orderId,allActiveTaxi);

		if (allActiveTaxi.isEmpty())
			throw new Exception("No Taxi in range");

		
		ListIterator<Person> it = allActiveTaxi.listIterator();
		
		while (it.hasNext()) {
			Person p = it.next();

			Proffer proffer = personDAO.getProfferPersonIdOrderId(orderId,p.getId());
			
			if(proffer!=null)
			{
			LOG.info("ghgh");
			it.remove();
			}
		}
		if (allActiveTaxi.isEmpty())
			throw new Exception("No Taxi in range");
		
		return allActiveTaxi;
	}
	


	protected void init() {

		tm = new TaxiManager(signalingSocketHandlerRPC, personDAO);

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		Runnable periodicTask = new Runnable() {
			public void run() {

				try {

					tm.process();// process old

					Map<String, WebSocketSession> connectedUsers = signalingSocketHandlerRPC.getConnectedUsers();
					LOG.info("ScheduledExecutorService RUN active connections " + connectedUsers.size());
					for (var entry : connectedUsers.entrySet()) {
						System.out.println(entry.getKey() + "/" + entry.getValue());

						Person p = personDAO.getPersonByToken(entry.getKey());
						if (p == null) {
							LOG.info("executor Person is null");
							continue;
						}
						boolean isTaxi = p.getRole().equals("taxi");

						if (isTaxi) {
							taxi(entry);
						} else {
							client(entry);
						}

					}
				} catch (Exception e) {
					LOG.info("ScheduledExecutorService " + e.getMessage());
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
	@Deprecated
	boolean sendToPersonUI(WebSocketSession webSocket, String msg) {

		if (webSocket != null) {
			try {
				ResultMessage resultMessage = new ResultMessage(msg);
				webSocket.sendMessage(new TextMessage(resultMessage.toObjectMapperString()));

				LOG.error("sendToPersonUI::OK");
				return true;
			} catch (Exception e) {
				LOG.error("sendToPersonUI::ERROR->" + e.getMessage());
				e.printStackTrace();
			}
		}
		LOG.error("sendToPersonUI::ERROR ");
		return false;
	}

	boolean sendToPersonUI(Person person, String msg) {

		WebSocketSession webSocketSession = signalingSocketHandlerRPC.getConnectedUsers().get(person.getToken());

		if (webSocketSession == null) {
			LOG.info("Not connection for person" + person.getName());
			tm.add(person.getId(), msg);
			return false;
		}

		boolean res = tm.send(webSocketSession, msg);

		if (!res)
			tm.add(person.getId(), msg);

		if (true == true)
			return true;

		LOG.info("sendToPersonUI::sendToPersonUI >to" + person.getName() + "  msg" + msg);

		WebSocketSession webSocket = signalingSocketHandlerRPC.getConnectedUsers().get(person.getToken());

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
		ArrayList<Person> personList = new ArrayList<Person>();

		doWork(new Callback() {
			@Override
			public void call(String id) {
				LOG.info("callback called arg" + id);
				Person person = personDAO.getPersonByToken(id);

				if (person != null)
					if (person.getRole().equals("taxi") && person.isActive())
						personList.add(person);
			}
		});
		return personList;
	}

	private ArrayList<Person> getAllPerson() {
		ArrayList<Person> personList = new ArrayList<Person>();

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

	// json utils

	String getLocation(String position) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			// JsonNode yourObj =
			// mapper.readTree("{\"timestamp\":1744117809345,\"coords\":{\"accuracy\":16.348,\"latitude\":43.2206817,\"longitude\":27.8976221}}");
			JsonNode yourObj = mapper.readTree(position);

			JsonNode coords = yourObj.path("coords");
			String latitude = coords.path("latitude").asText();
			String longitude = coords.path("longitude").asText();

			return longitude + "," + latitude;

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param orderStartPosition
	 * @param allActiveTaxi
	 * @throws Exception
	 */
	void calkOrderToTaxiDistanceAndSort(String orderStartPosition, ArrayList<Person> allActiveTaxi) throws Exception {
		if (allActiveTaxi.isEmpty())
			throw new Exception("No Acctive Taxi");

		ListIterator<Person> iter = allActiveTaxi.listIterator();
		// calk distance
		while (iter.hasNext()) {
			Person p = iter.next();
			Position plp = personDAO.getLastPosition(p.getId());
			if (plp == null) {
				iter.remove();
				continue;
			}

			String pos = getLocation(plp.getPosition());
			int distance = getDistance(pos, orderStartPosition);
			if (distance > MAXDISTANCE)
				iter.remove();
			else
				p.setAge(distance);
		}

		Collections.sort(allActiveTaxi, new Comparator<Person>() {
			@Override
			public int compare(Person person1, Person person2) {
				if (person1.getAge() < person2.getAge())
					return -1;
				if (person1.getAge() == person2.getAge())
					return 0;
				// if(this.age > p.getAge()) return 1;
				else
					return 1;
				// return u1.getAge().compareTo(u2.getAge());
			}
		});
	}
}
