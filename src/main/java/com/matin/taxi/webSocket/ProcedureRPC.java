package com.matin.taxi.webSocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.socket.WebSocketSession;

import com.matin.taxi.AppConfig;
import com.matin.taxi.db.model.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProcedureRPC extends ProcedureCore {

	public ProcedureRPC(SignalingSocketHandlerRPC signalingSocketHandlerRPC) {

		DataSource ds = HikariCPDataSource.getDataSource();
		this.personDAO = new PersonDAOImpl(ds);
		this.signalingSocketHandlerRPC = signalingSocketHandlerRPC;
		this.context = new AnnotationConfigApplicationContext(AppConfig.class);

		init();

	}

	/**
	 * 
	 * @param arg
	 * @param sessionId
	 * @return
	 */
	public String isLognned(ArrayList arg, String sessionId) {
		try {
			Person ret = getPersonUI(arg);

			if (ret == null) {
				return "";
			} else {
				String token = (String) arg.get(1);
				if (!sessionId.equals(token)) {
					ret.setToken(sessionId);
					personDAO.updatePerson(ret);

				}
				return sessionId;
			}
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 
	 * @param arg
	 * @param sessionId
	 * @return type of logned person
	 */
	public String login(ArrayList arg, String sessionId) {

		String user = (String) arg.get(0);
		String passw = (String) arg.get(1);
		try {
			Person p = personDAO.getPersonByPrincipal(user);

			if (p.getPassw().equals(passw)) {
				p.setToken(sessionId);
				personDAO.updatePersonToken(p);
				String role = p.getRole();

				boolean isTaxi = role.equals("taxi");

				JSONObject jsonObject = new JSONObject();

				jsonObject.put("sessionId", sessionId);
				jsonObject.put("isTaxi", isTaxi);
				jsonObject.put("personId", p.getId());

				String ret = jsonObject.toJSONString();
				//
				return ret;

			} else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}

	}

	public boolean logOut(ArrayList arg, String sessionId) {
		String user = (String) arg.get(0);
		String token = (String) arg.get(1);
		return personDAO.getPersonLogOutIdByUserToken(user, token);
	}

	public String loadOrder(ArrayList arg, String sessionId) {

		Person person = getPersonUI(arg);

		Orders orders = personDAO.getOrdersByClientIdState(person.getId(), 0);

		if (orders == null)
			return "";

		return orders.getRoute();
	}

	public boolean createOrder(ArrayList arg, String sessionId) {

		Person person = getPersonUI(arg);
		String order = (String) arg.get(2);

		Orders o = new Orders(person.getId(), (long) 0, Orders.STATE_CREATED, order);
		o.setCreateTime(new Timestamp(System.currentTimeMillis()));

		if (personDAO.createOrders(o))
			return true;
		else
			return false;
	}

	
	
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

	
	public void tryNewProffer(Orders order) throws Exception {
		ArrayList<Person> allActiveTaxi = getAllActiveTaxi();
		
		ListIterator<Person> it = allActiveTaxi.listIterator();
		//remove alegre try  inv
		while (it.hasNext()) {
			
			Person p = it.next();
			
			Proffer proffer = personDAO.getProfferByOrderIdPersonId(order.getId(),p.getId());
			if(proffer!=null)
				it.remove();
		}
		
		if (allActiveTaxi.isEmpty())
			throw new Exception("No Taxi in range");
		
		
		
	}
	
	public boolean acceptOrderClient(ArrayList arg, String sessionId) {

		Person person = null;
		try {
			Integer id = (Integer) arg.get(0);

			person = personDAO.getPersonByToken(sessionId);

			Orders order = personDAO.getOrderById(id.longValue());

			String orderStartPosition = getOrderStartPosition(order.getRoute());

			ArrayList<Person> allActiveTaxi = getAllActiveTaxi();

			if (allActiveTaxi.isEmpty())
				throw new Exception("No Taxi in range");

			
			calkOrderToTaxiDistanceAndSort(orderStartPosition, allActiveTaxi);
			// ---------------

		
			ListIterator<Person> it = allActiveTaxi.listIterator();

			//
			order.setState(Orders.STATE_CLIENT_START);
			order.setClientStartTime(new Timestamp(System.currentTimeMillis()));
			// TODO
			order.setId(null);

			boolean res = personDAO.createOrders(order);

			while (it.hasNext()) {
				int niq = it.nextIndex();
				Person p = it.next();
				Proffer proffer = new Proffer();
				proffer.setOrderId(order.getId());
				proffer.setState(0);
				proffer.setPersonId(p.getId());
				proffer.setMessage("distance " + p.getAge());
				personDAO.createProffer(proffer);

				WebSocketSession webSocket = signalingSocketHandlerRPC.getConnectedUsers().get(p.getToken());
				if (webSocket == null) {
					LOG.error("Missing Connection to Person " + p.toString());

				} else {

					String routeName = personDAO.getRouteName(order.getId());

					String ret = new ArgsFeaturesJson().addFeatures("profferId", proffer.getId())
							.addFeatures("orderId", order.getId()).addFeatures("orderState", order.getState())
							.addFeatures("clientId", person.getId()).addFeatures("clientName", person.getName())

							.addFeatures("taxiId", p.getId()).addFeatures("taxi", p.getName())
							.addFeatures("routeName", routeName)

							.get();

					sendToPersonUI(p, "RouteControl.AddOffer('" + ret + "');");
					// ResultMessage resultMessage = new ResultMessage(null,
					// "RouteControl.AddOffer('"+ret+"');",null);
					// webSocket.sendMessage(new TextMessage(resultMessage.toObjectMapperString()));
					proffer.setState(1);
					personDAO.updateProffer(proffer);

				}

			}

			// signalingSocketHandlerRPC.acceptOrderClientCB(person, order); // notify
			// current and taxis

			sendToPersonUI(person, "RouteControl.loadOrders();");

		} catch (Exception e) {
			sendToPersonUI(person, "alert('" + e.getMessage() + "');");
			LOG.error(e.getMessage());
			LOG.error(e.getMessage());
			return false;

		}
		return true;
	}

	public boolean acceptOrder(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(0);

		Person personTaxi = personDAO.getPersonByToken(sessionId);

		Long personId = personTaxi.getId();

		Orders order = personDAO.getOrderById(id.longValue());

		order.setState(Orders.STATE_TAXI_ACCEPTED);
		order.setTaxiId(personId);
		order.setAcceptedTime(new Timestamp(System.currentTimeMillis()));

		boolean res = personDAO.updateOrders(order);

		List<Proffer> proffers = personDAO.getProfferByOrderId(order.getId());

		for (Proffer proffer : proffers) {
			int profferId = proffer.getId();
			if (proffer.getPersonId() == personId) // update
			{
				proffer.setState(Proffer.APPROVED);
				personDAO.updateProffer(proffer);

				String orderId = new ArgsFeaturesJson().addFeatures("orderId", order.getId()).get();

				String routeName = personDAO.getRouteName(order.getId());

				Person personClient = personDAO.getPersonById(order.getClientId());

				String ret = new ArgsFeaturesJson().addFeatures("profferId", proffer.getId())
						.addFeatures("orderId", order.getId()).addFeatures("orderState", order.getState())
						.addFeatures("clientId", personClient.getId()).addFeatures("clientName", personClient.getName())

						.addFeatures("taxiId", personTaxi.getId()).addFeatures("taxi", personTaxi.getName())
						.addFeatures("routeName", routeName)
						.get();

				sendToPersonUI(personTaxi, "RouteControl.AddOffer('" + ret + "');");

				sendToPersonUI(personClient, "RouteControl.loadOrders();");

				System.err.print("RouteControl.AddOffer");
				// TODO
				// sendToPersonUI(person,"");

			} else {
				Person _person = personDAO.getPersonById(proffer.getPersonId());

				proffer.setState(Proffer.CLOSED);
				personDAO.updateProffer(proffer);

				String orderId = new ArgsFeaturesJson().addFeatures("orderId", order.getId()).get();
				//sendToPersonUI(_person, "RouteControl.removeOffer('" + orderId + "');");
				sendToPersonUI(_person, "RouteControl.removeOffer('" + order.getId() + "');");

			}

		}
		return res;
	}

	
	
	public boolean startOrder(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(0);

		Person person = personDAO.getPersonByToken(sessionId);

		// Long personId = person.getId();

		Orders order = personDAO.getOrderById(id.longValue());

		order.setState(Orders.STATE_TAXI_START);
		order.setTaxiStartTime(new Timestamp(System.currentTimeMillis()));
		// order.setTaxiId(personId);

		boolean res = personDAO.updateOrders(order);

		signalingSocketHandlerRPC.acceptOrderCB(person, order); // notify current and taxis
		return res;
	}

	public boolean finishOrder(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(0);

		Person person = personDAO.getPersonByToken(sessionId);

		if (person == null)
			return false;
		// Long taxiId = person.getId();

		Orders order = personDAO.getOrderById(id.longValue());

		order.setState(Orders.STATE_TAXI_END);
		order.setEndTime(new Timestamp(System.currentTimeMillis()));
		// order.setTaxiId(personId);

		boolean res = personDAO.updateOrders(order);

		Person personClient = personDAO.getPersonById(order.getClientId());
		sendToPersonUI(personClient, "RouteControl.loadOrders();");
		sendToPersonUI(person, "RouteControl.removeOffer('" + order.getId() + "');");

		// signalingSocketHandlerRPC.handleUpdateOrder(person, sendTo, order);
		// signalingSocketHandlerRPC.acceptOrderCB(person, order); // notify current and
		// taxis
		return res;
	}

	public List<Person> getPersonsClientOrdersId(Long orderId) {
		// String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";
		String sql = "select person.*  from taxi.person\n" + "inner join  taxi.Orders on Orders.clientid=person.id \n"
				+ "where Orders.id=?";
		// + " order by state";
		return personDAO.geJjdbcTemplate().query(sql, new Object[] { orderId }, new PersonMapper());

	}

	public String loadOrders(ArrayList arg, String sessionId) {
		Person person = getPersonUI(arg);

		String orders = personDAO.getOrdersByClientId(person.getId());
		return orders;
	}

	public String loadOrderById(ArrayList arg, String sessionId) {
		Integer id = (Integer) arg.get(0);

		Orders o = personDAO.getOrderById(id.longValue());

		return o.getRoute();
		// return orders;
	}

	public boolean setOrderDeleteStateById(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(2);

		Orders o = personDAO.getOrderById(id.longValue());

		o.setState(Orders.STATE_DELETED);
		List<Proffer> proffers = personDAO.getProfferByOrderId(o.getId());
		for (Proffer proffer : proffers) {

			proffer.setState(proffer.CLOSED);
			personDAO.updateProffer(proffer);

			Person personClient = personDAO.getPersonById(Long.valueOf(proffer.getPersonId()));

			String arga = new ArgFeatures("'").addFeatures(o.getId()).get();

			sendToPersonUI(personClient, "RouteControl.removeOffer(" + arga + ");");

			// sendToPersonUI(personClient,"RouteControl.removeOffer(teControl.removeO);");
		}

		boolean res = personDAO.updateOrders(o);

		// boolean res = personDAO.deleteOrderById(id.longValue());
		return res;
	}

	public boolean deleteOrderById(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(2);

		boolean res = personDAO.deleteOrderById(id.longValue());
		return res;
	}

	public String getOrders(String where) {
		return "";
	}

	public String getOrdersFn(Long clientId) {

		try {

			Connection cn = personDAO.geJjdbcTemplate().getDataSource().getConnection();

			Array sqlArray = cn.createArrayOf("int4", new Integer[] { 75, 85 });

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
	 * CREATE OR replace FUNCTION getOrders(number integer[]) RETURNS text AS $$
	 * BEGIN
	 * 
	 * 
	 * 
	 * RETURN (
	 * 
	 * 
	 * SELECT json_agg( json_build_object( 'id', orders.id
	 * ,'person.id',person.id,'person.name',person.name
	 * ,'taxi.id',person.id,'taxi.name',person.name ,'state',
	 * orders.state,'create',orders.createtime ,'acepted',orders.createtime
	 * ,'route', orders.route )
	 * 
	 * )FROM orders left join person on person.id =orders.clientid --where orders.id
	 * in(number) where orders.id IN (SELECT unnest(number) )
	 * 
	 * ); END;$$ LANGUAGE plpgsql;
	 * 
	 * 
	 * SELECT getOrders('{75,85}') SELECT getOrders('{85,85}')
	 * 
	 * 
	 * 
	 */

	public String getTaxuOrdersByClientId(Long clientId) {
		// String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";
		String sql = "SELECT json_agg( json_build_object(\n" + "'id', orders.id\n" + ",'state', orders.state\n"
				+ ",'taxiId',persontaxi.id,'taxiName',persontaxi.name\n"
				+ ",'createpersonId',person.id,'personName',person.name\n"
				+ ",'',orders.createtime ,'acepted',orders.createtime \n" + ",'route', orders.route  \n" + "))\n"
				+ "FROM taxi.orders\n" + "left join taxi.person on person.id =orders.clientid\n"
				+ "left join taxi.person persontaxi on persontaxi.id =orders.taxiid \n" + "\n" + "\n"
				+ "where  orders.state not in(0,4,2) --NOT temporal ,finish\n"
				+ "		or  ( state=1)			--all Active	 \n" + " or ( persontaxi.id=0 and state=2) " // not
																												// asigned
				+ " or ( persontaxi.id=? and state=2) ";
		// + " order by state";

		String cnt = personDAO.geJjdbcTemplate().queryForObject(sql, String.class, new Object[] { clientId });

		return cnt;

		// this.jdbcTemplate.queryForObject(sql, Integer.class, user, token);
		// this.jdbcTemplate.queryForObject( sql, Integer.class, new Object[] {
		// user,token });
	}

	public String getTaxuOrdersById(Long Id) {
		// String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";

		String sql = "SELECT json_agg( json_build_object(\n" + "'id', orders.id\n" + ",'state', orders.state\n"
				+ ",'taxiId',persontaxi.id,'taxiName',persontaxi.name\n"
				+ ",'createpersonId',person.id,'personName',person.name\n"
				+ ",'',orders.createtime ,'acepted',orders.createtime \n" + ",'route', orders.route  \n" + "))\n"
				+ "FROM orders\n" + "left join person on person.id =orders.clientid\n"
				+ "left join person persontaxi on persontaxi.id =orders.taxiid \n" + "\n" + "\n"
				+ "where  orders.state not in(0,4,2) --NOT temporal ,finish\n"
				+ "		or  ( state=1)			--all Active	 \n" + " or ( persontaxi.id=0 and state=2) " // not
																												// asigned
				+ " or ( persontaxi.id=? and state=2) ";
		// + " order by state";

		String cnt = personDAO.geJjdbcTemplate().queryForObject(sql, String.class, new Object[] { Id });

		return cnt;
	}

	public List<Person> getActiveOrdersByTaxiId(Long clientId) {
		// String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";
		String sql = "select person.*  from taxi.person\n" + "inner join  taxi.Orders on Orders.clientid=person.id \n"
				+ "where Orders.taxiid=? and Orders.state =2";
		// + " order by state";
		return personDAO.geJjdbcTemplate().query(sql, new Object[] { clientId }, new PersonMapper());
	}

	/**
	 * timestamp coords accuracy altitude altitudeAccuracy heading latitude
	 * longitude speed
	 * 
	 */
	public boolean updatePostion(ArrayList arg, String sessionId) {

		String user = (String) arg.get(0);
		String token = (String) arg.get(1);
		String postion = (String) arg.get(2);
		System.out.println(postion);

		Person person = getPersonUI(arg);

		personDAO.createPosition(person.getId(), postion);

		Person sendFrom = personDAO.getPersonByPrincipal(user);
		List<Person> sendTo = getActiveOrdersByTaxiId(person.getId());

		if (sendTo.isEmpty()) {
			LOG.info("updatePostion send to is empty");
			return true;
		}
		signalingSocketHandlerRPC.handleUpdatePostion(sendFrom, sendTo, postion);

		// getOrdersFn(clientId);
		return true;
	}

	private void connectRelay(String remoteAddress) throws IOException {

		// String
		// urlg="http://127.0.0.1:5000/route/v1/driving/27.9216128,43.20788479999999;27.9216128,43.20788479999999?overview=full&alternatives=true&steps=true";
		// String
		// urlg="http://127.0.0.1:5000/route/v1/driving/27.9216128,43.20788479999999;27.9216128,43.20788479999999?overview=full&alternatives=true";
		String urlg = "http://127.0.0.1:5000/route/v1/driving/27.88934046113281,43.23140274654088;27.9216128,43.2078848?overview=full&alternatives=true";

		// System.out.println("target Address : " + remoteAddress);
		int BUFFER_SIZE = 4096;
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

			String str = baos.toString();

			// Print the string
			System.out.println(str);

		} else {
			System.out.println("Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();

	}


//json utils

	
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

	String getOrderStartPosition(String positions) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			// JsonNode yourObj =
			// mapper.readTree("{\"timestamp\":1744117809345,\"coords\":{\"accuracy\":16.348,\"latitude\":43.2206817,\"longitude\":27.8976221}}");
			JsonNode yourObj = mapper.readTree(positions);

			JsonNode coords = yourObj.path("coord");

			JsonNode coord = coords.get(0);

			String latitude = coord.path("lat").asText();
			String longitude = coord.path("lon").asText();

			System.out.println(latitude + "" + longitude);

			return longitude + "," + latitude;

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return null;
	}
	
	public String SendMessage(ArrayList arg, String sessionId) {
		String orderId = (String) arg.get(0);
		String from = (String) arg.get(1);
		String to = (String) arg.get(2);
		String message = (String) arg.get(3);

		if (message.isEmpty())
			return "Empty message";
		Orders o = personDAO.getOrderById(Long.parseLong(orderId));
		if (o == null)
			return "Empty context";

		Person personFrom = personDAO.getPersonById(Long.parseLong(from));

		if (personFrom == null)
			return "Empty personFrom";

		Person personTo = personDAO.getPersonById(Long.parseLong(to));

		if (personTo == null)
			return "Empty personTo";

		Messages messageDB = new Messages(1, Long.parseLong(orderId), Long.parseLong(from), Long.parseLong(to),
				message);
		Integer msgId = personDAO.createMessage(messageDB);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgId", msgId);
		jsonObject.put("fromId", from);
		jsonObject.put("from", personFrom.getName());

		jsonObject.put("context", orderId);
		jsonObject.put("msg", message);

		String ret = jsonObject.toJSONString();

		boolean res = signalingSocketHandlerRPC.command(personTo.getToken(), "MessageControl.OnMessage('" + ret + "')");

		System.out.println("getAllConnectedUser sessionId:" + sessionId + "isOK: " + res);

		return "OK";
	}

	
	public void loadTaxiOrders(ArrayList arg, String sessionId) {

		// Person person=getPersonUI( arg);

		Person personTaxi = personDAO.getPersonByToken(sessionId);

		Long personId = personTaxi.getId();

		List<Proffer> proffers = personDAO.getProfferByPersonId(personId);

		for (Proffer proffer : proffers) {
			Orders order = personDAO.getOrderById(proffer.getOrderId());

			int os = order.getState();

			if (os == Orders.STATE_DELETED)
				continue;
			if (os == Orders.STATE_CREATED)
				continue;
			if (os == Orders.STATE_TAXI_END)
				continue;
			
			if (os == Orders.STATE_EXPIRED)
				continue;
			
			String orderId = new ArgsFeaturesJson().addFeatures("orderId", order.getId()).get();

			String routeName = personDAO.getRouteName(order.getId());

			Person personClient = personDAO.getPersonById(order.getClientId());

			String ret = new ArgsFeaturesJson().addFeatures("profferId", proffer.getId())
					.addFeatures("orderId", order.getId()).addFeatures("orderState", order.getState())
					.addFeatures("clientId", personClient.getId()).addFeatures("clientName", personClient.getName())

					.addFeatures("taxiId", personTaxi.getId()).addFeatures("taxi", personTaxi.getName())
					.addFeatures("routeName", routeName)

					// clientId", orders.getClientId());
					// parameters.put("taxiId", orders.getTaxiId());
					.get();

			// ResultMessage resultMessage = new ResultMessage(null,
			// "RouteControl.AddOffer('"+ret+"');",null);
			sendToPersonUI(personTaxi, "RouteControl.AddOffer('" + ret + "');");

			sendToPersonUI(personClient, "RouteControl.loadOrders();");

			System.err.print("RouteControl.AddOffer");
		}

	}

}
