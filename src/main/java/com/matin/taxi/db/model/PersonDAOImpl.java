package com.matin.taxi.db.model;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class PersonDAOImpl implements PersonDAO {

	 JdbcTemplate jdbcTemplate;
	 DataSourceTransactionManager transactionManager;

	private final String SQL_FIND_PERSON = "select * from person where id = ?";
	private final String SQL_DELETE_PERSON = "delete from person where id = ?";
	private final String SQL_UPDATE_PERSON = "update person set name = ?, passw = ?, age  = ? where id = ?";
	private final String SQL_GET_ALL = "select * from person";
	private final String SQL_INSERT_PERSON = "insert into person( name, passw, age) values(?,?,?)";

	
	private final String SQL_FIND_PRINCIPAL="select * from person where name = ?";
	
	@Autowired
	public PersonDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	    transactionManager = new DataSourceTransactionManager(dataSource);
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

	
	
	public Person getLognned(String name, String token) {
	String sql="SELECT * FROM person WHERE name = ? AND token = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { name,token }, new PersonMapper());
			}
		catch (EmptyResultDataAccessException e )
		{
		return null;	
		}
	//(sql, Integer.class, name,token);
	//
		
		
		
		//this.jdbcTemplate.queryForObject(sql, Integer.class,                     user, token);
        //this.jdbcTemplate.queryForObject( sql, Integer.class, new Object[] { user,token });
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
	private final String SQL_UPDATE_ORDERS = "update orders set clientId = ?, taxiId = ?, state  = ? ,route=? ,createTime=? where id = ?";
	public boolean updateOrders(Orders orders) {
		return jdbcTemplate.update(SQL_UPDATE_ORDERS
				, orders.getClientId()
				, orders.getTaxiId()
				, orders.getState()
				,orders.getRoute()
				,orders.getCreateTime(),
				orders.getId()) > 0;
		
	}

	


	@Override
	public boolean getPersonLogOutIdByUserToken(String user, String token) {
		 final String SQL_UPDATE_PERSON_TOKEN = "update person set token=null where name = ? and token  = ?";
		
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
	
	
	
	
	
	private final String SQL_INSERT_ORDERSs = "insert into Orders( clientId, taxiId, state,route,createTime) values(?,?,?,?,?)";
    //   insert into person( name, passw, age) values(?,?,?)";
public boolean createOrderss(Orders orders) {
int r = jdbcTemplate.update(SQL_INSERT_ORDERSs,  orders.getClientId(), orders.getTaxiId(), orders.getState(),orders.getRoute(),orders.getCreateTime());
return r > 0;
}

	
	

//   insert into person( name, passw, age) values(?,?,?)";
public boolean createOrdersss(Orders orders) {
	
	 GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
	 String sql = "insert into Orders( clientId, taxiId, state,route,createTime) values(?,?,?,?,?)";
	 
	 
	 int rowsAffected = jdbcTemplate.update(conn -> {
         
         // Pre-compiling SQL
         PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

         // Set parameters
         preparedStatement.setInt(1, 1);
         preparedStatement.setInt(2, 2);
         preparedStatement.setInt(3, 3);
         preparedStatement.setString(4, "JdbcTemplate");
         preparedStatement.setObject(5, LocalDateTime.now());

         return preparedStatement;
         
     }, generatedKeyHolder);

     
     // Get auto-incremented ID
     Integer id = generatedKeyHolder.getKey().intValue();
     return true;
}
	
@Transactional
public boolean createOrders(Orders orders) {
	
	 GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
	 
	 
	 String sql = "insert into Orders( clientId, taxiId, state,route,createTime) values(?,?,?,?,?)";
	 SimpleJdbcInsert insertIntoUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("Orders").usingGeneratedKeyColumns("id");
	 
	 

	    final Map<String, Object> parameters = new HashMap<>();
       // parameters.put("name", u.getName());
	    parameters.put("clientId", 1);
	    parameters.put("taxiId", 1);
	    parameters.put("state", 0);
	    parameters.put("route", "u.getName()");
	//    parameters.put("createTime",LocalDateTime.now());

         Number id = insertIntoUser.executeAndReturnKey(parameters);
         orders.setId(id.longValue());
    return true;
}
	
	/*
    public void test(Orders orders) {

       

        // Create GeneratedKeyHolder object
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO `user`(`balance`, `create_at`, `enabled`, `name`, `update_at`) VALUES(?, ?, ?, ?, ?);";

        // To insert data, you need to pre-compile the SQL and set up the data yourself.
        int rowsAffected = jdbcTemplate.update(conn -> {
            
            // Pre-compiling SQL
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set parameters
            preparedStatement.setBigDecimal(1, new BigDecimal("15.88"));
            preparedStatement.setObject(2, LocalDateTime.now());
            preparedStatement.setBoolean(3, Boolean.TRUE);
            preparedStatement.setString(4, "JdbcTemplate");
            preparedStatement.setObject(5, LocalDateTime.now());

            return preparedStatement;
            
        }, generatedKeyHolder);

        
        // Get auto-incremented ID
        Integer id = generatedKeyHolder.getKey().intValue();

       // log.info("rowsAffected = {}, id={}", rowsAffected, id);
    }
}
*/

}