package com.matin.taxi.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ProfferMapper implements RowMapper<Proffer> {

	public Proffer mapRow(ResultSet rs, int i) throws SQLException {

		Proffer proffer = new Proffer();
		proffer.setId(rs.getInt("id"));
		proffer.setState(rs.getInt("state"));
		proffer.setOrderId(rs.getLong("orderId"));
		
		proffer.setCreated_at(rs.getTimestamp("created_at"));
		return proffer;
	}
}