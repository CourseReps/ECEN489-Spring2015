import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;


/**
 * Created by RhoadsWylde on 4/15/2015.
 */
public class System_Admin extends JFrame
{
    private boolean serverConnected = false;
    public static String selectedTable;
    private JPanel SystemAdmin;
    private JLabel TableList;
    private JList Tables;
    public JTable dataTable;
    private JLabel Table;
    private JButton Connect;
    private JButton addUser;
    private JButton deleteUser;
    private JButton addLocation;
    private JButton deleteLocation;
    private JButton ExitButton;
    private final System_Admin systemAdmin = this;
    private ResultSet rSet = null;
    private Add_User au;
    private Delete_User du;
    private Add_Location al;
    private Delete_Location dl;

    //the following creates a new instance of the System_Admin Jpanel
    public System_Admin () {
        super("PhysicAmigo Administration Program");
        setContentPane(SystemAdmin);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             //what happens when X is clicked
        setVisible(true);                                           //set the frame visible
        Tables.setVisible(false);                                   //hides the loadDataButton JButton
        Tables.setEnabled(false);                                   //disables the Tables JList
        dataTable.setEnabled(false);                                //disables the dataTable JTable
        addUser.setEnabled(false);                                  //disables the addRowButton
        deleteUser.setEnabled(false);                               //disables the deleteRowButton
        addLocation.setEnabled(false);                              //disables the addLocation JButton
        deleteLocation.setEnabled(false);                           //disables the deleteLocation JButton
////////////////////////////////////////////////////////////////////
        //the following creates an action listener for the connect button to start the credentials process
        Connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Credentials password = new Credentials();
                password.addSysAdmin(systemAdmin);
            }
        });
////////////////////////////////////////////////////////////////////
        //this creates an action listener for the exit button for the program
        ExitButton.addActionListener(e -> {
            if (DBHelp.dbConn()) {
                DBHelp dbconnect = new DBHelp();
                dbconnect.DBClose();
                System.exit(0);
            } else {
                System.exit(0);
            }
        });
////////////////////////////////////////////////////////////////////
        //this creates a mouse listener to get the selected table
        Tables.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonEnable();
                tableEnable();
            }
        });
////////////////////////////////////////////////////////////////////
        //this creates the same as above but with a key listener
        Tables.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonEnable();
                    tableEnable();
                }
            }
        });
////////////////////////////////////////////////////////////////////
        //this creates the action listener for the add user button
        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                au = new Add_User();
                au.addSysAdmin(systemAdmin);
            }
        });
////////////////////////////////////////////////////////////////////
        //this creates the action listener for the delete user button
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                du = new Delete_User();
                du.addSysAdmin(systemAdmin);
            }
        });
////////////////////////////////////////////////////////////////////
        //this creates the action listener for the add location button
        addLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                al = new Add_Location();
                al.addSysAdmin(systemAdmin);
            }
        });
////////////////////////////////////////////////////////////////////
        //this creates the action listener for the delete location button
        deleteLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dl = new Delete_Location();
                dl.addSysAdmin(systemAdmin);
            }
        });
////////////////////////////////////////////////////////////////////
    }
////////////////////////////////////////////////////////////////////
    //this method handles the Credentials connection
    public void CredentialsConnected() {
            if (Credentials.connection()) {
                Tables.setEnabled(true);                           //enables the Tables JList
                Tables.setVisible(true);                           //shows the loadDataButton JButton
                dataTable.setEnabled(true);                        //enables the dataTable JTable
            }
    }
////////////////////////////////////////////////////////////////////
    public String getTableID() {                                   //changes the label above the table
        selectedTable = Tables.getSelectedValue().toString();      //places the table name in string
        Table.setText(selectedTable);                              //renames the label to the table name
        return selectedTable;
    }
////////////////////////////////////////////////////////////////////
    public void buttonEnable(){
        String tableID = getTableID();
        if(tableID.equals("Users")) {
            addUser.setEnabled(true);                              //enables the addUser JButton
            deleteUser.setEnabled(true);                           //enables the deleteUser JButton
            addLocation.setEnabled(false);                         //disables the addLocation JButton
            deleteLocation.setEnabled(false);                      //disables the deleteLocation JButton
        }
        else if (tableID.equals("Locations")) {
            addLocation.setEnabled(true);                          //enables the addLocation JButton
            deleteLocation.setEnabled(true);                       //enables the deleteLocation JButton
            addUser.setEnabled(false);                             //disables the addRowButton
            deleteUser.setEnabled(false);                          //disables the deleteRowButton
        }
    }
///////////////////////////////////////////////////////////////////
    //enables the table and calls the get data method to acquire the database
    public void tableEnable() {
        if(selectedTable.equals("Users")) {
            DBHelp dbconnect = new DBHelp();
            dbconnect.DBConnect();
            rSet = dbconnect.getData();
            dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
        }
        else if (selectedTable.equals("Locations")) {
            DBHelp dbconnect = new DBHelp();
            dbconnect.DBConnect();
            rSet = dbconnect.getData();
            dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
        }
    }
///////////////////////////////////////////////////////////////////
    //this takes the array from DBHelp and sends parts to database
    //also redraws the table and encrypts the password
    public void addUsers() {
        DBHelp dbconnect = new DBHelp();
        dbconnect.DBConnect();
        try {
            String[] addData = au.info();
            dbconnect.addUser(addData[0], addData[1], addData[2], addData[3], addData[4]);
            rSet = dbconnect.getData();
            dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
     }
///////////////////////////////////////////////////////////////////
    //this takes the number from DBHelp to delete user
    //also redraws the table and encrypts the password
    public void deleteUsers() {
        DBHelp dbconnect = new DBHelp();
        dbconnect.DBConnect();
        dbconnect.deleteUser(du.info());
        dbconnect.
        rSet = dbconnect.getData();
        dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
    }
///////////////////////////////////////////////////////////////////
    //this takes the array from DBHelp and sends the parts to database
    //also redraws the table and encrypts the password
    public void addLoc() {
        String[] addData = al.info();
        double A = Double.parseDouble(addData[1]);
        double B = Double.parseDouble(addData[2]);
        DBHelp dbconnect = new DBHelp();
        dbconnect.DBConnect();
        dbconnect.addLocation(addData[0], A, B, addData[3]);
        rSet = dbconnect.getData();
        dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
    }
///////////////////////////////////////////////////////////////////
    //this takes the number from DBHelp to delete user
    //also redraws the table and encrypts the password
    public void deleteLoc() {
        DBHelp dbconnect = new DBHelp();
        dbconnect.DBConnect();
        dbconnect.deleteLocation(dl.info());
        rSet = dbconnect.getData();
        dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
    }
///////////////////////////////////////////////////////////////////


}
