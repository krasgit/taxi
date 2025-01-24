package com.matin.taxi.db;

import com.matin.taxi.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessionAttributes")
public class SessionAttributes extends BaseEntity {
	
	@Column(name = "session_id") 
	int sessionId;// integer NOT NULL,
	
	@Column(name = "name") 
    String name;// VARCHAR(200) NOT NULL,
	
	@Column(name = "bytes",  columnDefinition="bytea") 
	byte[] bytes;//" BYTEA NOT NULL,
	
	
	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
