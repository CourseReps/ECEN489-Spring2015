import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

class CalcPanel extends JPanel {

    private static final double HAVERSINE_RADIUS = 6372.8;

    private GatheredData location1;
    private GatheredData location2;

    private final JLabel loc1Label;
    private final JLabel loc2Label;
    private final JTextField time1Field;
    private final JTextField time2Field;
    private final JLabel totalTimeField;
    private final JLabel distanceField;
    private final JLabel resultLabel;

    public CalcPanel() {
        this.setLayout(new GridLayout(6, 2));

        location1 = new GatheredData();
        location2 = new GatheredData();

        loc1Label = new JLabel("Location 1: null");
        loc2Label = new JLabel("Location 2: null");

        final JLabel time1FieldLabel = new JLabel("Time 1 (in s)");
        time1Field = new JTextField("0");

        final JLabel time2FieldLabel = new JLabel("Time 2 (in s)");
        time2Field = new JTextField("36000");

        final JLabel totalTimeLabel = new JLabel("Time Difference");
        totalTimeField = new JLabel("0");

        final JLabel distanceLabel = new JLabel("Distance Traveled");
        distanceField = new JLabel("0");

        final JButton button = new JButton("Calculate");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DecimalFormat df = new DecimalFormat("0.00");
                long timeResult = Long.parseLong(time2Field.getText()) - Long.parseLong(time1Field.getText());

                timeResult = Math.abs(timeResult);
                totalTimeField.setText(String.valueOf(df.format(timeResult)) + " s");

                double distanceResult = haversine(
                        location1.getLatitude(), location1.getLongitude(),
                        location2.getLatitude(), location2.getLongitude());

                distanceField.setText(String.valueOf(df.format(distanceResult)) + " km");

                double speedResult = distanceResult/timeResult;
                speedResult = speedResult * 3600;
                double mph = speedResult * 0.621371;
                resultLabel.setText("   " + String.valueOf(df.format(speedResult)) + " km/hr or "
                        + String.valueOf(df.format(mph) + " mph"));
            }
        });

        resultLabel = new JLabel("   Result goes here");

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

    public void setLoc1(boolean firstLoc, double lat, double lon, boolean NorS, boolean EorW) {

        double signedLat = cardinalToSigned(lat, NorS);
        double signedLon = cardinalToSigned(lon, EorW);

        DecimalFormat df = new DecimalFormat("0.00");

        if (firstLoc) {
            this.location1 = new GatheredData(signedLat, signedLon);
            this.loc1Label.setText("LOC1 -- Lat: " + df.format(lat) + latString(NorS)
                    + "   Lon: " + df.format(lon) + lonString(EorW));

        } else {
            this.location2 = new GatheredData(signedLat, signedLon);
            this.loc2Label.setText("LOC2 -- Lat: " + df.format(lat) + latString(NorS)
                    + "   Lon: " + df.format(lon) + lonString(EorW));
        }
    }

    private double cardinalToSigned(double value, boolean cardinal) {
        if (cardinal) {
            return value;
        } else {
            return (-1 * value);
        }
    }

    private String latString(boolean NorS) {
        if (NorS) {
            return "N";
        } else {
            return "S";
        }
    }

    private String lonString(boolean EorW) {
        if (EorW) {
            return "E";
        } else {
            return "W";
        }
    }

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