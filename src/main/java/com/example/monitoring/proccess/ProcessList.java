package com.example.monitoring.proccess;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.win32.W32APIOptions;
import oshi.SystemInfo;
import oshi.software.os.*;
import java.util.ArrayList;

public class ProcessList {

    private String th32ProcessID;

    private String th32ParentProcessID;

    private String szExeFile;
    private String cntUsage;
    private ArrayList<String> procData = new ArrayList<String>();

    public ArrayList<String> getProcList() {
        ArrayList<String> procs=new ArrayList<String>();
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        ArrayList<OSProcess> processes= new ArrayList<>(operatingSystem.getProcesses());
        for (OSProcess proc:processes
             ) {
           String th32ProcessID=Integer.toString( proc.getProcessID());
           String szExeFile= proc.getName();
           String CntUsage=Double.toString(proc.getProcessCpuLoadCumulative());
           procs.add(new String( th32ProcessID +"," + szExeFile+ ","  +CntUsage +","));
        }
        return procs;
    }
    public void getProcessList(){
        procData=getProcList();
        for (String s:procData
             ) {System.out.println(s);

        }
    }
}
