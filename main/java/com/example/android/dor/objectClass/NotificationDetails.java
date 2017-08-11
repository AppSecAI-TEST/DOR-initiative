package com.example.android.dor.objectClass;

/**
 * Created by darip on 19-06-2017.
 */

public class NotificationDetails {
    private String title;
    private boolean isBackground;
    private String msg;
    private String nName;
    private String nid;
    private String nDetail;
    private String nType;
    private String nTimestamp;
    private String timeStamp;

    public NotificationDetails(){
        this.title = "";
        this.isBackground = false;
        this.msg = "";
        this.nName = "";
        this.nid = "";
        this.nDetail = "";
        this.nType = "";
        this.nTimestamp = "";
        this.timeStamp = "";
    }

    public NotificationDetails(String title, String msg, String notificationName, String nid, String nDetail, String nType, String nTimestamp, String timeStamp){
        setTitle(title);
//        setIsBackground(isBackground);
        setMsg(msg);
        setNotificationName(notificationName);
        setNid(nid);
        setnDetail(nDetail);
        setnType(nType);
        setnTimestamp(nTimestamp);
        setTimeStamp(timeStamp);
    }
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public boolean getIsBackground(){
        return this.isBackground;
    }
    public void setIsBackground(boolean isBackground){
        this.isBackground = isBackground;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getNid(){
        return this.nid;
    }
    public void setNid(String nid){
        this.nid = nid;
    }

    public String getNotificationName(){
        return this.nName;
    }
    public void setNotificationName(String notificationName){
        this.nName = notificationName;
    }

    public String getnDetail(){
        return this.nDetail;
    }
    public void setnDetail(String nDetail){
        this.nDetail = nDetail;
    }

    public String getnType(){
        return this.nType;
    }
    public void setnType(String nType){
        this.nType = nType;
    }

    public String getnTimestamp(){
        return this.nTimestamp;
    }
    public void setnTimestamp(String nTimestamp){
        this.nTimestamp = nTimestamp;
    }

    public String getTimeStamp(){
        return this.timeStamp;
    }
    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }
}
