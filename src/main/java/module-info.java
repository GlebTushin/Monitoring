module com.example.monitoring {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires com.github.oshi;


    opens com.example.monitoring to javafx.fxml;
    opens com.example.monitoring.proccess to javafx.fxml;
    exports com.example.monitoring;
    exports com.example.monitoring.proccess;
    exports com.example.monitoring.memory;
    opens com.example.monitoring.memory to javafx.fxml;
}