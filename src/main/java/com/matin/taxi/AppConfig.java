package com.matin.taxi;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

//@Configuration
//@ComponentScan("com.matin.taxi.db.model")
//@PropertySource("classpath:database.properties")
public class AppConfig {

	@Autowired
	Environment environment;

	private final String URL = "spring.datasource.url";
	private final String USER = "spring.datasource.username";
	private final String DRIVER = "driver";
	private final String PASSWORD = "spring.datasource.password";

	//@Bean
	DataSource dataSource() {
		
		//spring.datasource.url=jdbc:postgresql://localhost:5433/nominatim
		//	spring.datasource.username=nominatim
		//	spring.datasource.password=very_secure_password
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		String propertyURL = environment.getProperty(URL);
		driverManagerDataSource.setUrl("jdbc:postgresql://localhost:5433/nominatim");
		driverManagerDataSource.setUsername("nominatim");
		driverManagerDataSource.setPassword("very_secure_password");
		//driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
		return driverManagerDataSource;
	}
}
