import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by RhoadsWylde on 4/15/2015.
 */
//instantiates the credentials JFrame
public class Credentials extends JFrame{
    private static boolean connected;
    private JPanel Cred;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton cancelButton;
    private JButton enterButton;
    private JLabel userLabel;
    private JLabel pwLabel;
    boolean servConnected;
    private System_Admin systemAdmin;
    private Credentials credentials = this;

    //the following creates a new instance of the Password Jpanel
    public Credentials(){
        super("Login");
        setContentPane(Cred);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
////////////////////////////////////////////////////////////////////
        //monitors the enter button and when clicked begins server connection
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBHelp serverConnect = new DBHelp();
                serverConnect.addCredentials(credentials);
                servConnected = serverConnect.serverConnect();
                systemAdmin.CredentialsConnected();
            }
        });
////////////////////////////////////////////////////////////////////
        //monitors the cancel button and when clicked closes the window
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
////////////////////////////////////////////////////////////////////
    //connected status
    public static boolean connection(){
        return connected;
    }
////////////////////////////////////////////////////////////////////
    //handles comms between classes
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
////////////////////////////////////////////////////////////////////
    //if the server connects closes window other wise leaves it open
    public void serverConnected() {
        if (DBHelp.svrConn()) {
            setVisible(false);
            System.out.println();
            connected = true;
        }
        else {
            connected =  false;
            }
    }

 }






