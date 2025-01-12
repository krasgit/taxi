package com.matin.taxi.db.WebSocet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootApplication

public class StartApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("StartApplication...");

        startCustomerApp();

    }

    // Tested with H2 database
    void startCustomerApp() {

    	JdbcIndexedSessionRepository customerRepository=new JdbcIndexedSessionRepository( jdbcTemplate);
    	
    	
    	
        jdbcTemplate.execute("DROP TABLE IF EXISTS SESSION CASCADE; ");
        jdbcTemplate.execute("CREATE TABLE SESSION(" +
                "id SERIAL, name VARCHAR(255), age NUMERIC(2), created_date timestamp)");
        
        
        customerRepository.insert();
     //   customerRepository.insert();

    }

}