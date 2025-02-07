package com.matin.taxi.db.model;

import java.util.List;

import com.matin.taxi.db.model.Person;



public interface PersonDAO {
	Person getPersonByPrincipal(String  principal);
	
	Person getPersonById(Long id);

	List<Person> getAllPersons();

	boolean deletePerson(Person person);

	boolean updatePerson(Person person);
	
	boolean updatePersonToken(Person person);

	boolean createPerson(Person person);

	boolean isLognned(String user, String token);
	
	boolean createOrders(Orders person);

	Long getPersonIdByUserToken(String user, String token);
	Orders getOrdersByClientIdState(Long clientId,int state);
	
	boolean updateOrders(Orders orders);

	boolean getPersonLogOutIdByUserToken(String user, String token);

	Person getLognned(String user, String token);

	String getOrdersByClientId(Long clientId);
	public Orders getOrderById(Long id) ;

	public boolean deleteOrderById(Long id) ;
	
	
	public Person getPersonByToken(String token);
	
	
	//
	
}
