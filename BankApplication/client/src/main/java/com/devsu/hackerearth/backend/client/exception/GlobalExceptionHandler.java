package com.devsu.hackerearth.backend.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Void> handleClientNotFoundException(ClientNotFoundException ex) {
        System.out.println(
                "=== DEBUG: GlobalExceptionHandler capturó ClientNotFoundException: " + ex.getMessage() + " ===");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicateDniException.class)
    public ResponseEntity<String> handleDuplicateDniException(DuplicateDniException ex) {
        System.out.println(
                "=== DEBUG: GlobalExceptionHandler capturó DuplicateDniException: " + ex.getMessage() + " ===");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        System.out.println("=== DEBUG: GlobalExceptionHandler capturó Exception genérica: "
                + ex.getClass().getSimpleName() + " - " + ex.getMessage() + " ===");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + ex.getMessage());
    }
}
