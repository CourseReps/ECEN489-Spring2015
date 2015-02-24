package com.company;

import java.util.Date;

/**
 * Created by NAGARAJ on 2/21/2015.
 */
public class TableSVR {
    private int id;
    private String macAddress;
    private Date date;
    private double latitude;
    private double longitude;

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
