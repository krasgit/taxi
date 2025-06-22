package com.matin.taxi.webSocket;

public class ArgFeatures {
	
	private String features = "";

	private String delemiter="";
	
	public ArgFeatures(String delemiter) {
		this.delemiter=delemiter;
	}

	public ArgFeatures addFeatures(Object... features) {
	    StringBuilder str = new StringBuilder(this.getFeatures());
	    for (Object feature : features) {
	        if (!str.isEmpty())
	            str.append(", ");
	        
	        if(feature.getClass().equals(String.class)) 
	        	str.append(delemiter+feature+delemiter);
	        else 
	        	str.append(feature);
	    }
	    this.setFeatures(str.toString());
	    
	    return this;
	}

	private void setFeatures(String features) {
		this.features=features;
		
	}

	private String getFeatures() {

		return features;
	}

	public static void main(String[] args){


		
		
		String ret=new ArgFeatures("'").addFeatures("34","B",66).addFeatures(44).get();
		//System.out.print(ret);
	
		int y=10;
		
		String o="########\n";
		 for(int b=y;b!=0;b--) 
				System.out.print("#");

		 for(int b=y;b!=0;b--)
		 {
				System.out.print("#");
				
		 }
		 for(int b=y;b!=0;b--) 
				System.out.print("#");
		
	}

	String get() {
		return features;
		
	}
	
}