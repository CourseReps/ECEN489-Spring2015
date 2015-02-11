package com.example.tungala.challenge4android;

import java.io.Serializable;

/**
 * Created by tungala on 2/11/2015.
 */
public class ClientInfo implements Serializable {
    private String location;

    private String wifiBssid;
    private int wifiRssid;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getWifiBssid() {
        return wifiBssid;
    }

    public void setWifiBssid(String wifiBssid) {
        this.wifiBssid = wifiBssid;
    }

    public int getWifiRssid() {
        return wifiRssid;
    }

    public void setWifiRssid(int wifiRssid) {
        this.wifiRssid = wifiRssid;
    }
}
