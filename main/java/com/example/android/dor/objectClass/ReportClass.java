package com.example.android.dor.objectClass;

/**
 * Created by darip on 12-06-2017.
 */

public class ReportClass {
    private int reportId;
    private String reportName;
    private String reportDate;
    private String reportType;
    private String reportPath;
    private String reportUrl;

    public ReportClass(){
        setReportId(0);
        setName("");
        setDate("");
        setPath("");
    }

    public int getId(){
        return this.reportId;
    }
    public void setReportId(int r){
        this.reportId = r;
    }
    public String getName(){
        return this.reportName;
    }
    public void setName(String s){
        this.reportName = s;
    }
    public String getDate(){
        return this.reportDate;
    }
    public void setDate(String r){
        this.reportDate = r;
    }
    public String getPath(){
        return this.reportPath;
    }
    public void setPath(String r){
        this.reportPath = r;
    }
    public String getReportType(){
        return this.reportType;
    }
    public void setReportType(String r){
        this.reportType = r;
    }
    public String getReportUrl(){
        return this.reportUrl;
    }
    public void setReportUrl(String s){
        this.reportUrl = s;
    }

}
