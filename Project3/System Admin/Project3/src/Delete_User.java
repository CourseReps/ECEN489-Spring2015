import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by RhoadsWylde on 4/30/2015.
 */

//instantiates the Delete_User Jframe
public class Delete_User extends JFrame{
    public JPanel userInfo;
    private JFormattedTextField userID;
    private JButton enterButton;
    private JButton clearInfoButton;
    private JLabel userIDLabel;
    private JTextArea instructions;
    public int userData;
    private Delete_User info = this;
    private System_Admin systemAdmin;

    //creates the delete user jpanel with hidden buttons
    public Delete_User() {
        super("Delete User");
        setContentPane(userInfo);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
        userIDLabel.setVisible(true);
        instructions.setVisible(true);
        enterButton.setEnabled(false);
        clearInfoButton.setEnabled(false);


////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        userID.getDocument().addDocumentListener(new DocumentListener() {
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
        userID.addKeyListener(new KeyAdapter() {
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
        enterButton.addActionListener(e -> systemAdmin.deleteUsers());
////////////////////////////////////////////////////////////////////
        //key listener to monitor for enter key and submits the info
        enterButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    systemAdmin.deleteUsers();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //clears out the fields when clicked
        clearInfoButton.addActionListener(e -> {
            userID.setText("");
            clearInfoButton.setEnabled(false);
            enterButton.setEnabled(false);
        });
    }
////////////////////////////////////////////////////////////////////
    //when the field changes this enables the buttons
    public void changed() {
        if (!userID.getText().equals("") || !userID.getText().equals("")) {
            enterButton.setEnabled(true);
            clearInfoButton.setEnabled(true);
        }
    }
////////////////////////////////////////////////////////////////////
    //creates an int with the text and returns the int
    public int info(){
        userData = Integer.parseInt(userID.getText());
        setVisible(false);
        return userData;
    }
////////////////////////////////////////////////////////////////////
    //this handles communication between classes
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
}

