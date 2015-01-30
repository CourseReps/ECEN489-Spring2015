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
        JFrame f = new JFrame("Server");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ServerPanel mapPanel = new ServerPanel();
        f.setContentPane(mapPanel);
        f.setSize(500, 460);
        f.setLocation(200, 200);
        f.setVisible(true);
   }
}