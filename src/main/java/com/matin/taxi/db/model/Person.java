package com.matin.taxi.db.model;

/*
 drop TABLE person;
 
     create  TABLE person (
	id int4 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	age int4 NULL,
	name text NULL,
	passw text NULL,
	token text NULL,
	CONSTRAINT person_pkey PRIMARY KEY (id)
);
 
 
 insert into person ( name, passw, age) values
( 'qaz', 'qaz', 21),
('wsx', 'wsx', 30)
 
 */

//firstName name
//lastName  passw
public class Person {
	
		private Long id;
		private Integer age;
		private String name;
		private String passw;

		private String token;
		
		public String getToken() {
			return token;
		}


		public void setToken(String token) {
			this.token = token;
		}


		public Person() {
		}

		
		public Person( String name, String passw,Integer age) {
			this.age = age;
			this.name = name;
			this.passw = passw;
		}
		
		public Person(Long id, Integer age, String name, String passw) {
			this.id = id;
			this.age = age;
			this.name = name;
			this.passw = passw;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassw() {
			return passw;
		}

		public void setPassw(String passw) {
			this.passw = passw;
		}

		@Override
		public String toString() {
			return "Person{" + "id=" + id + ", age=" + age + ", name='" + name + '\'' + ", passw='" + passw	+ '\'' + '}';
		}
	}

