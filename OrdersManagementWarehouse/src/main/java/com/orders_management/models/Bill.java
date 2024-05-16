package com.orders_management.models;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Bill( int orderId, String customerName, LocalDateTime createdAt, String status, String address,
                   BigDecimal totalAmount, List<Pair<String, Pair<Integer, BigDecimal>>> products) {



    @Override
    public int orderId() {
        return orderId;
    }

    @Override
    public String customerName() {
        return customerName;
    }

    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }

    @Override
    public String status() {
        return status;
    }

    @Override
    public String address() {
        return address;
    }

    @Override
    public BigDecimal totalAmount() {
        return totalAmount;
    }

    public List<Pair<String, Pair<Integer, BigDecimal>>> products() {
        return products;
    }
}