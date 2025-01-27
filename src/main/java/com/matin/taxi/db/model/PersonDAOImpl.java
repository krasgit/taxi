package com.matin.taxi.db.model;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.matin.taxi.db.model.*;



@Component
public class PersonDAOImpl implements PersonDAO {

	JdbcTemplate jdbcTemplate;

	private final String SQL_FIND_PERSON = "select * from people where id = ?";
	private final String SQL_DELETE_PERSON = "delete from people where id = ?";
	private final String SQL_UPDATE_PERSON = "update people set firstname = ?, lastname = ?, age  = ? where id = ?";
	private final String SQL_GET_ALL = "select * from people";
	private final String SQL_INSERT_PERSON = "insert into people( firstname, lastname, age) values(?,?,?)";

	
	private final String SQL_FIND_PRINCIPAL="select * from people where firstName = ?";
	
	@Autowired
	public PersonDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Person getPersonById(Long id) {
		return jdbcTemplate.queryForObject(SQL_FIND_PERSON, new Object[] { id }, new PersonMapper());
	}

	public List<Person> getAllPersons() {
		return jdbcTemplate.query(SQL_GET_ALL, new PersonMapper());
	}

	public boolean deletePerson(Person person) {
		return jdbcTemplate.update(SQL_DELETE_PERSON, person.getId()) > 0;
	}

	public boolean updatePerson(Person person) {
		return jdbcTemplate.update(SQL_UPDATE_PERSON, person.getFirstName(), person.getLastName(), person.getAge(),
				person.getId()) > 0;
	}

	public boolean createPerson(Person person) {
		return jdbcTemplate.update(SQL_INSERT_PERSON,  person.getFirstName(), person.getLastName(),
				person.getAge()) > 0;
	}

	
	public Person getPersonByPrincipal(String principal) {
		return jdbcTemplate.queryForObject(SQL_FIND_PRINCIPAL, new Object[] { principal }, new PersonMapper());
	}

	//
	private final String SQL_UPDATE_PERSON_TOKEN = "update people set token  = ?  where id = ?";
	public boolean updatePersonToken(Person person) {
		return jdbcTemplate.update(SQL_UPDATE_PERSON_TOKEN, person.getToken(),person.getId()) > 0;
	}

	@Override
	public boolean isLognned(String user, String token) {
	String sql="SELECT count(*) FROM people WHERE firstname = ? AND token = ?";
		
		Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, user,token);
			
		
		return cnt != null && cnt > 0;
		
		
		//this.jdbcTemplate.queryForObject(sql, Integer.class,                     user, token);
        //this.jdbcTemplate.queryForObject( sql, Integer.class, new Object[] { user,token });
	}

	private final String SQL_INSERT_ORDERS = "insert into Orders( clientId, taxiId, state,route) values(?,?,?,?)";
	public boolean createOrders(Orders orders) {
		return jdbcTemplate.update(SQL_INSERT_ORDERS,  orders.getClientId(), orders.getTaxiId(), orders.getState(),orders.getRoute()) > 0;
	}

	@Override
	public Long getPersonIdByUserToken(String user, String token) {
		
		String sql="SELECT id FROM people WHERE firstname = ? AND token = ?";
			
		Long id = jdbcTemplate.queryForObject(sql, Long.class, user,token);
				
			
			return id;
			
			
			//this.jdbcTemplate.queryForObject(sql, Integer.class,                     user, token);
	        //this.jdbcTemplate.queryForObject( sql, Integer.class, new Object[] { user,token });
		}

	private final String SQL_FIND_ORDERS_CLIENTID_STATE="select * from Orders  where clientId = ? and state=? "
			+ " ORDER BY id DESC LIMIT 1 ";
	public Orders getOrdersByClientIdState(Long clientId, int state) {
		return jdbcTemplate.queryForObject(SQL_FIND_ORDERS_CLIENTID_STATE, new Object[] { clientId, state}, new OrdersMapper());
	}

	//clientId, taxiId, state,route
	private final String SQL_UPDATE_ORDERS = "update people set clientId = ?, taxiId = ?, state  = ? ,route=? where id = ?";
	public boolean updateOrders(Orders orders) {
		return jdbcTemplate.update(SQL_UPDATE_PERSON, orders.getClientId(), orders.getTaxiId(), orders.getState(),orders.getRoute(),
				orders.getId()) > 0;
		
	}

}