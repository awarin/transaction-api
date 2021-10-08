package com.alex.exceptions;

public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException(Long id) {
        super(String.format("Could not find transaction with id=%s", id));
    }
}
