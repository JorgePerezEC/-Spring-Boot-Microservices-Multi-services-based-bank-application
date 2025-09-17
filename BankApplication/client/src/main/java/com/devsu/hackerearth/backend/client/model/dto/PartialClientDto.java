package com.devsu.hackerearth.backend.client.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialClientDto {

    private Boolean isActive;
    private String password;
    private String address;
    private String phone;

    public PartialClientDto(Boolean isActive) {
        this.isActive = isActive;
        this.password = null;
        this.address = null;
        this.phone = null;
    }

}
