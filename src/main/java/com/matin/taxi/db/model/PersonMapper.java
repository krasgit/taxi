package com.matin.taxi.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PersonMapper implements RowMapper<Person> {

	public Person mapRow(ResultSet resultSet, int i) throws SQLException {

		Person person = new Person();
		person.setId(resultSet.getLong("id"));
		person.setName(resultSet.getString("name"));
		person.setPassw(resultSet.getString("passw"));
		person.setAge(resultSet.getInt("age"));
		
		person.setToken(resultSet.getString("token"));
		person.setRole(resultSet.getString("role"));
		
		
		person.setActive(resultSet.getBoolean("active"));
		person.setManual(resultSet.getBoolean("manual"));
		
		person.setLabel1(resultSet.getString("label1"));
		person.setLabel2(resultSet.getString("label2"));
		return person;
	}
}