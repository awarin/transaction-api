package com.alex.models.rest;

import com.alex.models.internal.PaymentType;
import com.alex.models.internal.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;

public class RestTransaction {
    private Long id;
    private BigDecimal amount;
    private TransactionStatus status;
    private List<RestOrderRow> order;
    private PaymentType paymentType;
    private Long version;

    public RestTransaction() {
    }

    public RestTransaction(Long id, BigDecimal amount, TransactionStatus status, List<RestOrderRow> order, PaymentType paymentType, Long version) {
        this.amount = amount;
        this.status = status;
        this.order = order;
        this.paymentType = paymentType;
        this.id = id;
        this.version = version;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public List<RestOrderRow> getOrder() {
        return order;
    }

    public void setOrder(List<RestOrderRow> order) {
        this.order = order;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
