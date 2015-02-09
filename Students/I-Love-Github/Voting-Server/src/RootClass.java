import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// ROOT CLASS
// This class contains the object where the java program begins
public class RootClass {

    // MAIN FUNCTION
    // Create and build the main program window
    public static void main(String[] args) {
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