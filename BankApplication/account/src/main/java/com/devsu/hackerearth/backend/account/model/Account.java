package com.devsu.hackerearth.backend.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "accounts")
public class Account extends Base {

    @NotBlank(message = "Account number is required")
    @Column(nullable = false, unique = true)
    private String number;

    @NotBlank(message = "Account type is required")
    @Column(nullable = false)
    private String type;

    @NotNull(message = "Initial amount is required")
    @PositiveOrZero(message = "Initial amount must be positive or zero")
    @Column(nullable = false)
    private double initialAmount;

    @Column(nullable = false)
    private boolean isActive;

    @NotNull(message = "Client ID is required")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    // Constructors
    public Account() {
        this.isActive = true; // Default value
    }

    public Account(String number, String type, Double initialAmount, boolean isActive, Long clientId) {
        this.number = number;
        this.type = type;
        this.initialAmount = initialAmount;
        this.isActive = isActive;
        this.clientId = clientId;
    }

    // Getters and Setters

    // Number
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    // Acc Type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Initial Amount Acc
    public Double getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(Double initialAmount) {
        this.initialAmount = initialAmount;
    }

    // isActive
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Client Id
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
