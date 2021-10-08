package com.alex.models.rest;

import com.alex.models.internal.OrderRow;

import java.math.BigDecimal;

public class RestOrderRow {
    private int quantity;
    private BigDecimal price;
    private String name;

    public RestOrderRow() {
    }

    public RestOrderRow(int quantity, BigDecimal price, String name) {
        this.quantity = quantity;
        this.price = price;
        this.name = name;
    }

    public OrderRow toOrder(Long transactionId) {
        var order = new OrderRow(getQuantity(), getPrice(), getName());
        order.setTransactionId(transactionId);

        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
