import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        final JFrame f = new JFrame("Server");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final ServerPanel serverPanel = new ServerPanel();
        f.setContentPane(serverPanel);
        f.setSize(550, 500);
        f.setLocation(200, 200);
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            public void WindowClosing(WindowEvent e) {
                serverPanel.emergencyShutdown();
                f.dispose();
            }
        });
   }
}