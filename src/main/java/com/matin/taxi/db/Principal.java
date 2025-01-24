package com.matin.taxi.db;

import com.matin.taxi.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "principal")
public class Principal extends BaseEntity { 

	
	@Column(name = "name") 
		
	String name;  
	@Column(name = "email")
	String  email;
	@Column(name = "passw")
	String  passw;
	@Column(name = "token")
	String  token;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassw() {
		return passw;
	}
	public void setPassw(String passw) {
		this.passw = passw;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
