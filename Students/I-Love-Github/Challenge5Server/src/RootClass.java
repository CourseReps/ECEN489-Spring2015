import javax.swing.*;

// ROOT CLASS
// This class contains the object where the java program begins
public class RootClass {

    // MAIN FUNCTION
    public static void main(String[] args) {

        final JFrame f = new JFrame("Server");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final ServerPanel serverPanel = new ServerPanel();
        f.setContentPane(serverPanel);
        f.setSize(500, 460);
        f.setLocation(200, 200);
        f.setVisible(true);

//        f.addWindowListener(new WindowAdapter() {
//            public void WindowClosing(WindowEvent e) {
//                clientPanel.disconnectClient();
//                f.dispose();
//            }
//        });
    }
}