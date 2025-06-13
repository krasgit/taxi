package com.matin.taxi.webSocket;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgsFeaturesJson {

	private static final Logger LOG = LoggerFactory.getLogger(ArgsFeaturesJson.class);
	
	
	HashMap<String, Object> params = new HashMap<String, Object>();

	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}

	
	public ArgsFeaturesJson addFeatures(String name, Object value) {
		params.put(name, value);
	    
	    return this;
	}

	String get()  {
		try {
			return Utils.getString(this);
		} catch (Exception e) {
			LOG.error("", e);
		}
		
		return null;
		
	} 
	public static void main(String[] args) throws Exception{

		String a = new ArgsFeaturesJson().addFeatures("qaz", "val1").addFeatures("qazq", 45).get();
		System.out.print(a);
		
	
	}

	
	
}