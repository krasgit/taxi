package com.matin.taxi;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

//@Configuration
//@ComponentScan("com.matin.taxi.db.model")
//@PropertySource("classpath:database.properties")
public class AppConfig {

	private final String URL = "jdbc:postgresql://localhost:5433/nominatim";
	private final String USER = "nominatim";
	//private final String DRIVER = "driver";
	private final String PASSWORD = "very_secure_password";

	DataSource dataSource() {
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUrl(URL);
		driverManagerDataSource.setUsername(USER);
		driverManagerDataSource.setPassword(PASSWORD);
		//driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
		return driverManagerDataSource;
	}
}
