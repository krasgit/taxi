package com.matin.taxi.db.model;

import java.util.List;

import com.matin.taxi.db.model.Person;



public interface PersonDAO {
	Person getPersonById(Long id);

	List<Person> getAllPersons();

	boolean deletePerson(Person person);

	boolean updatePerson(Person person);

	boolean createPerson(Person person);
}
