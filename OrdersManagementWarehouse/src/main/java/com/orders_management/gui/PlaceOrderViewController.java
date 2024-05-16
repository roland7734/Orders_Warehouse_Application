package com.orders_management.gui;

import com.orders_management.logic.CustomerBLL;
import com.orders_management.logic.ProductBLL;
import com.orders_management.models.Customer;
import com.orders_management.models.OrderItem;
import com.orders_management.models.Orders;
import com.orders_management.models.Product;
import com.orders_management.logic.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.*;

public class PlaceOrderViewController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button placeOrderButton;

    @FXML
    private TableView<Product> tableView;

    Map<Product, Integer> productWithQuantity=new HashMap<>();

    public Map<Product, Integer> getProductWithQuantity() {
        return productWithQuantity;
    }

    public void setProductWithQuantity(Map<Product, Integer> productWithQuantity) {
        this.productWithQuantity = productWithQuantity;
    }

    private static class ProductRow {

        private final Button decreaseButton;
        private final Button increaseButton;
        private final TextField quantityField;
        private final Product product;
        private final PlaceOrderViewController placeOrderViewController;


        public ProductRow(Product product,PlaceOrderViewController placeOrderViewController) {

            this.product = product;
            this.placeOrderViewController=placeOrderViewController;

            decreaseButton = new Button("-");
            increaseButton = new Button("+");
            quantityField = new TextField("0");
            quantityField.setEditable(false);

            decreaseButton.setOnAction(event -> {
                int currentQuantity = Integer.parseInt(quantityField.getText());
                if (currentQuantity > 0) {
                    quantityField.setText(String.valueOf(currentQuantity - 1));
                    placeOrderViewController.getProductWithQuantity().put(product,currentQuantity-1);
                }

            });

            increaseButton.setOnAction(event -> {
                int currentQuantity = Integer.parseInt(quantityField.getText());
                quantityField.setText(String.valueOf(currentQuantity + 1));
                placeOrderViewController.getProductWithQuantity().put(product,currentQuantity+1);
            });
        }

        public Button getDecreaseButton() {
            return decreaseButton;
        }

        public Button getIncreaseButton() {
            return increaseButton;
        }

        public TextField getQuantityField() {
            return quantityField;
        }

        public HBox getLayout() {
            HBox buttonsAndQuantity = new HBox(decreaseButton, quantityField, increaseButton);
            buttonsAndQuantity.setSpacing(5);
            return buttonsAndQuantity;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Customer> customers = CustomerBLL.findAllCustomers();
        for (Customer customer : customers) {
            comboBox.getItems().add(customer.getName());
        }

        tableView.getColumns().clear();
        tableView.getItems().clear();

        ControllerUtillity<Product> controllerUtillity = new ControllerUtillity<>();
        controllerUtillity.setTableHeaders(Product.class, tableView);

        TableColumn<Product, Void> operationsColumn = new TableColumn<>("Operations");
        operationsColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new ProductRow(getTableView().getItems().get(getIndex()), PlaceOrderViewController.this).getLayout());
                }
            }
        });

        tableView.getColumns().add(operationsColumn);

        List<Product> products = ProductBLL.findAllProducts();
        tableView.getItems().addAll(products);
    }


    //    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//        List<Customer> customers = CustomerBLL.findAllCustomers();
//        for (Customer customer : customers) {
//            comboBox.getItems().add(customer.getName());
//        }
//
//
//        tableView.getColumns().clear();
//        tableView.getItems().clear();
//
//
//        ControllerUtillity<Product> controllerUtillity = new ControllerUtillity<>();
//        controllerUtillity.setTableHeaders(Product.class, tableView);
//
//        TableColumn<Product, Void> operationsColumn = new TableColumn<>("Operations");
//        operationsColumn.setCellFactory(param -> new TableCell<>() {
//            private final ProductRow productRow = new ProductRow();
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(productRow.getLayout());
//                }
//            }
//        });
//
//        tableView.getColumns().add(operationsColumn);
//
//
//        List<Product> products = ProductBLL.findAllProducts();
//        tableView.getItems().addAll(products);
//    }
    @FXML
    void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/orders_management/database-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DatabaseViewController databaseViewController = fxmlLoader.getController();
        databaseViewController.onOrderButtonClick();
        stage.setTitle("Database");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void onPlaceOrderButtonClick() {

        String selectedCustomerName = comboBox.getValue();
        if (selectedCustomerName == null) {
            AlertUtility.openInfoWarning("No Customer Selected", "Please select a Customer.");
            return;
        }

        Customer selectedCustomer = null;
        List<Customer> customers = CustomerBLL.findAllCustomers();
        for (Customer customer : customers) {
            if (customer.getName().equals(selectedCustomerName)) {
                selectedCustomer = customer;
                break;
            }
        }

        if (selectedCustomer == null) {
            AlertUtility.openInfoWarning("Customer Not Found", "Selected customer not found.");
            return;
        }


        boolean create=false;
        boolean notEnoughQuantity=false;
        Product notEnoughQuantityProduct=null;

        for (Product product : productWithQuantity.keySet()) {
            int quantity = productWithQuantity.get(product);
            if (quantity > 0) {
                create=true;
            }
            if(quantity>product.getStock_quantity())
            {
                notEnoughQuantity=true;
                notEnoughQuantityProduct=product;
                break;
            }
        }


        if (!create) {
            AlertUtility.openInfoWarning("No Products Selected", "Please select at least one product.");
            return;
        }

        if(notEnoughQuantity)
        {
            AlertUtility.openAlertWarning("Insufficient Stock Quantity","There are not enough "+notEnoughQuantityProduct.getName()+" products in stock. The actual number is: "+notEnoughQuantityProduct.getStock_quantity());
            return;
        }


        Orders order = new Orders();
        order.setCustomer_id(selectedCustomer.getId());
        order.setOrder_date(LocalDateTime.now());
        order.setAddress(selectedCustomer.getAddress());
        order.setStatus("Confirmed");

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : productWithQuantity.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(subtotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct_id(product.getId());
            orderItem.setQuantity(quantity);
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);
        }

        order.setTotal_price(totalPrice);

        try {
            Orders newOrder = OrdersBLL.insertOrder(order);
            int orderId=newOrder.getId();
            for (OrderItem orderItem : orderItems) {
                orderItem.setOrder_id(orderId);
                OrderItemBLL.insertOrderItem(orderItem);
            }
            for (Map.Entry<Product, Integer> entry : productWithQuantity.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                product.setStock_quantity(product.getStock_quantity()-quantity);
                ProductBLL.updateProduct(product);
            }

            AlertUtility.openInfoWarning("Order Placed", "The order has been successfully placed.");
            onCancelButtonClick();
        } catch (Exception e) {
            AlertUtility.openAlertError("Error", "An error occurred while placing the order: " + e.getMessage());
        }
    }

}
