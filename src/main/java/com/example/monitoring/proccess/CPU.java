package com.example.monitoring.proccess;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.util.FormatUtil;

public class CPU {

    public static  void GetMemoryInfo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory globalMemory = hardware.getMemory();
        VirtualMemory virtualMemory = globalMemory.getVirtualMemory();
        long usedMemory = globalMemory.getTotal() - globalMemory.getAvailable();

        System.out.println("Total memory: " + FormatUtil.formatBytes(globalMemory.getTotal()));
        System.out.println("Available memory: " + FormatUtil.formatBytes(globalMemory.getAvailable()));
        System.out.println("Used memory: " + FormatUtil.formatBytes(usedMemory));
        System.out.println("Max virtual memory: " + FormatUtil.formatBytes(virtualMemory.getVirtualMax()));
        System.out.println("Virtual memory used: " + FormatUtil.formatBytes(virtualMemory.getVirtualInUse()));
    }
}
