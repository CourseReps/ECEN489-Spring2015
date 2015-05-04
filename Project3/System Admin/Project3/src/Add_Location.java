/**
 * Created by RhoadsWylde on 5/1/2015.
 */import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

/**
 * Created by RhoadsWylde on 4/29/2015.
 */
    //this instantiates the Add_Location JFrame
public class Add_Location extends JFrame{
    public JPanel userInfo;
    private JTextField nameField;
    private JFormattedTextField latitude;
    private JFormattedTextField longitude;
    private JTextField image;
    private JButton enterButton;
    private JButton clearInfoButton;
    private JLabel nameLabel;
    private JLabel latitudeLabel;
    private JLabel longitudeLabel;
    private JLabel imageLabel;
    private JTextArea instructions;
    public String[] locData;
    private System_Admin systemAdmin;
    private Add_Location info = this;

    //this creates the Add Location JPanel with hidden buttons
    public Add_Location() {
        super("Create User");
        setContentPane(userInfo);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is closed
        setVisible(true);
        nameLabel.setVisible(true);
        latitudeLabel.setVisible(true);
        longitudeLabel.setVisible(true);
        imageLabel.setVisible(true);
        instructions.setVisible(true);
        enterButton.setEnabled(false);
        clearInfoButton.setEnabled(false);
////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        nameField.getDocument().addDocumentListener(new DocumentListener() {
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
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    latitude.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        latitude.getDocument().addDocumentListener(new DocumentListener() {
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
        latitude.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    longitude.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        longitude.getDocument().addDocumentListener(new DocumentListener() {
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
        //key listener to monitor for enter key and submits the info
        longitude.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    image.requestFocus();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        image.getDocument().addDocumentListener(new DocumentListener() {
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
        //key listener to monitor for enter key and submits the info
        image.addKeyListener(new KeyAdapter() {
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
        enterButton.addActionListener(e -> systemAdmin.addLoc());
////////////////////////////////////////////////////////////////////
        //key listener to monitor for enter key and submits the info
        enterButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    systemAdmin.addLoc();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //clears out the fields when clicked
        clearInfoButton.addActionListener(e -> {
            nameField.setText("");
            latitude.setText("");
            longitude.setText("");
            image.setText("");
            clearInfoButton.setEnabled(false);
            enterButton.setEnabled(false);
        });
    }
    ////////////////////////////////////////////////////////////////////
    //when the fields change this enables the buttons
    public void changed() {
        if (!nameField.getText().equals("") && !latitude.getText().equals("") &&
                !longitude.getText().equals("") && !image.getText().equals("")) {
            enterButton.setEnabled(true);
        }
        if (!nameField.getText().equals("") || !latitude.getText().equals("") ||
                !longitude.getText().equals("") || !image.getText().equals("")) {
            clearInfoButton.setEnabled(true);
        }
    }
    ////////////////////////////////////////////////////////////////////
    //this creates an array for System_Admin and encrypts the password and sessionID
    public String[] info(){
        locData = new String[4];
        locData[0] = nameField.getText();
        locData[1] = latitude.getText();
        locData[2] = longitude.getText();
        locData[3] = image.getText();
        setVisible(false);
        return locData;
    }
    ////////////////////////////////////////////////////////////////////
    //this handles communication between classes
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }

}


