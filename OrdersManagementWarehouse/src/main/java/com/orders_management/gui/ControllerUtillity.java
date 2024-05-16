package com.orders_management.gui;

import com.orders_management.models.Bill;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

public class ControllerUtillity<T> {


        public static boolean areNotEmpty(TextField... textFields) {
            for (TextField textField : textFields) {
                if (textField.getText().isEmpty()) {
                    return false;
                }
            }
            return true;
        }


    public <T> void setTableHeaders(Class<T> clazz, TableView<T> tableView) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equalsIgnoreCase("id")) {
                TableColumn<T, String> column = new TableColumn<>(fieldName);
                try {
                    field.setAccessible(true);
                    column.setCellValueFactory(cellData -> {
                        try {
                            String value = String.valueOf(field.get(cellData.getValue()));
                            return new SimpleStringProperty(value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return new SimpleStringProperty("");
                        }
                    });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                tableView.getColumns().add(column);
            }
        }
    }

    public <T> void setBillTableHeaders(Class<T> clazz, TableView<T> tableView) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equalsIgnoreCase("products")) {
                TableColumn<T, String> column = new TableColumn<>(fieldName);
                try {
                    field.setAccessible(true);
                    column.setCellValueFactory(cellData -> {
                        try {
                            String value = String.valueOf(field.get(cellData.getValue()));
                            return new SimpleStringProperty(value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return new SimpleStringProperty("");
                        }
                    });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                tableView.getColumns().add(column);
            }
        }

        TableColumn<T, String> productsColumn = new TableColumn<>("Products");
        productsColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Bill bill = (Bill) getTableRow().getItem();
                    if (bill != null) {
                        TableView<Pair<String, Pair<Integer, BigDecimal>>> nestedTable = createNestedTable(bill.products());
                        setGraphic(nestedTable);
                    }
                }
            }
        });
        tableView.getColumns().add(productsColumn);
    }

    private TableView<Pair<String, Pair<Integer, BigDecimal>>> createNestedTable(List<Pair<String, Pair<Integer, BigDecimal>>> products) {
        TableView<Pair<String, Pair<Integer, BigDecimal>>> nestedTable = new TableView<>();

        TableColumn<Pair<String, Pair<Integer, BigDecimal>>, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));

        TableColumn<Pair<String, Pair<Integer, BigDecimal>>, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getKey())));

        TableColumn<Pair<String, Pair<Integer, BigDecimal>>, String> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getValue())));

        nestedTable.getColumns().addAll(productNameColumn, quantityColumn, subtotalColumn);

        // Clear existing items before adding new ones
        nestedTable.getItems().clear();

        // Add only as many rows as there are products
        for (Pair<String, Pair<Integer, BigDecimal>> product : products) {
            nestedTable.getItems().add(product);
        }

        return nestedTable;
    }





}
