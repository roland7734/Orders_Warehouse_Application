package com.orders_management.gui;

import com.orders_management.logic.CustomerBLL;
import com.orders_management.models.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AmbientLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerViewController {

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;
    private Customer customer=null;

    @FXML
    void onApplyButton() {

        if (!ControllerUtillity.areNotEmpty(nameTextField, addressTextField, ageTextField, emailTextField)) {
            AlertUtility.openAlertWarning("Empty Fields", "Please fill in all available fields.");
        } else {

            Customer newCustomer = new Customer((customer == null ? 0 : customer.getId()), nameTextField.getText(), addressTextField.getText(), emailTextField.getText(), Integer.parseInt(ageTextField.getText()));
            try {
                if (customer == null) {
                    CustomerBLL.insertCustomer(newCustomer);
                } else {
                    CustomerBLL.updateCustomer(newCustomer);
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
                databaseViewController.onCustomerButtonClick();
                stage.setTitle("Database");
                stage.setScene(scene);
                stage.show();
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
        databaseViewController.onCustomerButtonClick();
        stage.setTitle("Database");
        stage.setScene(scene);
        stage.show();

    }

    public void setData(Customer item) {
        this.customer=item;
        addressTextField.setText(item.getAddress());
        nameTextField.setText(item.getName());
        ageTextField.setText(String.valueOf(item.getAge()));
        emailTextField.setText(item.getEmail());
    }



}
