package com.alex.models.rest;

import com.alex.models.internal.PaymentType;
import com.alex.models.internal.Transaction;
import com.alex.models.internal.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;

public class NewTransactionRequest {
    private final BigDecimal amount;
    private final List<RestOrderRow> order;
    private final PaymentType paymentType;

    public NewTransactionRequest(BigDecimal amount, List<RestOrderRow> order, PaymentType paymentType) {
        this.amount = amount;
        this.order = order;
        this.paymentType = paymentType;
    }

    public Transaction toTransaction() {
        return new Transaction(amount, TransactionStatus.NEW, paymentType);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public List<RestOrderRow> getOrder() {
        return order;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }
}
