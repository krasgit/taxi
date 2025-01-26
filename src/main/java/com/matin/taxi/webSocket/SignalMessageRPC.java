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
public class SignalMessageRPC {
	
	 // {"id":"9cb8392625585","procedure":"multiplyByOneThousand","args":[3]}
	private String id;
	private String procedure;
    private Object args;
	
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	public Object getArgs() {
		return args;
	}
	public void setArgs(Object args) {
		this.args = args;
	}
	
	
    
}
