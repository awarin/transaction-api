package com.alex.exceptions;

import com.alex.models.internal.TransactionStatus;

public class InvalidStatusChangeException extends Exception {
    public InvalidStatusChangeException(TransactionStatus init, TransactionStatus target) {
        super(String.format("Cannot change status from %s to %s", init, target));
    }
}
