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

	private final String SQL_FIND_PERSON = "select * from person where id = ?";
	private final String SQL_DELETE_PERSON = "delete from person where id = ?";
	private final String SQL_UPDATE_PERSON = "update person set name = ?, passw = ?, age  = ? where id = ?";
	private final String SQL_GET_ALL = "select * from person";
	private final String SQL_INSERT_PERSON = "insert into person( name, passw, age) values(?,?,?)";

	
	private final String SQL_FIND_PRINCIPAL="select * from person where name = ?";
	
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
		return jdbcTemplate.update(SQL_UPDATE_PERSON, person.getName(), person.getPassw(), person.getAge(),
				person.getId()) > 0;
	}

	public boolean createPerson(Person person) {
		return jdbcTemplate.update(SQL_INSERT_PERSON,  person.getName(), person.getPassw(),
				person.getAge()) > 0;
	}

	
	public Person getPersonByPrincipal(String principal) {
		return jdbcTemplate.queryForObject(SQL_FIND_PRINCIPAL, new Object[] { principal }, new PersonMapper());
	}

	//
	private final String SQL_UPDATE_PERSON_TOKEN = "update person set token  = ?  where id = ?";
	public boolean updatePersonToken(Person person) {
		return jdbcTemplate.update(SQL_UPDATE_PERSON_TOKEN, person.getToken(),person.getId()) > 0;
	}

	@Override
	public boolean isLognned(String name, String token) {
	String sql="SELECT count(*) FROM person WHERE name = ? AND token = ?";
		
		Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, name,token);
			
		
		return cnt != null && cnt > 0;
		
		
		//this.jdbcTemplate.queryForObject(sql, Integer.class,                     user, token);
        //this.jdbcTemplate.queryForObject( sql, Integer.class, new Object[] { user,token });
	}

	private final String SQL_INSERT_ORDERS = "insert into Orders( clientId, taxiId, state,route) values(?,?,?,?)";
	public boolean createOrders(Orders orders) {
		return jdbcTemplate.update(SQL_INSERT_ORDERS,  orders.getClientId(), orders.getTaxiId(), orders.getState(),orders.getRoute()) > 0;
	}

	@Override
	public Long getPersonIdByUserToken(String name, String token) {
		
		String sql="SELECT id FROM person WHERE name = ? AND token = ?";
			
		Long id = jdbcTemplate.queryForObject(sql, Long.class, name,token);
				
			
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
	private final String SQL_UPDATE_ORDERS = "update person set clientId = ?, taxiId = ?, state  = ? ,route=? where id = ?";
	public boolean updateOrders(Orders orders) {
		return jdbcTemplate.update(SQL_UPDATE_PERSON, orders.getClientId(), orders.getTaxiId(), orders.getState(),orders.getRoute(),
				orders.getId()) > 0;
		
	}

	


	@Override
	public boolean getPersonLogOutIdByUserToken(String user, String token) {
		 final String SQL_UPDATE_PERSON_TOKEN = "update people set token=null where    firstname = ? and token  = ?";
		
			return jdbcTemplate.update(SQL_UPDATE_PERSON_TOKEN, user,token) > 0;
		}
			
	//---------------------------------------------------------------------------------------------
	private final String SQL_FIND_TAXI = "select * from taxi where id = ?";
	public Taxi getTaxiById(Long id) {
		return jdbcTemplate.queryForObject(SQL_FIND_TAXI, new Object[] { id }, new TaxiMapper());
	}			
	
	
	private final String SQL_UPDATE_TAXI = "update taxi set name = ?, email = ?, passw  = ?,token= ?,state= ? where id = ?";
	
	public boolean updateTaxi(Taxi taxi) {
		return jdbcTemplate.update(SQL_UPDATE_TAXI, taxi.getName(),taxi.getEmail(),taxi.getPassw(),taxi.getToken(),taxi.getState(),
				taxi.getId()) > 0;
	}
	
	
}