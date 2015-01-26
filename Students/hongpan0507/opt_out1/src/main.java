/**
 * Created by hpan on 1/25/15.
 */
import java.lang.Math.*;
import java.util.Scanner;

public class main {
    public static void main(String args[]){
        GatheredData location1 = new GatheredData();
        GatheredData location2 = new GatheredData();

        Scanner input = new Scanner(System.in);
        String temp1;

        System.out.print("Enter location 1 time: ");
        temp1 = input.nextLine();
        location1.set_time(Long.parseLong(temp1));
        System.out.print("Enter location 1 longitude: ");
        temp1 = input.nextLine();
        location1.set_longitude(Long.parseLong(temp1));
        System.out.print("Enter location 1 latitude: ");
        temp1 = input.nextLine();
        location1.set_latitude(Long.parseLong(temp1));
        System.out.print("Enter location 2 time: ");
        temp1 = input.nextLine();
        location2.set_time(Long.parseLong(temp1));
        System.out.print("Enter location 2 longitude: ");
        temp1 = input.nextLine();
        location2.set_longitude(Long.parseLong(temp1));
        System.out.print("Enter location 2 latitude: ");
        temp1 = input.nextLine();
        location2.set_latitude(Long.parseLong(temp1));

        double distance = dist(location1.get_longtidue(), location1.get_latitude(),
                               location2.get_longtidue(), location2.get_latitude());
        double time_diff = location2.get_time() - location1.get_time();
        double velocity = distance / time_diff;

        System.out.format("velocity = %.2f", velocity);
        System.out.println(" km/hour");
    }

    public static double dist(double long1, double lat1, double long2, double lat2){
        double R = 6371;    //earth radius in km
        double long_diff = Math.toRadians(long2 - long1);
        double lat_diff =  Math.toRadians(lat2 - lat1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double temp1 = Math.sin(lat_diff/2)*Math.sin(lat_diff/2) +
                       Math.sin(long_diff/2)*Math.sin(long_diff/2) *
                       Math.cos(lat1)*Math.cos(lat2);

        return 2*R*Math.asin(Math.sqrt(temp1));
    }
}
