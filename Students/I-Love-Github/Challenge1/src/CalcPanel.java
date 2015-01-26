import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

// CALC PANEL CLASS
// This class is a custom JPanel that displays a series of text labels and fields.
// The "Calculate" JButton allows the user to perform an average speed calculation based upon two
// locations and departure/arrival times
//
// This class makes use of the GATHERED DATA CLASS
class CalcPanel extends JPanel {

    // Haversine radius constant for the Earth
    private static final double HAVERSINE_RADIUS = 6372.8;

    // INSTANCES OF THE GATHERED DATA CLASS
    private GatheredData location1;
    private GatheredData location2;

    // Instances of the "Location" JLabels, required as fields because they are changed in other methods
    private final JLabel loc1Label;
    private final JLabel loc2Label;

    // CONSTRUCTOR: Creates and adds the fields and action listeners to the base Panel object
    public CalcPanel() {

        // Create the GATHERED DATA objects (default fields to 0)
        location1 = new GatheredData();
        location2 = new GatheredData();

        // Location (lat/lon) text labels
        loc1Label = new JLabel("Location 1: null");
        loc2Label = new JLabel("Location 2: null");

        // User entry for location 1 and 2 GATHERED DATA -> TIME fields
        final JLabel time1FieldLabel = new JLabel("Time 1 (in s)");
        final JTextField time1Field = new JTextField("0");

        final JLabel time2FieldLabel = new JLabel("Time 2 (in s)");
        final JTextField time2Field = new JTextField("36000");

        // Text labels that display the difference between Time 1 and Time 2
        final JLabel totalTimeLabel = new JLabel("Time Difference");
        final JLabel totalTimeField = new JLabel("0");

        // Text labels that display the great circle distance between Location 1 and Location 2
        final JLabel distanceLabel = new JLabel("Distance Traveled");
        final JLabel distanceField = new JLabel("0");

        // Text label that displays the average speed considering the two locations and travel time
        final JLabel resultLabel = new JLabel("   Result goes here");

        // CALCULATE BUTTON: Implements an ACTION LISTENER that performs the average speed calculation
        final JButton button = new JButton("Calculate");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                location1.setTime(Long.parseLong(time1Field.getText()));
                location2.setTime(Long.parseLong(time2Field.getText()));

                long timeResult = Math.abs(location2.getTime() - location1.getTime());

                double distanceResult = haversine(
                        location1.getLatitude(), location1.getLongitude(),
                        location2.getLatitude(), location2.getLongitude());

                double speedResult = distanceResult/timeResult;
                speedResult = speedResult * 3600;
                double mph = speedResult * 0.621371;

                DecimalFormat df = new DecimalFormat("0.00");
                totalTimeField.setText(String.valueOf(df.format(timeResult)) + " s");
                distanceField.setText(String.valueOf(df.format(distanceResult)) + " km");

                resultLabel.setText("   " + String.valueOf(df.format(speedResult)) + " km/hr or "
                        + String.valueOf(df.format(mph) + " mph"));
            }
        });

        // Build the JPanel
        this.setLayout(new GridLayout(6, 2));
        this.add(loc1Label);
        this.add(loc2Label);
        this.add(time1FieldLabel);
        this.add(time1Field);
        this.add(time2FieldLabel);
        this.add(time2Field);
        this.add(totalTimeLabel);
        this.add(totalTimeField);
        this.add(distanceLabel);
        this.add(distanceField);
        this.add(button);
        this.add(resultLabel);
    }

    // SET LOCATION METHOD
    // Takes in the latitude and longitude of a location clicked on the map, then populates the LOCATION LABEL and
    // GATHERED DATA objects as required
    // BOOLEAN firstLoc determines if the first or second LOCATION is being populated
    public void setLocation(boolean firstLoc, double lat, double lon) {

        // Given the value of lat and lon, set the string that shows the correct cardinal direction
        String NorS = "N";
        String EorW = "E";

        // Lat <= 0 is NORTH
        if (lat > 0) {
            NorS = "S";
        }

        // Lon <= 0 is EAST
        if (lon > 0) {
            EorW = "W";
        }

        // Remove sign information from lat/lon for the purpose of string display
        double unsignedLat = Math.abs(lat);
        double unsignedLon = Math.abs(lon);

        // Truncate all string values to two decimal places
        DecimalFormat df = new DecimalFormat("0.00");

        // Create and populate GATHERED DATA object and LOCATION LABEL text label as appropriate
        if (firstLoc) {
            this.location1 = new GatheredData(lat, lon);
            this.loc1Label.setText("LOC1 -- Lat: " + df.format(unsignedLat) + NorS
                    + "   Lon: " + df.format(unsignedLon) + EorW);

        } else {
            this.location2 = new GatheredData(lat, lon);
            this.loc2Label.setText("LOC2 -- Lat: " + df.format(unsignedLat) + NorS
                    + "   Lon: " + df.format(unsignedLon) + EorW);
        }
    }

    // HAVERSINE CALCULATION
    // Performs a great circle distance calculation based upon the latitude and longitude of two points on the globe
    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return HAVERSINE_RADIUS * c;
    }
}