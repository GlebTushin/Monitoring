package com.example.monitoring;

import com.example.monitoring.proccess.ProcessList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onButtonClick()  {
        ProcessList processList =new ProcessList();
        processList.getProcessList();

    }
}