package com.matin.taxi.db.model;


/*
 CREATE TABLE taxi (
   id integer PRIMARY KEY,
   name text NOT NULL      CONSTRAINT name_unique unique,
   email VARCHAR(100),
   passw VARCHAR(100) NOT null,
   token VARCHAR(100),
   state int
    
)
 */

public class Taxi {
	
	private int id ;
	private String    name;
	private String    email;
	private String    passw;
	private String   token;
	private int   state;
	
	public Taxi(int id, String name, String email, String passw, String token, int state) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.passw = passw;
		this.token = token;
		this.state = state;
	}
	
	public Taxi() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}
	public  void setId(int id) {
		this.id = id;
	}
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

}
