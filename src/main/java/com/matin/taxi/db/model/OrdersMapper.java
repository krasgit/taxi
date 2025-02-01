package com.matin.taxi.db.model;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.jdbc.core.RowMapper;

public class OrdersMapper implements RowMapper<Orders> {

	public Orders mapRow(ResultSet resultSet, int i) throws SQLException {

		Orders order = new Orders();
		order.setId(resultSet.getLong("id"));
		order.setClientId(resultSet.getLong("clientId"));
		order.setTaxiId(resultSet.getLong("taxiId"));
		order.setState(resultSet.getInt("state"));
		order.setRoute(resultSet.getString("route"));
		Date input = resultSet.getDate("createTime");
		LocalDate date = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());
		order.setCreateTime(date);
		return order;
	}
}