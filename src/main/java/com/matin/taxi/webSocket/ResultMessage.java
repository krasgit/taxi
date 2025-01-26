package com.matin.taxi.webSocket;

public class ResultMessage {

	
	private String id;
	private Object result;
	private String  error;
	
	public ResultMessage(String id, Object result, String error) {
		super();
		this.id = id;
		this.result = result;
		this.error = error;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
