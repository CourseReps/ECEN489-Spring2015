/**
 * Created by RhoadsWylde on 5/1/2015.
 */import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

/**
 * Created by RhoadsWylde on 4/29/2015.
 */
public class Add_Location extends JFrame{
    public JPanel userInfo;
    private JTextField nameField;
    private JTextField latitude;
    private JTextField longitude;
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

    public Add_Location() {
        super("Create User");
        setContentPane(userInfo);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
        nameLabel.setVisible(true);
        latitudeLabel.setVisible(true);
        longitudeLabel.setVisible(true);
        imageLabel.setVisible(true);
        instructions.setVisible(true);
        enterButton.setEnabled(false);
        clearInfoButton.setEnabled(false);


////////////////////////////////////////////////////////////////////
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
        enterButton.addActionListener(e -> systemAdmin.addLoc());
////////////////////////////////////////////////////////////////////
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
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }

}


