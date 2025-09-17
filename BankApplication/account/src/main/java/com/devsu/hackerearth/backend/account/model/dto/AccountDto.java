package com.devsu.hackerearth.backend.account.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

	private Long id;

	@NotBlank(message = "Account number is required")
	private String number;

	@NotBlank(message = "Account type is required")
	private String type;

	@NotNull(message = "Initial amount is required")
	@PositiveOrZero(message = "Initial amount must be positive or zero")
	private Double initialAmount;

	private boolean isActive;

	@NotNull(message = "Client ID is required")
	private Long clientId;
}
