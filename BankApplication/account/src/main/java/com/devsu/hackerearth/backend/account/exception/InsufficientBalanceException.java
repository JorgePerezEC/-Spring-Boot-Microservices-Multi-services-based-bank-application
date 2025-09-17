package com.devsu.hackerearth.backend.account.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException() {
        super("Saldo no disponible");
    }

    public InsufficientBalanceException(Double currentBalance, Double requestedAmount) {
        super("Saldo no disponible. Saldo actual: " + currentBalance + ", Monto solicitado: " + requestedAmount);
    }
    
}
