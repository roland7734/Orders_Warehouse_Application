module com.ordersmanagementwarehouse {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens com.orders_management to javafx.fxml;
    //exports com.orders_management;
    exports com.orders_management.gui;
    opens com.orders_management.gui to javafx.fxml;
}