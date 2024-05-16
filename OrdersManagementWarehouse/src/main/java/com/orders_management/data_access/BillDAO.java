package com.orders_management.data_access;

import com.orders_management.connection.ConnectionFactory;
import com.orders_management.models.Bill;
import javafx.util.Pair;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public static List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String sql = "SELECT o.id AS order_id, c.name AS customer_name, o.order_date, o.status, o.address, " +
                    "o.total_price, p.name AS product_name, oi.quantity, oi.subtotal " +
                    "FROM Orders o " +
                    "JOIN Customer c ON o.customer_id = c.id " +
                    "JOIN OrderItem oi ON oi.order_id = o.id " +
                    "JOIN Product p ON oi.product_id = p.id ORDER BY o.id";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            int prevID=-1;
            String customerName=null,status=null,address=null;
            LocalDateTime createdAt=null;
            BigDecimal totalAmount=null;
            Bill bill=null;
            List<Pair<String, Pair<Integer, BigDecimal>>> products=new ArrayList<>();
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                if (orderId != prevID) {
                    if (prevID != -1) {
                        bill = new Bill(prevID, customerName, createdAt, status, address, totalAmount, new ArrayList<>(products));
                        bills.add(bill);
                        products.clear();
                    }
                    prevID = orderId;
                    customerName = resultSet.getString("customer_name");
                    createdAt = resultSet.getTimestamp("order_date").toLocalDateTime();
                    status = resultSet.getString("status");
                    address = resultSet.getString("address");
                    totalAmount = resultSet.getBigDecimal("total_price");
                }
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                BigDecimal subtotal = new BigDecimal(resultSet.getString("subtotal"));
                Pair<String, Pair<Integer, BigDecimal>> product = new Pair<>(productName, new Pair<>(quantity, subtotal));
                products.add(product);
            }
            if (!products.isEmpty()) {
                bill = new Bill(prevID, customerName, createdAt, status, address, totalAmount, new ArrayList<>(products));
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bills;
    }
}
