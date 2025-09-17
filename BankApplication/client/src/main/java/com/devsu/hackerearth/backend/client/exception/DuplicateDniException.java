package com.devsu.hackerearth.backend.client.exception;

public class DuplicateDniException extends RuntimeException {

    public DuplicateDniException(String dni) {
        super("DNI already exists: " + dni);
    }

}
