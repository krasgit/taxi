package com.matin.taxi.db.model;

import java.time.LocalDate;

public class Orders {

	Long id;
	Long clientId; 
	Long taxiId ;
	int state ;
	String route ;
	java.time.LocalDate createTime  ; 
	
	public Orders() {
	
	}
	public Orders( Long clientId, Long taxiId, int state, String route) {
		super();
		
		this.clientId = clientId;
		this.taxiId = taxiId;
		this.state = state;
		this.route = route;
		
	}
	
	
	public Orders(Long id, Long clientId, Long taxiId, int state, String route, LocalDate createTime) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.taxiId = taxiId;
		this.state = state;
		this.route = route;
		this.createTime = createTime;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getTaxiId() {
		return taxiId;
	}
	public void setTaxiId(Long taxiId) {
		this.taxiId = taxiId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public java.time.LocalDate getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.time.LocalDate createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "Orders [id=" + id + ", clientId=" + clientId + ", taxiId=" + taxiId + ", state=" + state + ", route="
				+ route + ", createTime=" + createTime + "]";
	}
	
	
}
