import javax.swing.*;

// ROOT CLASS
// This class contains the object where the java program begins
public class RootClass {

    // MAIN FUNCTION //
    // Create the Swing GUI window and go from there
    public static void main(String[] args) {
        final JFrame f = new JFrame("Endless Thread Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final UserInterface userInterface = new UserInterface();
        f.setContentPane(userInterface);
        f.setSize(450, 180);
        f.setLocation(200, 200);
        f.setVisible(true);
    }
}