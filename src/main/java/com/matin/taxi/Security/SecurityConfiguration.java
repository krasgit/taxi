package com.matin.taxi.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	// [...]
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("user")
			.password(passwordEncoder().encode("password"))
			.roles("USER")
			.and()
			.withUser("admin")
			.password(passwordEncoder().encode("admin"))
			.roles("ADMIN");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
