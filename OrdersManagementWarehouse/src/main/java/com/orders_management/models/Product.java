package com.orders_management.models;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private int stock_quantity;

    public Product(){

    }

    public Product(int id, String name, BigDecimal price, int stock_quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stock_quantity +
                '}';
    }
}

