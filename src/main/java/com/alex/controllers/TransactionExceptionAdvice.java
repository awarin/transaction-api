package com.alex.controllers;

import com.alex.exceptions.InconsistentAmountException;
import com.alex.exceptions.InvalidStatusChangeException;
import com.alex.exceptions.TransactionNotFoundException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionAdvice {
    @ExceptionHandler(InvalidStatusChangeException.class)
    public ResponseEntity<String> handleInvalidStatus(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLock(Exception e) {
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(e.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleNotFound(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(InconsistentAmountException.class)
    public ResponseEntity<String> handleInconsistentAmountException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
