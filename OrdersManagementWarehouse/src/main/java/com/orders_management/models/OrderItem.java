package com.orders_management.models;

import java.math.BigDecimal;
public class OrderItem {
    private int id;
    private int order_id;
    private int product_id;
    private int quantity;
    private BigDecimal subtotal;
    public OrderItem(){}
    public OrderItem(int id, int order_id, int product_id, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "order_item_id=" + id +
                ", order_id=" + order_id +
                ", product_id=" + product_id +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}