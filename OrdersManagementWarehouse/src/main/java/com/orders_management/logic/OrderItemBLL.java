package com.orders_management.logic;

import com.orders_management.models.OrderItem;
import com.orders_management.data_access.OrderItemDAO;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderItemBLL {

    public static OrderItem findOrderItemById(int id) {
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        OrderItem orderItem = orderItemDAO.findById(id);
        if (orderItem == null) {
            throw new NoSuchElementException("The order item with id = " + id + " was not found!");
        }
        return orderItem;
    }

    public static List<OrderItem> findAllOrderItems() {
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        List<OrderItem> orderItems = orderItemDAO.findAll();
        return orderItems;
    }

    public static OrderItem insertOrderItem(OrderItem orderItem) {
        if (!Validator.isPositiveNumber(orderItem.getSubtotal())) {
            throw new IllegalArgumentException("Subtotal must be a positive number.");
        }
        if (orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        OrderItem newOrderItem = orderItemDAO.insert(orderItem);
        return newOrderItem;
    }

    public static OrderItem updateOrderItem(OrderItem orderItem) {
        if (!Validator.isPositiveNumber(orderItem.getSubtotal())) {
            throw new IllegalArgumentException("Subtotal must be a positive number.");
        }
        if (orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        OrderItem updatedOrderItem = orderItemDAO.update(orderItem);
        return updatedOrderItem;
    }

    public static boolean deleteOrderItem(OrderItem orderItem) {
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        boolean flag = orderItemDAO.delete(orderItem);
        return flag;
    }
}
