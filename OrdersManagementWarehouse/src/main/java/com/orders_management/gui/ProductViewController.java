package com.orders_management.gui;
import com.orders_management.logic.ProductBLL;
import com.orders_management.models.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
public class ProductViewController {

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField stockQuantityTextField;

    private Product product = null;

    @FXML
    void onApplyButton() {
        if (!ControllerUtillity.areNotEmpty(nameTextField, stockQuantityTextField, priceTextField)) {
            AlertUtility.openAlertWarning("Empty Fields", "Please fill in all available fields.");
        } else {

            try {
                Product newProduct = new Product((product == null ? 0 : product.getId()), nameTextField.getText(), new BigDecimal(priceTextField.getText()), Integer.parseInt(stockQuantityTextField.getText()));

                if (product == null) {
                    ProductBLL.insertProduct(newProduct);
                } else {
                    ProductBLL.updateProduct(newProduct);
                }
                Stage stage = (Stage) applyButton.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/orders_management/database-view.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DatabaseViewController databaseViewController = fxmlLoader.getController();
                databaseViewController.onProductButtonClick();
                stage.setTitle("Database");
                stage.setScene(scene);
                stage.show();
            } catch (NumberFormatException e) {
                AlertUtility.openAlertError("Invalid Field", "Price or Stock Quantity is not a valid number.");
            } catch (IllegalArgumentException e) {
                AlertUtility.openAlertError("Invalid Field", e.getMessage());
            }

        }
    }

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
        databaseViewController.onProductButtonClick();
        stage.setTitle("Database");
        stage.setScene(scene);
        stage.show();

    }

    public void setData(Product item) {
        this.product = item;
        stockQuantityTextField.setText(String.valueOf(item.getStock_quantity()));
        nameTextField.setText(item.getName());
        priceTextField.setText(String.valueOf(item.getPrice()));
    }


}
