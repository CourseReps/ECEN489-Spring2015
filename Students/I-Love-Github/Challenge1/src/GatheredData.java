// GATHERED DATA CLASS
// This class is requested by the assignment.  It stores latitude, longitude, and time information
class GatheredData {
    private long time;
    private double longitude;
    private double latitude;

    // CONSTRUCTOR: fields default to 0
    GatheredData() {
        time = 0;
        longitude = 0;
        latitude = 0;
    }

    // CONSTRUCTOR: Prepopulate latitude and longitude
    GatheredData(double latitude, double longitude) {
        this.time = 0;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // GETTERS AND SETTERS
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