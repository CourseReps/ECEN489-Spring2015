package com.example.tungala.challenge5android;

import java.io.Serializable;

/**
 * Created by tungala on 2/5/2015.
 */
public class ClientInfo implements Serializable {
    private String location;
    private String username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
