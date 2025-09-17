package com.devsu.hackerearth.backend.account.model.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

	private Long id;

	private Date date;

	@NotBlank(message = "Transaction type is required")
	private String type;

	@NotNull(message = "Amount is required")
	private Double amount;

	private Double balance;

	@NotNull(message = "Account ID is required")
	private Long accountId;
}
