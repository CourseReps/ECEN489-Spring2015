import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;

/**
 * Created by RhoadsWylde on 4/29/2015.
 */
//instantiates the Add_User Jframe
public class Add_User extends JFrame{
    public JPanel userInfo;
    private JTextField userName;
    private JTextField Name;
    private JTextField Password;
    private JButton enterButton;
    private JButton clearInfoButton;
    private JLabel userNameLabel;
    private JLabel NameLabel;
    private JLabel PasswordLabel;
    private JTextArea instructions;
    public String[] userData;
    private System_Admin systemAdmin;
    private Add_User info = this;

    //creates the user info jpanel with hidden buttons
    public Add_User() {
        super("Create User");
        setContentPane(userInfo);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
        userNameLabel.setVisible(true);
        NameLabel.setVisible(true);
        PasswordLabel.setVisible(true);
        instructions.setVisible(true);
        enterButton.setEnabled(false);
        clearInfoButton.setEnabled(false);
        instructions.setWrapStyleWord(true);
////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        userName.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            public void insertUpdate(DocumentEvent e) {
                changed();
            }
        });
////////////////////////////////////////////////////////////////////
        //key listener to monitor for enter key and move to next field
        userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Name.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        Name.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            public void insertUpdate(DocumentEvent e) {
                changed();
            }
        });
////////////////////////////////////////////////////////////////////
        //key listener to monitor for enter key and move to next field
        Name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Password.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        Password.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            public void insertUpdate(DocumentEvent e) {
                changed();
            }
        });
////////////////////////////////////////////////////////////////////
        //key listener to monitor for enter key and move to next field
        Password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterButton.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //submits the info when enter button is clicked
        enterButton.addActionListener(e -> systemAdmin.addUsers());
////////////////////////////////////////////////////////////////////
        //key listener to monitor for enter key and submits the info
        enterButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    systemAdmin.addUsers();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //clears out the fields when clicked
        clearInfoButton.addActionListener(e -> {
            userName.setText("");
            Name.setText("");
            Password.setText("");
            clearInfoButton.setEnabled(false);
            enterButton.setEnabled(false);
        });
    }
////////////////////////////////////////////////////////////////////
    //when the fields change this enables the buttons
    public void changed() {
        if (!userName.getText().equals("") && !Name.getText().equals("") && !Password.getText().equals("")) {
            enterButton.setEnabled(true);
        }
        if (!userName.getText().equals("") || !Name.getText().equals("") || !Password.getText().equals("")) {
            clearInfoButton.setEnabled(true);
        }
    }
////////////////////////////////////////////////////////////////////
    //this creates an array for System_Admin and encrypts the password and sessionID
    public String[] info() throws NoSuchAlgorithmException{
        userData = new String[5];
        userData[0] = userName.getText();
        userData[1] = Name.getText();
        userData[2] = HashMachine.generateSessionID();
        userData[3] = HashMachine.generateUnsaltedUserHash(Password.getText());
        userData[4] = "SALT";
        setVisible(false);
        return userData;
    }
////////////////////////////////////////////////////////////////////
    //this handles communication between classes
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
}
