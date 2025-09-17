package com.devsu.hackerearth.backend.client.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Constructors
	public Base() {

	}

	public Base(Long id) {
		this.id = id;
	}

	// Getter
	public Long getId() {
		return id;
	}

	// Setter
	public void setId(Long id) {
		this.id = id;
	}

}
