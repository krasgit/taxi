package com.matin.taxi.db.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class OrdersMapper implements RowMapper<Orders> {

	public Orders mapRow(ResultSet resultSet, int i) throws SQLException {

		Orders order = new Orders();
		order.setId(resultSet.getLong("id"));
		order.setClientId(resultSet.getLong("clientId"));
		order.setTaxiId(resultSet.getLong("taxiId"));
		order.setState(resultSet.getInt("state"));
		order.setRoute(resultSet.getString("route"));
		order.setCreateTime(resultSet.getTimestamp("createTime"));
		order.setClientStartTime(resultSet.getTimestamp("clientStartTime"));
		order.setAcceptedTime(resultSet.getTimestamp("acceptedTime"));
		order.setTaxiStartTime(resultSet.getTimestamp("taxiStartTime"));
		order.setEndTime(resultSet.getTimestamp("endTime"));
		
	return order;
	}
}