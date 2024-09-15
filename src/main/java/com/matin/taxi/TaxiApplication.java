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

import proxy.HttpProxyServer;

import java.util.Locale;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@ImportRuntimeHints(TaxiRuntimeHints.class)
public class TaxiApplication {
	  private static int port=8282;;
	public static void main(String[] args) {
		try {
	        HttpProxyServer myProxy = new HttpProxyServer(port);
	        myProxy.listen();
	    	}catch (Exception e) {
	    		System.err.print(e.getMessage());
	        }
		SpringApplication.run(TaxiApplication.class, args);
		
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
		
		@Bean
		public FilterRegistrationBean<ReverceProxyFilterSearch> filterSearchBean() {

			// Filter Registration Bean
			FilterRegistrationBean<ReverceProxyFilterSearch> registrationBean = new FilterRegistrationBean<ReverceProxyFilterSearch>();

			// Configure Authorization Filter
			registrationBean.setFilter(new ReverceProxyFilterSearch());

			// Specify URL Pattern
			registrationBean.addUrlPatterns("/search/*");

			// Set the Execution Order of Filter
			registrationBean.setOrder(2);

			return registrationBean;
		}
		
		
		@Bean
		public FilterRegistrationBean<ReverceProxyFilterReverse> filterReverseBean() {

			// Filter Registration Bean
			FilterRegistrationBean<ReverceProxyFilterReverse> registrationBean = new FilterRegistrationBean<ReverceProxyFilterReverse>();
			// Configure Authorization Filter
			registrationBean.setFilter(new ReverceProxyFilterReverse());

			// Specify URL Pattern
			registrationBean.addUrlPatterns("/reverse/*");

			// Set the Execution Order of Filter
			registrationBean.setOrder(2);

			return registrationBean;
		}
		
		
		
		
		@Bean
		public FilterRegistrationBean<ReverceProxyFilterRoute> filterRouteBean() {

			// Filter Registration Bean
			FilterRegistrationBean<ReverceProxyFilterRoute> registrationBean = new FilterRegistrationBean<ReverceProxyFilterRoute>();
			// Configure Authorization Filter
			registrationBean.setFilter(new ReverceProxyFilterRoute());

			// Specify URL Pattern
			registrationBean.addUrlPatterns("/route/*");

			// Set the Execution Order of Filter
			registrationBean.setOrder(2);

			return registrationBean;
		}
	
	}


