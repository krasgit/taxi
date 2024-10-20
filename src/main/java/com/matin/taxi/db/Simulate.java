package com.matin.taxi.db;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import com.matin.taxi.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "simulate")
public class Simulate extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotBlank
	private String description;
	@Column(name = "sessionId")
	private String sessionId;

	/**
	 * Creates a new instance of Visit for the current date
	 */
	public Simulate() {
		this.date = LocalDate.now();
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		
	}
	public String getSessionId() {
		return this.sessionId ;
		
	}

}
