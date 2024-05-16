package com.orders_management.logic;

import com.orders_management.logic.Validator;
import com.orders_management.models.Orders;
import com.orders_management.data_access.OrdersDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public class OrdersBLL {

    public static Orders findOrderById(int id) {
        OrdersDAO ordersDAO = new OrdersDAO();
        Orders order = ordersDAO.findById(id);
        if (order == null) {
            throw new NoSuchElementException("The order with id = " + id + " was not found!");
        }
        return order;
    }

    public static List<Orders> findAllOrders() {
        OrdersDAO ordersDAO = new OrdersDAO();
        List<Orders> orders = ordersDAO.findAll();
        return orders;
    }

    public static Orders insertOrder(Orders order) {
        if (!Validator.isDateBeforeToday(order.getOrder_date())) {
            throw new IllegalArgumentException("Order date must be before today.");
        }
        if (!Validator.isPositiveNumber(order.getTotal_price())) {
            throw new IllegalArgumentException("Total price must be a positive number.");
        }
        OrdersDAO ordersDAO = new OrdersDAO();
        Orders newOrder = ordersDAO.insert(order);
        return newOrder;
    }

    public static Orders updateOrder(Orders order) {
        if (!Validator.isDateBeforeToday(order.getOrder_date())) {
            throw new IllegalArgumentException("Order date must be before today.");
        }
        if (!Validator.isPositiveNumber(order.getTotal_price())) {
            throw new IllegalArgumentException("Total price must be a positive number.");
        }
        OrdersDAO ordersDAO = new OrdersDAO();
        Orders updatedOrder = ordersDAO.update(order);
        return updatedOrder;
    }

    public static boolean deleteOrder(Orders order) {
        OrdersDAO ordersDAO = new OrdersDAO();
        boolean flag = ordersDAO.delete(order);
        return flag;
    }
}
