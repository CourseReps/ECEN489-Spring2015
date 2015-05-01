import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;


/**
 * Created by RhoadsWylde on 4/15/2015.
 */
public class System_Admin extends JFrame
{
    private int flag = 0;
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
        Connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Credentials password = new Credentials();
                password.addSysAdmin(systemAdmin);
            }
        });
////////////////////////////////////////////////////////////////////
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
        Tables.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonEnable();
                tableEnable();
            }
        });
////////////////////////////////////////////////////////////////////
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
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
////////////////////////////////////////////////////////////////////
        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                au = new Add_User();
                au.addSysAdmin(systemAdmin);
                flag = 1;
            }
        });
////////////////////////////////////////////////////////////////////
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                du = new Delete_User();
                du.addSysAdmin(systemAdmin);
                flag = 2;
            }
        });
////////////////////////////////////////////////////////////////////
        addLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                al = new Add_Location();
                al.addSysAdmin(systemAdmin);
                flag = 3;
            }
        });
////////////////////////////////////////////////////////////////////
        deleteLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dl = new Delete_Location();
                dl.addSysAdmin(systemAdmin);
                flag = 4;
            }
        });
    }
////////////////////////////////////////////////////////////////////
    public void CredentialsConnected() {
            if (Credentials.connection()) {
                Tables.setEnabled(true);                           //enables the Tables JList
                Tables.setVisible(true);                           //shows the loadDataButton JButton
                dataTable.setEnabled(true);                        //enables the dataTable JTable
            }
    }
////////////////////////////////////////////////////////////////////
    public String getTableID() {                                   //changes the label above the table
        selectedTable = Tables.getSelectedValue().toString();      //
        Table.setText(selectedTable);
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
////////////////////////////////////////////////////////////////////
 /*   public void DBisConnected() {
        boolean DBConnected = false;
        if (DBHelp.dbConn()) {
            setVisible(false);
            DBConnected = true;
        }
        else
        {
            DBConnected = false;
        }
    }*/
///////////////////////////////////////////////////////////////////
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
    public void addUsers() {
        String[] addData = au.info();
        DBHelp dbconnect = new DBHelp();
        dbconnect.DBConnect();
        dbconnect.addUser(addData[0], addData[1], addData[2], addData[3], addData[4]);
        rSet = dbconnect.getData();
        dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
     }
///////////////////////////////////////////////////////////////////
    public void deleteUsers() {
        DBHelp dbconnect = new DBHelp();
        dbconnect.DBConnect();
        dbconnect.deleteUser(du.info());
        rSet = dbconnect.getData();
        dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
    }
///////////////////////////////////////////////////////////////////
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
    public void deleteLoc() {
        DBHelp dbconnect = new DBHelp();
        dbconnect.DBConnect();
        dbconnect.deleteLocation(dl.info());
        rSet = dbconnect.getData();
        dataTable.setModel(DbUtils.resultSetToTableModel(rSet));
    }
///////////////////////////////////////////////////////////////////


}
