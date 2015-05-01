import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by RhoadsWylde on 5/1/2015.
 */
public class Delete_Location extends JFrame {
    public JPanel locInfo;
    private JTextField locID;
    private JButton enterButton;
    private JButton clearInfoButton;
    private JLabel locIDLabel;
    private JTextArea instructions;
    public int locData;
    private Delete_Location info = this;
    private System_Admin systemAdmin;


    public Delete_Location() {
        super("Delete User");
        setContentPane(locInfo);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //when X is pressed window is hidden
        setVisible(true);
        locIDLabel.setVisible(true);
        instructions.setVisible(true);
        enterButton.setEnabled(false);
        clearInfoButton.setEnabled(false);


        ////////////////////////////////////////////////////////////////////
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
        enterButton.addActionListener(e -> systemAdmin.deleteLoc());
////////////////////////////////////////////////////////////////////
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
        clearInfoButton.addActionListener(e -> {
            locID.setText("");
            clearInfoButton.setEnabled(false);
            enterButton.setEnabled(false);
        });
    }

    ////////////////////////////////////////////////////////////////////
    public void changed() {
        if (!locID.getText().equals("") || !locID.getText().equals("")) {
            enterButton.setEnabled(true);
            clearInfoButton.setEnabled(true);
        }
    }
    ////////////////////////////////////////////////////////////////////
    public int info(){
        locData = Integer.parseInt(locID.getText());
        setVisible(false);
        return locData;
    }
    ////////////////////////////////////////////////////////////////////
    public void addSysAdmin(System_Admin sa) {
        this.systemAdmin = sa;
    }
}

