module com.example.monitoring {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires com.github.oshi;


    opens com.example.monitoring to javafx.fxml;
    exports com.example.monitoring;
}