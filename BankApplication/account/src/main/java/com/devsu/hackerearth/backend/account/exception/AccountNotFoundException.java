package com.devsu.hackerearth.backend.account.exception;

public class AccountNotFoundException extends RuntimeException {
    
    public AccountNotFoundException(Long id) {
        super("Account not found with id: " + id);
    }

    public static AccountNotFoundException byAccountNumber(String accountNumber) {
        return new AccountNotFoundException("Account not found with number: " + accountNumber);
    }

    private AccountNotFoundException(String message) {
        super(message);
    }
}
