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

import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.matin.taxi.AppConfig;
import com.matin.taxi.db.model.HikariCPDataSource;
import com.matin.taxi.db.model.Orders;
import com.matin.taxi.db.model.Person;
import com.matin.taxi.db.model.PersonDAO;
import com.matin.taxi.db.model.PersonDAOImpl;
import com.matin.taxi.db.model.PersonMapper;

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

	
	public boolean reconnect(String user ,String token,String sessionId) {
		try {
			Person ret = personDAO.getLognned(user, token);

			if (ret == null) {
				return false;
			} else {
				if (!sessionId.equals(token)) {
					ret.setToken(sessionId);
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
				String role=p.getRole();
				
				boolean isTaxi=role.equals("taxi");
				
				  JSONObject jsonObject = new JSONObject();
				 
				  jsonObject.put("sessionId", sessionId);
				  jsonObject.put("isTaxi", isTaxi);
				  
				  
				  //return sessionId;
				  String ret=jsonObject.toJSONString();
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
		boolean res = personDAO.updateOrders(order);

		signalingSocketHandlerRPC.acceptOrderClientCB(person, order); // notify current and taxis

		return res;
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

		Long personId = person.getId();

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

		Long taxiId = person.getId();

		Orders order = personDAO.getOrderById(id.longValue());

		order.setState(Orders.STATE_TAXI_END);
		order.setEndTime(new Timestamp(System.currentTimeMillis()));
		// order.setTaxiId(personId);

		boolean res = personDAO.updateOrders(order);

		List<Person> sendTo = getPersonsClientOrdersId(order.getId());
		
		signalingSocketHandlerRPC.handleUpdateOrder(person, sendTo,order);
		//signalingSocketHandlerRPC.acceptOrderCB(person, order); // notify current and taxis
		return res;
	}

	public List<Person> getPersonsClientOrdersId(Long orderId ) {
		// String sql = "SELECT json_agg(orders) FROM orders where clientId = ? ";
		String sql = "select person.*  from person\n"
				+ "inner join  Orders on Orders.clientid=person.id \n"
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
				+ ",'taxi.id',persontaxi.id,'taxi.name',persontaxi.name\n"
				+ ",'createperson.id',person.id,'person.name',person.name\n"
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
		String sql = "select person.*  from person\n"
				+ "inner join  Orders on Orders.clientid=person.id \n"
				+ "where Orders.taxiid=? and Orders.state =2";
		// + " order by state";
		return personDAO.geJjdbcTemplate().query(sql, new Object[] { clientId }, new PersonMapper());

	}

	/**
	 * timestamp
    coords accuracy
            altitude
            altitudeAccuracy
            heading
            latitude
            longitude
            speed
	 * 
	 */
	public boolean updatePostion(ArrayList arg, String sessionId) {

		String user = (String) arg.get(0);
		String token = (String) arg.get(1);
		Object postion = (Object) arg.get(2);
		System.out.println(postion);
		
			
		Long clientId = personDAO.getPersonIdByUserToken(user, token);
		

		Person sendFrom = personDAO.getPersonByPrincipal(user);
		List<Person> sendTo = getActiveOrdersByTaxiId(clientId);
		
		
		signalingSocketHandlerRPC.handleUpdatePostion(sendFrom,sendTo,postion);
		
		// getOrdersFn(clientId);
		return true;
	}
	
	public void getRouteDistanceTime() {
	//	JSONObject obj = new JSONObject("hfhf");
	}
	
	private void connectRelay(String remoteAddress) throws IOException {
		
		//String urlg="http://127.0.0.1:5000/route/v1/driving/27.9216128,43.20788479999999;27.9216128,43.20788479999999?overview=full&alternatives=true&steps=true";
		//String urlg="http://127.0.0.1:5000/route/v1/driving/27.9216128,43.20788479999999;27.9216128,43.20788479999999?overview=full&alternatives=true";
		  String urlg="http://127.0.0.1:5000/route/v1/driving/27.88934046113281,43.23140274654088;27.9216128,43.2078848?overview=full&alternatives=true";
		
		// System.out.println("target Address : " + remoteAddress);
		  int BUFFER_SIZE = 4096;
		URL url = new URL(urlg);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = httpConn.getInputStream();
			
			
			//ServletOutputStream outputStream = response.getOutputStream();
			
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

}