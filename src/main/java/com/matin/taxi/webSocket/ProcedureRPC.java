package com.matin.taxi.webSocket;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.matin.taxi.AppConfig;
import com.matin.taxi.db.model.Orders;
import com.matin.taxi.db.model.PersonDAO;

public class ProcedureRPC {
	
	public String loadOrder(ArrayList arg)
	{
		
		String user=(String)arg.get(0);
		String token=(String)arg.get(1);
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		PersonDAO personDAO = context.getBean(PersonDAO.class);
		
	    
		
		//clientid
		Long clientId = personDAO.getPersonIdByUserToken(user,token);

	
		
		Orders o=personDAO.getOrdersByClientIdState(clientId, 0);
		
		return o.getRoute();
	}
	
    public double publicSum(int a, double b) {
        return a + b;
    }

    public Object multiplyByOneThousand(ArrayList number) {
  	  return 3 * 1000;
  	
  }
    
    
    public Object sum(ArrayList arg) {
    	Object arg1 =arg.get(0);
    	Object arg2 =arg.get(1);
    
    	return ((Integer)arg1).intValue() +((Integer)arg2).intValue();
    	  //return 3 * 1000;
    	
    }
    
    
}