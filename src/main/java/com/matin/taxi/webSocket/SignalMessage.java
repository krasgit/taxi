package com.matin.taxi.webSocket;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalMessage {
	
	public final static String logned = "logned"; 
	public final static String notlogned = "notlogned";
	
	

	
    @Override
	public String toString() {
		return "SignalMessage [type=" + type + ", sender=" + sender + ", receiver=" + receiver + ", data=" + data + "]";
	}
    private String id;
    
    private String procedure;
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String type;
    private String sender;
    private String receiver;

    
    private Object data;
    
    
    @JsonProperty("map")
    public HashMap<?, ?>  map;
    
    public HashMap<?, ?> getMap() {
		return this.map;
	}
    /*
	public void setMap(String map) {
		this.map = map;
	}
    */
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
    public String getUser() {
		return user;
	}
	public void setArg(String arg) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	private String user;
    private String token;

}
