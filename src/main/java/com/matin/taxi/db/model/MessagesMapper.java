package com.matin.taxi.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessagesMapper  implements RowMapper<Messages> {

	  @Override
	  public Messages mapRow(ResultSet rs, int rowNum) throws SQLException {
		Messages messages = new Messages();
		messages.setId(rs.getLong("id"));
		messages.setState(rs.getInt("state"));
		messages.setCreated_at(rs.getTimestamp("created_at"));
		messages.setMessage(rs.getString("message"));
		messages.setOrderId(rs.getLong("orderId"));
		messages.setFrom(rs.getLong("from"));
		messages.setTo(rs.getLong("to"));
        return messages;
	  }
	}