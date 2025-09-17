package com.devsu.hackerearth.backend.account.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "transactions")
public class Transaction extends Base {

	@NotNull(message = "Date is required")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date date;

	@NotBlank(message = "Transaction type is required")
	@Column(nullable = false)
	private String type;

	@NotNull(message = "Amount is required")
	@Column(nullable = false)
	private double amount;

	@NotNull(message = "Balance is required")
	@Column(nullable = false)
	private double balance;
	
	@NotNull(message = "Account Id is required")
	@Column(name = "account_id", nullable = false)
	private Long accountId;

	// Constructors
	public Transaction() {
	}

	public Transaction(Date date, String type, Double amount, Double balance, Long accountId) {
		this.date = date;
		this.type = type;
		this.amount = amount;
		this.balance = balance;
		this.accountId = accountId;
	}

	@PrePersist
	protected void onCreate() {
		if (date == null) {
			date = new Date();
		}
	}

	// Getters and Setters

	// Date
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	// Type
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// Amount
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	// Balance
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	// Account ID
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
