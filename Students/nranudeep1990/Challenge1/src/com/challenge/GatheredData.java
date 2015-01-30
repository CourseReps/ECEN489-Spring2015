package com.challenge;

public class GatheredData {

    private long time;
    private double longitude;
    private double latitude;

    public GatheredData(long time, double longitude, double latitude) {
        this.setTime(time);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double haversine(GatheredData gd1,GatheredData gd2) {
        double R = 6372.8;
        double dLat = Math.toRadians(gd2.getLatitude() - gd1.getLatitude());
        double dLon = Math.toRadians(gd2.getLongitude() - gd1.longitude);
        gd1.setLatitude(Math.toRadians(gd1.getLatitude()));
        gd2.setLatitude(Math.toRadians(gd2.getLatitude()));

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(gd1.getLatitude()) * Math.cos(gd2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    public double averagevelocity(double haversin,long time1,long time2) {

        return haversin/(time2-time1);
    }


}

