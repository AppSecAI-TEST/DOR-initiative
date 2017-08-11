package com.example.android.dor.objectClass;

/**
 * Created by darip on 23-06-2017.
 */

public class NotificationClass {
    private int notiId;
    private String notiName;
    private String notiDetails;
    private String notiTimestamp;
    private String notiType;
    private String notiStatus;


    public int getNotiId(){
        return this.notiId;
    }
    public void setNotiId(int notiId){
        this.notiId = notiId;
    }

    public String getNotiName(){
        return this.notiName;
    }
    public void setNotiName(String notiName){
        this.notiName = notiName;
    }

    public String getNotiDetails(){
        return this.notiDetails;
    }
    public void setNotiDetails(String notiDetails){
        this.notiDetails = notiDetails;
    }

    public String getNotiTimestamp(){
        return this.notiTimestamp;
    }
    public void setNotiTimestamp(String notiTimestamp){
        this.notiTimestamp = notiTimestamp;
    }

    public String getNotiType(){
        return this.notiType;
    }
    public void setNotiType(String notiType){
        this.notiType = notiType;
    }

    public String getNotiStatus(){
        return this.notiStatus;
    }
    public void setNotiStatus(String notiStatus){
        this.notiStatus = notiStatus;
    }

}
