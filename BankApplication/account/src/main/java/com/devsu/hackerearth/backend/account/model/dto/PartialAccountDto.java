package com.devsu.hackerearth.backend.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialAccountDto {

	private Boolean isActive;
	private String type;

	public PartialAccountDto(Boolean isActive) {
		this.isActive = isActive;
		this.type = null;
	}
}
