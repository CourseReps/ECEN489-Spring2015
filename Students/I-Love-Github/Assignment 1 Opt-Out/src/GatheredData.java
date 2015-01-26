class GatheredData {
    private long time;
    private double longitude;
    private double latitude;

    GatheredData() {
        time = 0;
        longitude = 0;
        latitude = 0;
    }

    GatheredData(double latitude, double longitude) {
        this.time = 0;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}