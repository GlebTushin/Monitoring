package com.example.monitoring.proccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import oshi.SystemInfo;
import oshi.software.os.*;
import java.util.ArrayList;

public class ProcessList {


    public ObservableList<ProcessInfo> getProcList() {
        ObservableList<ProcessInfo> procs = FXCollections.observableArrayList();
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        ArrayList<OSProcess> processes= new ArrayList<>(operatingSystem.getProcesses());
        for (OSProcess proc:processes
             ) {
            procs.add(new ProcessInfo(proc.getProcessID(),proc.getName(),proc.getProcessCpuLoadCumulative()));
        }
        return procs;
    }

}
