package com.orders_management.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Orders {
    private int id;
    private int customer_id;
    private LocalDateTime order_date;
    private String status;
    private String address;
    private BigDecimal total_price;

    // Constructors, getters, and setters

public Orders(){}
    public Orders(int id, int customer_id, LocalDateTime order_date, String status, String address, BigDecimal total_price) {
        this.id = id;
        this.customer_id = customer_id;
        this.order_date = order_date;
        this.status = status;
        this.address = address;
        this.total_price = total_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public LocalDateTime getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "order_id=" + id +
                ", customer_id=" + customer_id +
                ", order_date=" + order_date +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", total_price=" + total_price +
                '}';
    }
}

