package com.google.android.gms.plus.sample.quickstart;

/**
 * Created by mandel on 4/15/15.
 */
public class CheckIn {
    private String userName;
    private String locationName;
    private int timestamp;
    private String method;
    
    
    public CheckIn(String userName, String locationName, int timestamp, String method){
        this.userName = userName;
        this.locationName = locationName;
        this.timestamp = timestamp;
        this.method = method;
    }

    public String getUserName() {
        return userName;
    }

    public String getLocationName() {
        return locationName;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getMethod() {
        return method;
    }
}
