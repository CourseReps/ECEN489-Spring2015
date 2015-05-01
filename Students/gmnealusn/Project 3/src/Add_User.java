import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

/**
 * Created by RhoadsWylde on 4/29/2015.
 */
public class Add_User extends JFrame{
    public JPanel userInfo;
    private JTextField userName;
    private JTextField Name;
    private JTextField SessionID;
    private JTextField Password;
    private JButton enterButton;
    private JButton clearInfoButton;
    private JLabel userNameLabel;
    private JLabel NameLabel;
    private JLabel SessionIDLabel;
    private JLabel PasswordLabel;
    private JTextArea instructions;
    public String[] userData;
    private System_Admin systemAdmin;
    private Add_User info = this;

    public Add_User() {
        super("Create User");
        setContentPane(userInfo);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
        userNameLabel.setVisible(true);
        NameLabel.setVisible(true);
        SessionIDLabel.setVisible(true);
        PasswordLabel.setVisible(true);
        instructions.setVisible(true);
        enterButton.setEnabled(false);
        clearInfoButton.setEnabled(false);


////////////////////////////////////////////////////////////////////
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
        Name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SessionID.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        SessionID.getDocument().addDocumentListener(new DocumentListener() {
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
        SessionID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Password.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
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
        enterButton.addActionListener(e -> systemAdmin.addUsers());
////////////////////////////////////////////////////////////////////
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
        clearInfoButton.addActionListener(e -> {
            userName.setText("");
            Name.setText("");
            SessionID.setText("");
            Password.setText("");
            clearInfoButton.setEnabled(false);
            enterButton.setEnabled(false);
        });
    }
////////////////////////////////////////////////////////////////////
    public void changed() {
        if (!userName.getText().equals("") && !Name.getText().equals("") &&
                !SessionID.getText().equals("") && !Password.getText().equals("")) {
            enterButton.setEnabled(true);
        }
        if (!userName.getText().equals("") || !Name.getText().equals("") ||
                !SessionID.getText().equals("") || !Password.getText().equals("")) {
            clearInfoButton.setEnabled(true);
        }
    }
////////////////////////////////////////////////////////////////////
    public String[] info(){
        userData = new String[5];
        userData[0] = userName.getText();
        userData[1] = Name.getText();
        userData[2] = SessionID.getText();
        userData[3] = Password.getText();
        userData[4] = "SALT";
        setVisible(false);
        return userData;
    }
////////////////////////////////////////////////////////////////////
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
}
