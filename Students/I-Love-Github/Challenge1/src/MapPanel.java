import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// MAP PANEL CLASS
// This class is a custom JPanel that displays a clickable map
// If the user clicks on the map, the latitude and longitude is passed to a CALC PANEL object and an X is displayed over
// the clicked location
// Several fields are used for cleaner inter-method communication
class MapPanel extends JPanel
{
    // CALC PANEL object reference
    CalcPanel calcPanel = null;

    // Boolean determines if this is the first or second location clicked
    // Points objects store up to two clicked locations for drawing purposes
    boolean whichLocation = true;
    Point firstClick = new Point();
    Point secondClick = new Point();

    // CONSTRUCTOR: Creates an ImageLabel object which displays the map and a MOUSE LISTENER to detect when and where
    //      the mouse is clicked on the map
    public MapPanel()
    {
        // Create and populate the panel object with a IMAGE LABEL object
        // Image loaded is hard-coded in the IMAGE LABEL class
        final JLabel imageLabel = new ImageLabel().getLabel();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        panel.add(imageLabel, new GridBagConstraints());

        // Mouse listener:
        // * Captures clicks on the map and sends the location information (in lat/lon) to the CALC PANEL.
        // * Stores location information in local fields (in pixels) for the purpose of drawing an "X" on the map
        imageLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                // Get the point info and determine which location we are looking at (1 or 2), then store in fields
                Point p = e.getPoint();
                if (whichLocation) {
                    firstClick = p;
                } else {
                    secondClick = p;
                }

                // Perform lat/lon transforms and send location to CALC PANEL
                calcPanel.setLocation(whichLocation, transformLat(p), transformLon(p));

                // Ensure next location is the "other" field.
                whichLocation = !whichLocation;

                // Redraw screen to show the "X"
                repaint();
            }
        });

        // Build the JPanel
        setLayout(new BorderLayout());
        add(panel);
    }

    // MAP PIXEL-LAT/LON TRANSFORMS
    // Map is 826x568
    //x:406 = 0 degrees east
    //y:284 = 0 degrees north

    // TRANSFORM LONGITUDE METHOD
    // Performs a transform that returns the longitude of the clicked map location based on the pixel coordinates
    // Negative values are WEST, positive values are EAST
    private double transformLon(Point p) {
        double value = p.getX();
        double divisor = 360;
        divisor = divisor / 826;
        value = value - 406;
        value = value * divisor;
        return value;
    }

    // TRANSFORM LATITUDE METHOD
    // Performs a transform that returns the latitude of the clicked map location based on the pixel coordinates
    // Negative values are NORTH, positive values are SOUTH
    private double transformLat(Point p) {
        double value = p.getY();
        double divisor = 284;
        divisor = 90 / divisor;
        value = value - 263;
        value = value * divisor;
        return value;
    }

    // Setter for CALC PANEL reference
    public void setCalcLink(CalcPanel calcPanel) {
        this.calcPanel = calcPanel;
    }

    // SCREEN DRAW function, allows for the drawing of "X"s on the map to denote clicked locations
    // Because only two points are stored, only the last two locations are marked on the map
    // This is okay, since we only care about two locations at a time
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // "X" arm length in pixels
        int xSize = 15;

        // From the point, build a box of X and Y extremes for the first X
        int x11 = (int) firstClick.getX() - xSize;
        int x12 = (int) firstClick.getX() + xSize;
        int y11 = (int) firstClick.getY() - xSize;
        int y12 = (int) firstClick.getY() + xSize;

        // Do the same for the other X
        int x21 = (int) secondClick.getX() - xSize;
        int x22 = (int) secondClick.getX() + xSize;
        int y21 = (int) secondClick.getY() + xSize;
        int y22 = (int) secondClick.getY() - xSize;

        // The x is 5 pixels wide, created by drawing 5 lines shifted by one pixel in the Y direction
        for (int i = -2; i < 3; i++) {
            // Don't draw anything if the values of this location is 0
            if (firstClick.getX() == firstClick.getY() && firstClick.getX() == 0) {
                return;
            }
            // Draw the X by drawing two crossing lines between the corners of the box created above
            g.drawLine(x11, y11 + i, x12, y12 + i);
            g.drawLine(x11, y12 + i, x12, y11 + i);
        }

        // Now do the same for the other X
        for (int i = -2; i < 3; i++) {
            if (secondClick.getX() == secondClick.getY() && secondClick.getX() == 0) {
                return;
            }
            g.drawLine(x21, y21 + i, x22, y22 + i);
            g.drawLine(x21, y22 + i, x22, y21 + i);
        }
    }
}