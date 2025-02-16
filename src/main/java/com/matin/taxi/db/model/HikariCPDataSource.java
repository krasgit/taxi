package com.matin.taxi.db.model;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSource {

	private final static String URL = "jdbc:postgresql://localhost:5433/nominatim";
	private final static String USER = "nominatim";
	private final static String PASSWORD = "very_secure_password";
    private static HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(HikariCPDataSource.URL);
        config.setUsername(HikariCPDataSource.USER);
        config.setPassword(HikariCPDataSource.PASSWORD);

        // Set minimum (initial) connections
        config.setMinimumIdle(5); // Default is same as maximumPoolSize

        // Set maximum connections
        config.setMaximumPoolSize(20); // Default is 10

        // Set idle connections
        config.setIdleTimeout(30000); // 30 seconds (time before idle connections are closed)

        // Other optional settings
        config.setConnectionTimeout(30000); // 30 seconds (time to wait for a connection)
        config.setMaxLifetime(1800000); // 30 minutes (maximum lifetime of a connection)

        ds = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}