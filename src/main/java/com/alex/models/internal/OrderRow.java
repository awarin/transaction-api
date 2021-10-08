package com.alex.models.internal;

import com.alex.models.rest.RestOrderRow;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("order_row")
public class OrderRow {
    @Id
    private Long id;
    private Long transactionId;
    private int quantity;
    private BigDecimal price;
    private String name;

    public OrderRow(int quantity, BigDecimal price, String name) {
        this.quantity = quantity;
        this.price = price;
        this.name = name;
    }

    public RestOrderRow toRestOrderRow() {
        return new RestOrderRow(getQuantity(), getPrice(), getName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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
