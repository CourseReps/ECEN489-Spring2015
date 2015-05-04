import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by RhoadsWylde on 5/1/2015.
 */
//instantiates the Add_User Jframe
public class Delete_Location extends JFrame {
    public JPanel locInfo;
    private JFormattedTextField locID;
    private JButton enterButton;
    private JButton clearInfoButton;
    private JLabel locIDLabel;
    private JTextArea instructions;
    public int locData;
    private Delete_Location info = this;
    private System_Admin systemAdmin;

    //creates the delete location jpanel with hidden buttons
    public Delete_Location() {
        super("Delete Location");
        setContentPane(locInfo);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
        locIDLabel.setVisible(true);
        instructions.setVisible(true);
        enterButton.setEnabled(false);
        clearInfoButton.setEnabled(false);


////////////////////////////////////////////////////////////////////
        //document listener to monitor changes to field calls changed method
        locID.getDocument().addDocumentListener(new DocumentListener() {
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
        locID.addKeyListener(new KeyAdapter() {
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
        enterButton.addActionListener(e -> systemAdmin.deleteLoc());
////////////////////////////////////////////////////////////////////
    //key listener to monitor for enter key and submits the info
        enterButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    systemAdmin.deleteLoc();
                }
            }
        });
////////////////////////////////////////////////////////////////////
    //clears out the fields when clicked
        clearInfoButton.addActionListener(e -> {
            locID.setText("");
            clearInfoButton.setEnabled(false);
            enterButton.setEnabled(false);
        });
    }
////////////////////////////////////////////////////////////////////
    //when the field changes this enables the buttons
    public void changed() {
        if (!locID.getText().equals("") || !locID.getText().equals("")) {
            enterButton.setEnabled(true);
            clearInfoButton.setEnabled(true);
        }
    }
////////////////////////////////////////////////////////////////////
    //creates an int with the text and returns the int
    public int info() throws NumberFormatException{
        try {
            locData = Integer.parseInt(locID.getText());
            setVisible(false);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return locData;
    }
////////////////////////////////////////////////////////////////////
    //this handles communication between classes
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
}

