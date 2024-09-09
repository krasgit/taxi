/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matin.taxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@ImportRuntimeHints(TaxiRuntimeHints.class)
public class TaxiApplication {

	public static void main(String[] args) {

		SpringApplication sa = new SpringApplication(TaxiApplication.class);
		sa.setLogStartupInfo(false);
		// sa.setBannerMode(Banner.Mode.OFF);
		sa.run(args);
		// SpringApplication.run(TaxiApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<ReverceProxyFilter> filterRegistrationBean() {

		// Filter Registration Bean
		FilterRegistrationBean<ReverceProxyFilter> registrationBean = new FilterRegistrationBean();

		// Configure Authorization Filter
		registrationBean.setFilter(new ReverceProxyFilter());

		// Specify URL Pattern
		registrationBean.addUrlPatterns("/tile/*");

		// Set the Execution Order of Filter
		registrationBean.setOrder(2);

		return registrationBean;
	}

}
