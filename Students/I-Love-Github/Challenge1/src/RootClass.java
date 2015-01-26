import javax.swing.*;

// ROOT CLASS
// This class contains the object where the java program begins
public class RootClass {

    // MAIN FUNCTION
    // This function creates two swing frame objects...
    // MAP PANEL: This object creates a JFrame containing a custom panel that displays the map (MapPanel class)
    // CALC PANE: This object creates another JFrame that contains text fields that allow the user to enter travel
    //              times and a button that performs the required average speed calculation
    public static void main(String[] args) {
        // Create MAP PANEL frame
        JFrame f = new JFrame("Map Panel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MapPanel mapPanel = new MapPanel();
        f.setContentPane(mapPanel);
        f.setSize(826, 568);
        f.setLocation(200, 200);
        f.setVisible(true);

        // Create CALC PANEL frame to the right of the MAP PANEL frame
        JFrame buttons = new JFrame("Calculation Panel");
        buttons.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalcPanel calcPanel = new CalcPanel();
        buttons.setContentPane(calcPanel);
        buttons.setSize(500, 200);
        buttons.setLocation(1050, 200);
        buttons.setVisible(true);

        // Connect MAP PANEL to CALC PANEL so location information can be transferred between the two objects
        mapPanel.setCalcLink(calcPanel);
    }
}
