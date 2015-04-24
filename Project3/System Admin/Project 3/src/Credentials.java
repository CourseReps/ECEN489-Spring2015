import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by RhoadsWylde on 4/15/2015.
 */
public class Credentials extends JFrame{
    private static boolean connected;
    private JPanel Credentials;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton cancelButton;
    private JButton enterButton;
    private JLabel userLabel;
    private JLabel pwLabel;
    String DBconnected;

    private System_Admin systemAdmin;



    public Credentials(){                                 //the following creates a new instance of the Password Jpanel
        super("Login");
        setContentPane(Credentials);
        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);   //when X is pressed window is hidden

////////////////////////////////////////////////////////////////////
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(userField.getText());
                System.out.println(passwordField.getPassword());
                DBConnect dbconnect = new DBConnect();
                DBconnected = dbconnect.DBConnect();
                System.out.println(DBconnected);
                systemAdmin.CredentialsConnected();
                if (DBconnected == "yes") {
                    connected = true;
                    setVisible(false);
                }
                else {
                    connected =  false;
                }
            }
        });
////////////////////////////////////////////////////////////////////
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
////////////////////////////////////////////////////////////////////
        setVisible(true);

    }
    public static boolean connection(){
        return connected;
    }

    public void addSysAdmin(System_Admin sa) {

        this.systemAdmin = sa;
    }



}



