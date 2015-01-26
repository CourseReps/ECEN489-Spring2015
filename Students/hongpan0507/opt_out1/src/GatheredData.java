/**
 * Created by hpan on 1/25/15.
 */
public class GatheredData {
    private long time;
    private double longitude;
    private double latitude;

    //constructor
    public GatheredData(){

    }

    void set_time(long data) {
        time = data;
    }

    long get_time(){
        return time;
    }

    void set_longitude(double data){
        longitude = data;
    }

    double get_longtidue(){
        return longitude;
    }

    void set_latitude(double data){
        latitude = data;
    }

    double get_latitude(){
        return latitude;
    }
}
