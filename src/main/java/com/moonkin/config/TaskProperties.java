package com.moonkin.config;

/**
 * Created by Administrator on 2018/11/2 0002.
 */
public class TaskProperties {
    private  String taskName;
    public  String GetTaskName(){return  taskName;}
    public  void SetTaskName(String value){taskName = value;}

    private  String PathIn;
    public  String GetPathIn(){return  PathIn;}
    public  void SetPathIn(String value){PathIn = value;}

    private  String PathOut;
    public  String GetPathOut(){return  PathOut;}
    public  void SetPathOut(String value){PathOut = value;}

    private  int RadioIndex;
    public  int GetRadioIndex(){return  RadioIndex;}
    public  void SetRadioIndex(int value){RadioIndex = value;}

    private  String DataKeepTime;
    public  String GetDataKeepTime(){return  DataKeepTime;}
    public  void SetDataKeepTime(String value){DataKeepTime = value;}

    public TaskProperties() {
        PathIn = "";
        PathOut = "";
        RadioIndex = 1;
        DataKeepTime ="1";
    }
}
