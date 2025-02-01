package com.matin.taxi.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TaxiMapper implements RowMapper<Taxi> {

	public Taxi mapRow(ResultSet resultSet, int i) throws SQLException {

		Taxi taxi = new Taxi();
		taxi.setId(resultSet.getInt("id"));
		
		taxi.setName(resultSet.getString("state"));
		taxi.setEmail(resultSet.getString("email"));
		taxi.setPassw(resultSet.getString("passw"));
		taxi.setToken(resultSet.getString("token"));
		taxi.setState(resultSet.getInt("state"));
		
		
		return taxi;
	}
}
