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
		proffer.setPersonId(rs.getLong("personId"));
		proffer.setCreated_at(rs.getTimestamp("created_at"));
		
		
		 try{
		        rs.findColumn("difference");
		        proffer.setDiff(rs.getInt("difference"));
		    } catch (SQLException sqlex){
		        //logger.debug("column doesn't exist {}", column);
		    }
		
					
		
		
		return proffer;
	}
}