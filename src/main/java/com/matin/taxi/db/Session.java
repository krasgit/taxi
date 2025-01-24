package com.matin.taxi.db;

import com.matin.taxi.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "session")
public class Session extends BaseEntity {

	@Column(name = "session_Id") 
	 String sessionId;
	
	@Column(name = "principal_Id") 
	 int principalId;

	
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public int getPrincipalId() {
		return principalId;
	}
	public void setPrincipalId(int principalId) {
		this.principalId = principalId;
	}
	
}
