package com.orders_management.gui;

import com.orders_management.logic.BillBLL;
import com.orders_management.logic.ProductBLL;
import com.orders_management.logic.CustomerBLL;
import com.orders_management.models.Bill;
import com.orders_management.models.Customer;
import com.orders_management.models.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class DatabaseViewController {

        @FXML
        private Button addRowButton;

        @FXML
        private Button customerButton;

        @FXML
        private Label label;

        @FXML
        private Button orderButton;

        @FXML
        private Button placeOrderButton;

        @FXML
        private Button productButton;


        @FXML
        private TableView<Bill> tableOrderView;

        @FXML
        private TableView<Customer> tableCustomerView;
        @FXML
        private TableView<Product> tableProductView;

        @FXML
        void onAddRowButtonClick() {
                if(tableCustomerView.isVisible())
                {
                        Stage stage = (Stage) customerButton.getScene().getWindow();
                        stage.close();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/orders_management/customer-view.fxml"));
                        Scene scene;
                        try {
                                scene = new Scene(fxmlLoader.load());
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                        }
                        stage.setTitle("Customer");
                        stage.setScene(scene);
                        stage.show();
                }
                else {
                        Stage stage = (Stage) customerButton.getScene().getWindow();
                        stage.close();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/orders_management/product-view.fxml"));
                        Scene scene;
                        try {
                                scene = new Scene(fxmlLoader.load());
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                        }
                        stage.setTitle("Product");
                        stage.setScene(scene);
                        stage.show();

                }

        }


        @FXML
        void onCustomerButtonClick() {
                tableCustomerView.setVisible(true);
                tableProductView.setVisible(false);
                tableOrderView.setVisible(false);
                label.setVisible(false);
                addRowButton.setVisible(true);

                tableCustomerView.getColumns().clear();
                tableCustomerView.getItems().clear();


                ControllerUtillity<Customer> controllerUtillity = new ControllerUtillity<>();
                controllerUtillity.setTableHeaders(Customer.class, tableCustomerView);

                TableColumn<Customer, Void> operationsColumn = new TableColumn<>("Operations");
                operationsColumn.setCellFactory(param -> new TableCell<>() {
                        final Button editButton = new Button("Edit");
                        final Button deleteButton = new Button("Delete");

                        {
                                editButton.setOnAction(event -> {
                                        Customer item = getTableView().getItems().get(getIndex());
                                        EditAction(item);
                                });

                                deleteButton.setOnAction(event -> {
                                        Customer item = getTableView().getItems().get(getIndex());
                                        DeleteAction(item);
                                });
                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                        setGraphic(null);
                                } else {
                                        HBox buttons = new HBox(editButton, deleteButton);
                                        buttons.setSpacing(5);
                                        setGraphic(buttons);
                                }
                        }
                });


                tableCustomerView.getColumns().add(operationsColumn);

                List<Customer> customers = CustomerBLL.findAllCustomers();
                tableCustomerView.getItems().addAll(customers);
        }

        private void EditAction(Customer item) {
                Stage stage = (Stage) customerButton.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/orders_management/customer-view.fxml"));
                Scene scene;
                try {
                        scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
                CustomerViewController customerViewController = fxmlLoader.getController();
                customerViewController.setData(item);
                stage.setTitle("Customer");
                stage.setScene(scene);
                stage.show();

        }

        private void DeleteAction(Customer item) {
                boolean result = CustomerBLL.deleteCustomer(item);
                if(result==false)
                {
                        AlertUtility.openAlertError("Failure","Cannot delete the row.");
                }
                else {
                        AlertUtility.openInfoWarning("Success", "The row was successfully deleted.");
                }
                this.onCustomerButtonClick();
        }



        @FXML
        void onOrderButtonClick() {
                tableOrderView.setVisible(true);
                tableProductView.setVisible(false);
                tableCustomerView.setVisible(false);
                label.setVisible(false);
                addRowButton.setVisible(false);

                tableOrderView.getColumns().clear();
                tableOrderView.getItems().clear();


                ControllerUtillity<Bill> controllerUtillity = new ControllerUtillity<>();
                controllerUtillity.setBillTableHeaders(Bill.class, tableOrderView);


                List<Bill> orders = BillBLL.getAllBills();
                tableOrderView.getItems().addAll(orders);

        }

        @FXML
        void onPlaceOrderButtonClick() {
                Stage stage = (Stage) placeOrderButton.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/orders_management/place-order-view.fxml"));
                Scene scene;
                try {
                        scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
                stage.setTitle("Place an Order");
                stage.setScene(scene);
                stage.show();

        }

        @FXML
        void onProductButtonClick() {
                tableCustomerView.setVisible(false);
                tableProductView.setVisible(true);
                tableOrderView.setVisible(false);
                label.setVisible(false);
                addRowButton.setVisible(true);

                tableProductView.getColumns().clear();
                tableProductView.getItems().clear();


                ControllerUtillity<Product> controllerUtillity = new ControllerUtillity<>();
                controllerUtillity.setTableHeaders(Product.class, tableProductView);

                TableColumn<Product, Void> operationsColumn = new TableColumn<>("Operations");
                operationsColumn.setCellFactory(param -> new TableCell<>() {
                        final Button editButton = new Button("Edit");
                        final Button deleteButton = new Button("Delete");

                        {
                                editButton.setOnAction(event -> {
                                        Product item = getTableView().getItems().get(getIndex());
                                        EditAction(item);
                                });

                                deleteButton.setOnAction(event -> {
                                        Product item = getTableView().getItems().get(getIndex());
                                        DeleteAction(item);
                                });
                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                        setGraphic(null);
                                } else {
                                        HBox buttons = new HBox(editButton, deleteButton);
                                        buttons.setSpacing(5);
                                        setGraphic(buttons);
                                }
                        }
                });


                tableProductView.getColumns().add(operationsColumn);

                List<Product> products = ProductBLL.findAllProducts();
                tableProductView.getItems().addAll(products);

        }

        private void EditAction(Product item) {
                Stage stage = (Stage) customerButton.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/orders_management/product-view.fxml"));
                Scene scene;
                try {
                        scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
                ProductViewController productViewController = fxmlLoader.getController();
                productViewController.setData(item);
                stage.setTitle("Product");
                stage.setScene(scene);
                stage.show();

        }

        private void DeleteAction(Product item) {
                boolean result = ProductBLL.deleteProduct(item);
                if(result==false)
                {
                        AlertUtility.openAlertError("Failure","Cannot delete the row.");
                }
                else {
                        AlertUtility.openInfoWarning("Success", "The row was successfully deleted.");
                }
                this.onProductButtonClick();
        }



}