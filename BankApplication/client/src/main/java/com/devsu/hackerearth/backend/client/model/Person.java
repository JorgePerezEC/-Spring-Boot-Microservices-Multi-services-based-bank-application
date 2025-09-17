package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@MappedSuperclass
public class Person extends Base {

	@NotBlank(message = "Name is required")
	@Column(nullable = false)
	private String name;

	@NotBlank(message = "Dni is required")
	@Column(nullable = false, unique = true)
	private String dni;

	@NotBlank(message = "Gender is required")
	private String gender;

	@Positive(message = "Age must be higher than zero")
	@Column(nullable = false)
	private Integer age;

	@NotBlank(message = "Address is required")
	private String address;

	@NotBlank(message = "Phone is required")
	private String phone;

	// Constructor
	public Person() {
	}

	public Person(String name, String dni, String gender, Integer age, String address, String phone) {
		this.name = name;
		this.dni = dni;
		this.gender = gender;
		this.age = age;
		this.address = address;
		this.phone = phone;
	}

	// Getters & Setters
	// Name
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Dni
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	// Gender
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	// Age
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	// Address
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// Phone
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
