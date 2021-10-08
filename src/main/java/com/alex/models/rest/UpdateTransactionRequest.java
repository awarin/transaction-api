package com.alex.models.rest;

import com.alex.models.internal.PaymentType;
import com.alex.models.internal.Transaction;
import com.alex.models.internal.TransactionStatus;

import java.math.BigDecimal;

public class UpdateTransactionRequest {
    private final Long version;
    private final TransactionStatus status;
    private final PaymentType paymentType;

    public UpdateTransactionRequest(PaymentType paymentType, Long version, TransactionStatus status) {
        this.paymentType = paymentType;
        this.version = version;
        this.status = status;
    }

    public Transaction toTransaction(Long id, BigDecimal amount) {
        var tx = new Transaction(amount, status, paymentType);
        tx.setVersion(version);
        tx.setId(id);

        return tx;
    }

    public Long getVersion() {
        return version;
    }

    public TransactionStatus getStatus() {
        return status;
    }
}
