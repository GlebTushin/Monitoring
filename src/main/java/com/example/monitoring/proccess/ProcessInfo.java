package com.example.monitoring.proccess;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProcessInfo {
    private SimpleIntegerProperty processID;
    private SimpleStringProperty processName;
    private SimpleDoubleProperty processCpu;
    public ProcessInfo(int pId, String pName, double pCpu){
        processID= new SimpleIntegerProperty(pId);
        processName=new SimpleStringProperty(pName);
        processCpu= new SimpleDoubleProperty(pCpu);
    }

    public int getProcessID() {
        return processID.get();
    }

    public void setProcessID(int pID) {
         processID.set(pID);
    }

    public String getProcessName() {

        return processName.get();
    }

    public void setProcessName(String name) {

        processName.set(name);
    }

    public double getProcessCpu() {
        return processCpu.get();
    }

    public void setProcessCpu(double cpu) {
        this.processCpu.set(cpu); ;
    }
}

