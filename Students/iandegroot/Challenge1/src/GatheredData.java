
import java.util.Scanner;

public class GatheredData {
    long time;
    double longitude;
    double latitude;

    public static double haversine(GatheredData gd1, GatheredData gd2)
    {
        int radius = 6371; //Average radius of earth is 6371 km

        double deltaLat = Math.toRadians(gd2.latitude - gd1.latitude);
        double deltaLong = Math.toRadians(gd2.longitude - gd1.longitude);
        double radLat1 = Math.toRadians(gd1.latitude);
        double radLat2 = Math.toRadians(gd2.latitude);

        double a = Math.pow(Math.sin(deltaLat / 2), 2.0) + Math.pow(Math.sin(deltaLong / 2), 2.0) * Math.cos(radLat1) * Math.cos(radLat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return radius * c;
    }

    public static void getInput(GatheredData gd1, GatheredData gd2)
    {
        Scanner input = new Scanner(System.in);

        System.out.print("Please enter the latitude of the first point: ");
        gd1.latitude = input.nextDouble();

        System.out.print("Please enter the longitude of the first point: ");
        gd1.longitude = input.nextDouble();

        System.out.print("Please enter how many hours you would like the trip to take: ");
        gd1.time = input.nextLong();

        System.out.print("Please enter the latitude of the second point: ");
        gd2.latitude = input.nextDouble();

        System.out.print("Please enter the longitude of the second point: ");
        gd2.longitude = input.nextDouble();

        System.out.print("Please enter how many hours you would like the trip to take: ");
        gd2.time = input.nextLong();
    }

    public static void main(String[] args) {
        GatheredData gd1 = new GatheredData();
        GatheredData gd2 = new GatheredData();

        getInput(gd1, gd2);

        double distance = haversine(gd1, gd2);

        double vel1 = distance / gd1.time;
        double vel2 = distance / gd2.time;

        System.out.format("The first velocity is: %.3f km/h\n", vel1);
        System.out.format("The second velocity is: %.3f km/h\n", vel2);

        double aveVel = (vel1 + vel2) / 2;

        System.out.format("The average velocity is: %.3f km/h\n", aveVel);

    }
}
