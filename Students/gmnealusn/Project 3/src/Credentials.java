import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by RhoadsWylde on 4/15/2015.
 */
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

    public Credentials(){                                    //the following creates a new instance of the Password Jpanel
        super("Login");
        setContentPane(Cred);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
////////////////////////////////////////////////////////////////////
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
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
////////////////////////////////////////////////////////////////////
    public static boolean connection(){
        return connected;
    }
////////////////////////////////////////////////////////////////////
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
////////////////////////////////////////////////////////////////////
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






