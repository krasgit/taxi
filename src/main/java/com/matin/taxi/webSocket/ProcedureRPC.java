package com.matin.taxi.webSocket;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProcedureRPC {
	
	
	
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