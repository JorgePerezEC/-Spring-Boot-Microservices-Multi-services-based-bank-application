package com.devsu.hackerearth.backend.client.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

	private Long id;

	@NotBlank(message = "DNI is required")
	private String dni;

	@NotBlank(message = "DNI is required")
	private String name;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Gender is required")
	private String gender;

	@NotNull(message = "Age is required")
	@Positive(message = "Age must be positive")
	private int age;

	@NotBlank(message = "Address is required")
	private String address;

	@NotBlank(message = "Password is required")
	private String phone;

	private boolean isActive;
}
