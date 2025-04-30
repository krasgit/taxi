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
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.socket.WebSocketSession;

import com.matin.taxi.AppConfig;
import com.matin.taxi.db.model.HikariCPDataSource;
import com.matin.taxi.db.model.*;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// org.json
public class ProcedureRPC {

	SignalingSocketHandlerRPC signalingSocketHandlerRPC = null;
	AnnotationConfigApplicationContext context = null;
	PersonDAO personDAO = null;

	public ProcedureRPC(SignalingSocketHandlerRPC signalingSocketHandlerRPC) {

		DataSource ds = HikariCPDataSource.getDataSource();
		this.personDAO = new PersonDAOImpl(ds);

		this.signalingSocketHandlerRPC = signalingSocketHandlerRPC;
		this.context = new AnnotationConfigApplicationContext(AppConfig.class);
	}

	public boolean reconnect(String user, String token, String sessionId) {
		try {
			Person ret = personDAO.getLognned(user, token);

			if (ret == null) {
				return false;
			} else {
				if (!sessionId.equals(token)) {
					ret.setToken(sessionId);
					System.out.println("Update Token " + ret);
					personDAO.updatePerson(ret);

				}
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public String isLognned(ArrayList arg, String sessionId) {
		String user = (String) arg.get(0);
		String token = (String) arg.get(1);

		try {
			Person ret = personDAO.getLognned(user, token);

			if (ret == null) {
				return "";
			} else {
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

		try {
			connectRelay("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

				// return sessionId;
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
		String user = (String) arg.get(0);
		String token = (String) arg.get(1);

		Long clientId = personDAO.getPersonIdByUserToken(user, token);
		Orders orders = personDAO.getOrdersByClientIdState(clientId, 0);

		if (orders == null)
			return "";

		return orders.getRoute();
	}

	// private void handleCreateOrder(WebSocketSession session, SignalMessage
	// signalMessage) throws Exception{
	public boolean createOrder(ArrayList arg, String sessionId) {

		String user = (String) arg.get(0);
		String token = (String) arg.get(1);
		String order = (String) arg.get(2);

		// clientid
		Long clientId = personDAO.getPersonIdByUserToken(user, token);

		Orders o = new Orders(clientId, (long) 0, Orders.STATE_CREATED, order);
		o.setCreateTime(new Timestamp(System.currentTimeMillis()));

		if (personDAO.createOrders(o))
			return true;
		else
			return false;
	}

	public boolean acceptOrderClient(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(0);

		Person person = personDAO.getPersonByToken(sessionId);

		Long personId = person.getId();

		Orders order = personDAO.getOrderById(id.longValue());
		order.setState(Orders.STATE_CLIENT_START);
		order.setClientStartTime(new Timestamp(System.currentTimeMillis()));
		// TODO
		order.setId(null);

		boolean res = personDAO.createOrders(order);

		Position plp = personDAO.getLastPosition(personId);

		String pos = getLocation(plp.getPosition());

		String pos1 = getOrderStartPosition(order.getRoute());

		int distance = getDistance(pos, pos1);

		// signalingSocketHandlerRPC.command(sessionId, "alert('"+order.getId()+"')");

		signalingSocketHandlerRPC.acceptOrderClientCB(person, order); // notify current and taxis

		return res;
	}

	void getActivePersons(Map<String, WebSocketSession> connectedUsers) {
		connectedUsers.values().forEach(webSocketSession -> {
			try {
				// webSocketSession.getId();

				Person person = getPersonByToken(webSocketSession.getId());

				// pro.
				// person.

			} catch (Exception e) {

			}
		});

	}

	public boolean acceptOrder(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(0);

		Person person = personDAO.getPersonByToken(sessionId);

		Long personId = person.getId();

		Orders order = personDAO.getOrderById(id.longValue());

		order.setState(Orders.STATE_TAXI_ACCEPTED);
		order.setTaxiId(personId);
		order.setAcceptedTime(new Timestamp(System.currentTimeMillis()));

		boolean res = personDAO.updateOrders(order);

		// signalingSocketHandlerRPC.acceptOrderCB( person,order); //notify current and
		// taxis
		signalingSocketHandlerRPC.acceptOrderClientCB(person, order); // notify current and taxis
		return res;
	}

	public boolean startOrder(ArrayList arg, String sessionId) {

		Integer id = (Integer) arg.get(0);

		Person person = personDAO.getPersonByToken(sessionId);

		//Long personId = person.getId();

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

		if(person==null)
			return false;
		//Long taxiId = person.getId();

		Orders order = personDAO.getOrderById(id.longValue());

		order.setState(Orders.STATE_TAXI_END);
		order.setEndTime(new Timestamp(System.currentTimeMillis()));
		// order.setTaxiId(personId);

		boolean res = personDAO.updateOrders(order);

		List<Person> sendTo = getPersonsClientOrdersId(order.getId());

		signalingSocketHandlerRPC.handleUpdateOrder(person, sendTo, order);
		// signalingSocketHandlerRPC.acceptOrderCB(person, order); // notify current and
		// taxis
		return res;
	}

	public List<Person> getPersonsClientOrdersId(Long orderId) {
		// String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";
		String sql = "select person.*  from person\n" + "inner join  Orders on Orders.clientid=person.id \n"
				+ "where Orders.id=?";
		// + " order by state";
		return personDAO.geJjdbcTemplate().query(sql, new Object[] { orderId }, new PersonMapper());

	}

	public String loadOrders(ArrayList arg, String sessionId) {
		String user = (String) arg.get(0);
		String token = (String) arg.get(1);

		Long clientId = personDAO.getPersonIdByUserToken(user, token);
		String orders = personDAO.getOrdersByClientId(clientId);
		return orders;
	}

	public String loadOrderById(ArrayList arg, String sessionId) {
		Integer id = (Integer) arg.get(0);

		Orders o = personDAO.getOrderById(id.longValue());

		return o.getRoute();
		// return orders;
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
				+ "FROM orders\n" + "left join person on person.id =orders.clientid\n"
				+ "left join person persontaxi on persontaxi.id =orders.taxiid \n" + "\n" + "\n"
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

		// this.jdbcTemplate.queryForObject(sql, Integer.class, user, token);
		// this.jdbcTemplate.queryForObject( sql, Integer.class, new Object[] {
		// user,token });
	}

	public String loadTaxiOrders(ArrayList arg, String sessionId) {

		String user = (String) arg.get(0);
		String token = (String) arg.get(1);

		Long clientId = personDAO.getPersonIdByUserToken(user, token);
		String orders = getTaxuOrdersByClientId(clientId);

		// getOrdersFn(clientId);
		return orders;
	}

	public List<Person> getActiveOrdersByTaxiId(Long clientId) {
		// String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";
		String sql = "select person.*  from person\n" + "inner join  Orders on Orders.clientid=person.id \n"
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

		Long clientId = personDAO.getPersonIdByUserToken(user, token);

		personDAO.createPosition(clientId, postion);

		//Position pos = personDAO.getLastPosition(clientId);

		// String loc1=getLocation(pos.getPosition());

		// getDistance(loc1);

		Person sendFrom = personDAO.getPersonByPrincipal(user);
		List<Person> sendTo = getActiveOrdersByTaxiId(clientId);

		signalingSocketHandlerRPC.handleUpdatePostion(sendFrom, sendTo, postion);

		// getOrdersFn(clientId);
		return true;
	}

	public void getRouteDistanceTime() {
		// JSONObject obj = new JSONObject("hfhf");
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

	/**
	 * 
	 * @param start
	 * @param end
	 */
	int getDistance(String start, String end) {

		int distance = -1;

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
				System.out.println("Server replied HTTP code: " + responseCode);
				return -1;
			}
			httpConn.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distance;
	}

//json utils

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

	Person getPersonByToken(String token) {
		return personDAO.getPersonByToken(token);
	}

	// {"coord":[{"lon":27.846524698242188,"lat":43.24471167931782,"name":"кв.
	// Владиславово, Владислав
	// Варненчик","waypointName":"Start"},{"lon":27.94437168310547,"lat":43.25946508662122,"name":"Дупката-Куманово,
	// Куманово, Аксаково","waypointName":"End"}]}
	public List<Position> getAllOpenOrders(ArrayList arg, String sessionId) {
		ArrayList<Position> positions = new ArrayList<Position>();

		List<Orders> orders = personDAO.getAllOrdersByState(1);

		for (Orders order : orders) {

			Position p = new Position();

			ObjectMapper mapper = new ObjectMapper();
			// JsonNode yourObj =
			// mapper.readTree("{\"timestamp\":1744117809345,\"coords\":{\"accuracy\":16.348,\"latitude\":43.2206817,\"longitude\":27.8976221}}");
			JsonNode yourObj;
			try {
				yourObj = mapper.readTree(order.getRoute());
				JsonNode coords = yourObj.path("coord");

				JsonNode coord = coords.get(0);

				String latitude = coord.path("lat").asText();
				String longitude = coord.path("lon").asText();

				String gg = "{\"timestamp\":1744117809345,\"coords\":{\"accuracy\":16.348,\"latitude\":" + latitude
						+ ",\"longitude\":" + longitude + "}}";

				p.setPosition(gg);
				// todo
				p.setId(order.getId());

				p.setCreated_at(order.getCreateTime());

				positions.add(p);

			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// return longitude+","+latitude;
			System.out.println(order);
		}

		return positions;
	}

	// {"coord":[{"lon":27.846524698242188,"lat":43.24471167931782,"name":"кв.
	// Владиславово, Владислав
	// Варненчик","waypointName":"Start"},{"lon":27.94437168310547,"lat":43.25946508662122,"name":"Дупката-Куманово,
	// Куманово, Аксаково","waypointName":"End"}]}

	public List<Position> getAllConnectedUser(ArrayList arg, String sessionId) {
		ArrayList<Position> positions = new ArrayList<Position>();
		System.out.println("getAllConnectedUser sessionId:" + sessionId);

		signalingSocketHandlerRPC.getConnectedUsers().values().forEach(webSocketSession -> {
			try {

				String id = webSocketSession.getId();

				System.out.println("webSocketSession id" + id);

				if (sessionId != id) {
					Person p = getPersonByToken(id);

					if (p == null) {
						System.out.println("not found person by ssee:" + id);
					}

					if (p != null) {

						Position plp = personDAO.getLastPosition(p.getId());

						positions.add(plp);
						// String pos=getLocation(plp.getPosition());

						// ResultMessage resultMessage = new ResultMessage(null,
						// "LoginControlOLD.addPosition("+pos+")", null);

						// final String resendingMessage = Utils.getString(resultMessage);

						// webSocketSession.sendMessage(new TextMessage(resendingMessage));
					}
				}
			} catch (Exception e) {
				// LOG.warn("Error while message sending.", e);
			}
		});

		// signalingSocketHandlerRPC.getConnectedUsers();

		return positions;
	}

	public String SendMessage(ArrayList arg, String sessionId) {
		String context = (String) arg.get(0);
		String from = (String) arg.get(1);
		String to = (String) arg.get(2);
		String message = (String) arg.get(3);

		
		if(message.isEmpty())
			return "Empty message";
		Orders o=personDAO.getOrderById(Long.parseLong(context));
		if(o==null)
			return "Empty context";
		
		Person personFrom = personDAO.getPersonById(Long.parseLong(from));
		
		if(personFrom==null)
			return "Empty personFrom";
		
		Person personTo = personDAO.getPersonById(Long.parseLong(to));
		
		if(personTo==null)
			return "Empty personTo";
		
		Messages messageDB=new Messages(1,Long.parseLong(context),Long.parseLong(context),Long.parseLong(to),message);
		personDAO.createMessage(messageDB);
		
		
		boolean res=signalingSocketHandlerRPC.command(personTo.getToken(), "MessageControl.OnMessage('"+message+"')");
		
		System.out.println("getAllConnectedUser sessionId:" + sessionId + "isOK: "+res);

		return "OK";
	}

	
	public String createOffer(java.util.ArrayList arg ,String sessionId) 
	{
		String from = (String) arg.get(0);
		String to = (String) arg.get(1);
		String message = (String) arg.get(2);
		
		if(message.isEmpty())
			return "Empty message";
		
		Person personFrom = personDAO.getPersonById(Long.parseLong(from));
		
		if(personFrom==null)
			return "Empty personFrom";
		
		Person personTo = personDAO.getPersonById(Long.parseLong(to));
		
		if(personTo==null)
			return "Empty personTo";
		
		
		
		boolean res=signalingSocketHandlerRPC.command(personTo.getToken(), "MessageControl.Call('+from+','"+message+"')");
		
		
		
		return "OK";
		
	}
}