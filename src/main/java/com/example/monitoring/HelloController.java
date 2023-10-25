package com.example.monitoring;

import com.example.monitoring.memory.MemoryInfo;
import com.example.monitoring.ports.PortScan;
import com.example.monitoring.proccess.ProcessList;
import com.example.monitoring.proccess.ProcessInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class HelloController implements Initializable {
    private static Date startDate=new Date();
    private static double avgvirtmemused;
    private static double avgramused;
    private static Set<String> usedProcesses=new HashSet<>();
    private ObservableList<ProcessInfo> procData = FXCollections.observableArrayList();
    @FXML
    private TextField virtTextField;
    @FXML
    private TextField memTextField;

    @FXML
    private TableView<ProcessInfo> processInfoTableView;
    @FXML
    TableColumn<ProcessInfo, Integer> processID;
    @FXML
    TableColumn<ProcessInfo, String> processName;
    @FXML
    TableColumn<ProcessInfo, Double> processCPU;
    @FXML
    LineChart<Number, Number> virtualMemChart;
    @FXML
    LineChart<Number, Number> ramChart;
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
    @FXML
    Tab procTab;
    @FXML
    Tab fireTab;
    @FXML
    TextArea firewallArea;
    MemoryInfo memoryInfo = new MemoryInfo();
    ObservableList<XYChart.Data> virtdata = FXCollections.observableArrayList();
    ObservableList<XYChart.Data> ramdata = FXCollections.observableArrayList();
    ObservableList<Double> ramlist = FXCollections.observableArrayList();
    ObservableList<Double> virtlist = FXCollections.observableArrayList();
    XYChart.Series ramseries = new XYChart.Series();
    XYChart.Series virtseries = new XYChart.Series();
    TimerTask memoryTask;
    TimerTask processTask;
    TimerTask firewallTask;


    public void initProcessTask() {
       processTask  = new TimerTask() {
            @Override
            public void run() {
                ProcessList processList = new ProcessList();
                procData = processList.getProcList();
                for (ProcessInfo p: procData
                     ) {usedProcesses.add(p.getProcessName());

                }
                if (procTab.isSelected()){
                processInfoTableView.setItems(procData);}
            }
        };

    }


    @FXML
    public void scanbtnClick() throws IOException {
        PortScan portScan = new PortScan();
        ports.setItems(portScan.runPortScan("127.0.0.1", 65535));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProcessPage();
        initMemoryPage();
        initMemoryTask();
        initProcessTask();
        checkFirewallStatus();
        Timer timer =new Timer();
        timer.scheduleAtFixedRate(processTask,0,1000);
        timer.scheduleAtFixedRate(memoryTask,0,1000);
        timer.scheduleAtFixedRate(firewallTask,0,1000);
    }



    public void initProcessPage() {
        processID.setCellValueFactory(new PropertyValueFactory<>("processID"));
        processName.setCellValueFactory(new PropertyValueFactory<>("processName"));
        processCPU.setCellValueFactory(new PropertyValueFactory<>("processCpu"));



    }

    public void initMemoryPage() {
        ramX = new NumberAxis(0, 20, 1);
        ramY = new NumberAxis(0, memoryInfo.getTotalMemory(), 0.1);
        virtualX = new NumberAxis(0, 20, 1);
        virtualY = new NumberAxis(0, memoryInfo.getMaxVirtual(), 0.1);
        ramChart.getData().add(ramseries);
        virtualMemChart.getData().add(virtseries);
        avgramused=memoryInfo.getMemoryInUse();
        avgvirtmemused=memoryInfo.getUsedVirtual();
        

    }


    public void initMemoryTask() {
        memoryTask = new TimerTask() {
            @Override
            public void run(){
                    memoryInfo.updateMemoryInfo();
                    ramlist = memoryInfo.getRamlist();
                    virtlist = memoryInfo.getVirtualmemlist();
                    ramdata.removeAll();
                    virtlist.removeAll();
                    if(memTab.isSelected()){
                        for (int i = 0; i < ramlist.size(); i++) {
                        ramdata.add(new XYChart.Data(i, ramlist.get(i)));
                        virtdata.add(new XYChart.Data(i, virtlist.get(i)));
                    }
                        ramseries.setData(ramdata);
                        virtseries.setData(virtdata);
                        virtTextField.setText(Double.toString(memoryInfo.getUsedVirtual()));
                        memTextField.setText(Double.toString(memoryInfo.getMemoryInUse()));
                    }
                avgramused=(avgramused+memoryInfo.getMemoryInUse())/2;
                avgvirtmemused=(avgvirtmemused+memoryInfo.getUsedVirtual())/2;
                }

        };
    }
    public void checkFirewallStatus(){
        firewallTask = new TimerTask()  {
            @Override
            public void run() {
                StringBuilder output = new StringBuilder();
                Process p = null;
                try {
                    p = Runtime.getRuntime().exec("netsh advfirewall show allprofiles state");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    p.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "CP866"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                String line = "";
                if (fireTab.isSelected()){
                    firewallArea.clear();
                    while (true) {
                        try {
                            if (!((line = reader.readLine()) != null)) break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        firewallArea.appendText(line+"\n");
                    }
                }

            }

        };
    }
    public static void  exit() throws IOException {
        FileWriter writer=new FileWriter("src//main//resources//com/example//monitoring//Results.txt");
        writer.write("\r\n");
        writer.write("\r\n");
        writer.write(String.valueOf(startDate)+"\r\n");
        writer.write("Время работы программы: "+ (startDate.getTime()/1000000000/100)+" секунд"+"\r\n");
        writer.write("Используемая память: "+ avgramused+"\r\n");
        writer.write("Используемая виртуальная память : "+ avgvirtmemused+"\r\n");
        writer.write("Используемые процессы: "+"\r\n");
        for (String s:usedProcesses
             ) {
            writer.write(s+"\r\n");

        }

    }

}