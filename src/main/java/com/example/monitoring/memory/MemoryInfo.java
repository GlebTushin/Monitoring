package com.example.monitoring.memory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.util.FormatUtil;

public class MemoryInfo implements Runnable {
    ObservableList<Double> virtualmemlist = FXCollections.observableArrayList();
    ObservableList<Double> ramlist = FXCollections.observableArrayList();
    private double memoryInUse;
    private double totalMemory;
    private double maxVirtual;
    private double usedVirtual;

    public double getUsedVirtual() {

        return usedVirtual;
    }


    public double getMemoryInUse() {

        return memoryInUse;
    }

    public ObservableList<Double> getVirtualmemlist() {

        return virtualmemlist;
    }


    public ObservableList<Double> getRamlist() {

        return ramlist;
    }


    public double getTotalMemory() {

        return totalMemory;
    }


    public double getMaxVirtual() {

        return maxVirtual;
    }


    public void updateMemoryInfo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory globalMemory = hardware.getMemory();
        VirtualMemory virtualMemory = globalMemory.getVirtualMemory();
        long usedMemory = globalMemory.getTotal() - globalMemory.getAvailable();
        totalMemory = globalMemory.getTotal();
        memoryInUse = usedMemory;
        usedVirtual = virtualMemory.getVirtualInUse();
        maxVirtual = virtualMemory.getVirtualMax();
        if (ramlist.size() == 20) {
            ramlist.remove(0);
        }
        ramlist.add(getMemoryInUse());
        if (virtualmemlist.size() == 20) {
            virtualmemlist.remove(0);}
        virtualmemlist.add(getUsedVirtual());

    }
    @Override
    public void run() {
        updateMemoryInfo();


    }


    public void setVirtualmemlist(ObservableList<Double> virtualmemlist) {
        this.virtualmemlist = virtualmemlist;
    }

    public void setRamlist(ObservableList<Double> ramlist) {
        this.ramlist = ramlist;
    }
}
