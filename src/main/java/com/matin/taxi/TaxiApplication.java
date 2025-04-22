package com.matin.taxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(TaxiRuntimeHints.class)
public class TaxiApplication {
	//private static int port = 8282;;

	public static void main(String[] args) {

		/*
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode yourObj = mapper.readTree("{\"timestamp\":1744117809345,\"coords\":{\"accuracy\":16.348,\"latitude\":43.2206817,\"longitude\":27.8976221}}");
			JsonNode coords = yourObj.path("coords");
			String latitude = coords.path("latitude").asText();
			String longitude = coords.path("longitude").asText();
			System.out.println(yourObj.toString());
		}
		 catch (JsonProcessingException e) {e.printStackTrace();}
		
		String orderc="{\"coord\":[{\"lon\":27.8902718,\"lat\":43.22973060000004,\"name\":\"Quattro, Тролейна, Северна промишлена зона, ж.к. Младост 2, Младост\",\"waypointName\":\"Start\"},{\"lon\":27.909923919239958,\"lat\":43.19375095000001,\"name\":\"Cargo port of Varna, кв. Аспарухово, Аспарухово\",\"waypointName\":\"End\"}]}";
        try {
			JsonNode yourObj = mapper.readTree(orderc);
			JsonNode coords = yourObj.path("coord");
			JsonNode coord =coords.get(0);
			String latitude = coord.path("lat").asText();
			String longitude = coord.path("lon").asText();
			System.out.println(latitude+""+longitude);
		} catch (Exception e) {	e.printStackTrace();}
		
		String r="{\"code\":\"Ok\",\"routes\":[{\"geometry\":\"}oxfGswgiDYSA[fJsL`F_IlASR_AGq@e@u@yD{Ka@yD?sP_@eFy@sEwDuLq@sCe@kD[gF\",\"legs\":[{\"steps\":[],\"distance\":1734.3,\"duration\":162.9,\"summary\":\"\",\"weight\":167.1}],\"distance\":1734.3,\"duration\":162.9,\"weight_name\":\"routability\",\"weight\":167.1}],\"waypoints\":[{\"hint\":\"XWEOgGhhDoA5AAAASwAAABoAAADfAAAALMo_QVlGeEGgRq5Am605QhwAAAAmAAAADQAAAG8AAADnAAAAZK-pAZN-kwIUr6kByH6TAgEA7w0EQja-\",\"distance\":8.770086,\"name\":\"\",\"location\":[27.8977,43.220627]},{\"hint\":\"qAQBgP___38CAAAAGgAAADsAAAAAAAAA4FI7QJSN00H2n4JCAAAAAAIAAAAaAAAAOwAAAAAAAADnAAAAcfWpAZR-kwJ39akBPn6TAgMAbwUEQja-\",\"distance\":9.566671,\"name\":\"бул. Васил Левски\",\"location\":[27.915633,43.220628]}]}";		
		try {
			JsonNode yourObj = mapper.readTree(r);
			JsonNode coords = yourObj.path("routes");
			JsonNode coord =coords.get(0);
		
			long duration = coord.path("duration").asLong();
			long distance = coord.path("distance").asLong();
			System.out.println(distance+" "+duration);
		}  catch (JsonProcessingException e) {	e.printStackTrace();}
		*/
		
		SpringApplication.run(TaxiApplication.class, args);
		
		/*
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		
		Orders order=new Orders(null, new Long(1), new Long(2), 3, "route", LocalDate.now());
		personDAO.createOrders(order);
		
		order.setRoute("sssss");
		
		
		personDAO.updateOrders(order);
		*/
		
		/**
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		System.out.println("List of person is:");
		for (Person p : personDAO.getAllPersons()) {
			System.out.println(p);
		}
		*/
/*
		try {
		Long a=new Long(5);
		Person ff=personDAO.getPersonById(a);
		}
		catch (EmptyResultDataAccessException e )
		{
			
		}
		*/
	
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
