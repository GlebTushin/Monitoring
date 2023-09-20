package com.example.monitoring;

import com.example.monitoring.memory.MemoryInfo;
import com.example.monitoring.ports.PortScan;
import com.example.monitoring.proccess.ProcessList;
import com.example.monitoring.proccess.ProcessInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    private ObservableList<ProcessInfo> procData = FXCollections.observableArrayList();
    @FXML
    private TextField virtTextField;
    @FXML
    private TextField memTextField;
    @FXML
    TableView<ProcessInfo> processInfoTableView;
    @FXML
    TableColumn<ProcessInfo, Integer> processID;
    @FXML
    TableColumn<ProcessInfo, String> processName;
    @FXML
    TableColumn<ProcessInfo, Double> processCPU;
    @FXML
    LineChart<Integer, Double> virtualMemChart;
    @FXML
    LineChart<Integer, Double> ramChart;
    @FXML
    NumberAxis virtualX;
    @FXML
    NumberAxis virtualY;
    @FXML
    NumberAxis ramX;
    @FXML
    NumberAxis ramY;
    @FXML
    ListView<String> ports;
    @FXML
    Tab memTab;
    MemoryInfo memoryInfo= new MemoryInfo();
    Thread memorythread = new Thread(memoryInfo);

    public void getProcessList() {
        ProcessList processList = new ProcessList();
        procData = processList.getProcList();

    }

    @FXML
    public void scanbtnClick() throws IOException {
        PortScan portScan = new PortScan();
        ports.setItems(portScan.runPortScan("127.0.0.1", 65535));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProcessPage();
    }

    public void setMemTab() {
        initMemoryPage();
    }

    public void initProcessPage() {
        processID.setCellValueFactory(new PropertyValueFactory<>("processID"));
        processName.setCellValueFactory(new PropertyValueFactory<>("processName"));
        processCPU.setCellValueFactory(new PropertyValueFactory<>("processCpu"));
        getProcessList();
        processInfoTableView.setItems(procData);
    }

    public void initMemoryPage() {
        ObservableList<XYChart.Data> virtdata = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> ramdata = FXCollections.observableArrayList();
        ObservableList<Double> ramlist = FXCollections.observableArrayList();
        ObservableList<Double> virtlist = FXCollections.observableArrayList();
        XYChart.Series ramseries = new XYChart.Series();
        XYChart.Series virtseries = new XYChart.Series();
        memorythread.run();
        ramX = new NumberAxis(0, 20, 1);
        ramY = new NumberAxis(0, memoryInfo.getTotalMemory(), 0.1);
        virtualX = new NumberAxis(0, 20, 1);
        virtualY = new NumberAxis(0, memoryInfo.getMaxVirtual(), 0.1);
        ramChart.getData().removeAll();
        virtualMemChart.getData().removeAll();
        ramlist = memoryInfo.getRamlist();
        virtlist = memoryInfo.getVirtualmemlist();
        ramdata.removeAll();
        virtlist.removeAll();
        for (int i = 0; i < ramlist.size(); i++) {
            ramdata.add(new XYChart.Data(i, ramlist.get(i)));
            virtdata.add(new XYChart.Data(i, virtlist.get(i)));
        }
        ramseries.setData(ramdata);
        virtseries.setData(virtdata);
        ramChart.getData().add(ramseries);
        virtualMemChart.getData().add(virtseries);

        virtTextField.setText(Double.toString(memoryInfo.getUsedVirtual()));
        memTextField.setText(Double.toString(memoryInfo.getMemoryInUse()));

    }


}