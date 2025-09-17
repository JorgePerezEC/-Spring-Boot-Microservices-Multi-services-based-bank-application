package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "clients")
public class Client extends Person {
	@NotBlank(message = "Password is required")
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean isActive;

	// Cosnturctors
	public Client() {
		this.isActive = true; // Default
	}

	public Client(String name, String dni, String gender, Integer age, String address, String phone, String password,
			boolean isActive) {
		super(name, dni, gender, age, address, phone);
		this.password = password;
		this.isActive = isActive;
	}

	// Getters and Setters

	// Password
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// isActive
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}
}
