package com.matin.taxi.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.jdbc.core.RowMapper;

public class PositionMapper  implements RowMapper<Position> {

	@Override
	public Position mapRow(ResultSet rs, int rowNum) throws SQLException {
		Position order = new Position();
		order.setId(rs.getLong("id"));
		order.setPersonId(rs.getLong("personId"));
		order.setPosition(rs.getString("position"));
		order.setCreated_at(rs.getTimestamp("created_at"));
		
	return order;
	}
	

}
