import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by RhoadsWylde on 4/30/2015.
 */


public class Delete_User extends JFrame{
    public JPanel userInfo;
    private JTextField userID;
    private JButton enterButton;
    private JButton clearInfoButton;
    private JLabel userIDLabel;
    private JTextArea instructions;
    public int userData;
    private Delete_User info = this;
    private System_Admin systemAdmin;


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
        enterButton.addActionListener(e -> systemAdmin.deleteUsers());
////////////////////////////////////////////////////////////////////
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
        clearInfoButton.addActionListener(e -> {
            userID.setText("");
            clearInfoButton.setEnabled(false);
            enterButton.setEnabled(false);
        });
    }

////////////////////////////////////////////////////////////////////
    public void changed() {
        if (!userID.getText().equals("") || !userID.getText().equals("")) {
            enterButton.setEnabled(true);
            clearInfoButton.setEnabled(true);
        }
    }
////////////////////////////////////////////////////////////////////
    public int info(){
        userData = Integer.parseInt(userID.getText());
        setVisible(false);
        return userData;
    }
////////////////////////////////////////////////////////////////////
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
}

