package com.matin.taxi.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PersonMapper implements RowMapper<Person> {

	public Person mapRow(ResultSet resultSet, int i) throws SQLException {

		Person person = new Person();
		person.setId(resultSet.getLong("id"));
		person.setFirstName(resultSet.getString("firstname"));
		person.setLastName(resultSet.getString("lastname"));
		person.setAge(resultSet.getInt("age"));
		
		person.setToken(resultSet.getString("token"));
		return person;
	}
}