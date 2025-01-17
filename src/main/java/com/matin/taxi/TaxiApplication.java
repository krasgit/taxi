package com.matin.taxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;


import com.matin.taxi.db.model.*;




@SpringBootApplication
@ImportRuntimeHints(TaxiRuntimeHints.class)
public class TaxiApplication {
	private static int port = 8282;;

	public static void main(String[] args) {

		try {
		//	HttpProxyServer myProxy = new HttpProxyServer(port);
		//	myProxy.listen();
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
		SpringApplication.run(TaxiApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		System.out.println("List of person is:");

		for (Person p : personDAO.getAllPersons()) {
			System.out.println(p);
		}
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
		registrationBean.addUrlPatterns(ReverceProxyFilterSearch.urlPatterns);

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
/*
	 @Bean
	    public FilterRegistrationBean filterSessionBean() {
	        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new SessionFilter());
	        registrationBean.addUrlPatterns("/*");
	        registrationBean.setOrder(1);
	        return registrationBean;
	    }
	
*/
	@Bean
	public FilterRegistrationBean<ReverceProxyFilterSearchPhoton> filterSearchPhotonBean() {

		// Filter Registration Bean
		FilterRegistrationBean<ReverceProxyFilterSearchPhoton> registrationBean = new FilterRegistrationBean<ReverceProxyFilterSearchPhoton>();

		// Configure Authorization Filter
		registrationBean.setFilter(new ReverceProxyFilterSearchPhoton());

		// Specify URL Pattern
		registrationBean.addUrlPatterns(ReverceProxyFilterSearchPhoton.urlPatterns);

		// Set the Execution Order of Filter
		registrationBean.setOrder(2);

		return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean<SuperCookieFilter> filterBean() {

		// Filter Registration Bean
		FilterRegistrationBean<SuperCookieFilter> filterBean = new FilterRegistrationBean<SuperCookieFilter>();

		// Configure Authorization Filter
		filterBean.setFilter(new SuperCookieFilter());

		// Specify URL Pattern
		filterBean.addUrlPatterns(SuperCookieFilter.urlPatterns);

		// Set the Execution Order of Filter
		filterBean.setOrder(2);

		return filterBean;
	}
	
}
