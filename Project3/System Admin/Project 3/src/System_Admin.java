import javax.swing.*;
import java.awt.event.*;
import java.sql.*;


/**
 * Created by RhoadsWylde on 4/15/2015.
 */
public class System_Admin extends JFrame{
    private boolean connected = false;
    private String selectedTable;
    private JPanel SystemAdmin;
    private JLabel TableList;
    private JList Tables;
    private JTable dataTable;
    private JLabel Table;
    private JButton Connect;
    private JButton loadDataButton;
    private JButton addRowButton;
    private JButton deleteRowButton;
    private JButton enterInfoButton;
    private JButton clearInfoButton;
    private JButton ExitButton;

    private final System_Admin systemAdmin = this;


    public System_Admin () {                             //the following creates a new instance of the System_Admin Jpanel
        super("PhysicAmigo Administration Program");
        setContentPane(SystemAdmin);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //what happens when X is clicked
        setVisible(true);
        Tables.setVisible(false);               //hides the loadDataButton JButton
        Tables.setEnabled(false);                       //disables the Tables JList
        dataTable.setEnabled(false);                    //disables the dataTable JTable
        loadDataButton.setEnabled(false);               //disables the loadDataButton
        addRowButton.setEnabled(false);                 //disables the addRowButton
        deleteRowButton.setEnabled(false);              //disables the deleteRowButton
        enterInfoButton.setEnabled(false);              //disables the enterInfoButton
        clearInfoButton.setEnabled(false);              //disables the clearInfoButton

////////////////////////////////////////////////////////////////////
        Connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Credentials password = new Credentials();
                password.addSysAdmin(systemAdmin);
                System.out.println(Credentials.connection());
            }
        });
////////////////////////////////////////////////////////////////////
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
////////////////////////////////////////////////////////////////////
        Tables.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadDataButton.setEnabled(true);
                selectedTable = Tables.getSelectedValue().toString();
                Table.setText(selectedTable);
            }
        });
////////////////////////////////////////////////////////////////////
        Tables.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadDataButton.setEnabled(true);
                    selectedTable = Tables.getSelectedValue().toString();
                    Table.setText(selectedTable);
                }
            }
        });
////////////////////////////////////////////////////////////////////
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
////////////////////////////////////////////////////////////////////
        loadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
////////////////////////////////////////////////////////////////////
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
////////////////////////////////////////////////////////////////////
        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
////////////////////////////////////////////////////////////////////
        enterInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
////////////////////////////////////////////////////////////////////
        clearInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
////////////////////////////////////////////////////////////////////
    }

    public void CredentialsConnected() {
            System.out.println(Credentials.connection());
            if (Credentials.connection()) {
                Tables.setEnabled(true);                       //enables the Tables JList
                Tables.setVisible(true);                       //shows the loadDataButton JButton
                loadDataButton.setEnabled(true);               //enables the loadDataButton JButton
                dataTable.setEnabled(true);                    //enables the dataTable JTable
                addRowButton.setEnabled(true);                 //enables the addRowButton
                deleteRowButton.setEnabled(true);              //enables the deleteRowButton
                enterInfoButton.setEnabled(true);              //enables the enterInfoButton
                clearInfoButton.setEnabled(true);              //enables the clearInfoButton
            }
    }

}
