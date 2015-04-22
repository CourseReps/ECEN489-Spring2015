import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by mandel on 4/15/15.
 */
public class Main {
    public static void main(String args[]){
        DBHelper dbHelper = new DBHelper();
        ArrayList<CheckIn> checkIns1 = new ArrayList<CheckIn>();
        ArrayList<CheckIn> checkIns2 = new ArrayList<CheckIn>();
        dbHelper.openDB();
        dbHelper.createDB();
        dbHelper.addUser("Mandel", "Password", "SALT");
        dbHelper.addUser("Trevor", "Password", "SALT");
        dbHelper.addUser("Benito", "Password", "SALT");
        dbHelper.addUser("Chamberland", "Password", "SALT");
        dbHelper.addUser("Ian", "Password", "SALT");
        dbHelper.addLocation("Home", 12.567888, 12.567, "Image");
        dbHelper.addLocation("School", 12.567888, 12.567, "Image");
        int mandelId = dbHelper.getUserIDByName("Mandel");
        int trevorId = dbHelper.getUserIDByName("Trevor");
        int benitoId = dbHelper.getUserIDByName("Benito");
        int chamberlandID = dbHelper.getUserIDByName("Chamberland");
        int ianID = dbHelper.getUserIDByName("Ian");
        dbHelper.addFriend(mandelId,ianID);
        dbHelper.addFriend(mandelId,trevorId);
        dbHelper.addFriend(mandelId,benitoId);
        ArrayList<String> mandelfriends = dbHelper.getFriends(mandelId);
        System.out.println(mandelfriends.toString());
        System.out.println();
        dbHelper.checkIn(dbHelper.getUserIDByName("Mandel"), dbHelper.getLocationIDByName("Home"), 123456789, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Benito"), dbHelper.getLocationIDByName("Home"), 123456799, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Mandel"), dbHelper.getLocationIDByName("School"), 123456709, "Method");
        checkIns1 = dbHelper.getCheckInByLocation(dbHelper.getLocationIDByName("Home"));
        checkIns2 = dbHelper.getCheckInByUser(dbHelper.getUserIDByName("Mandel"));
        for(CheckIn ci : checkIns1){
            System.out.println("["+ci.getUserName()+","+ci.getLocationName()+"]");
        }
        System.out.println();
        for(CheckIn ci : checkIns2){
            System.out.println("["+ci.getUserName()+","+ci.getLocationName()+"]");
        }
        dbHelper.deleteFriend(mandelId,trevorId);
        mandelfriends = dbHelper.getFriends(mandelId);
        System.out.println(mandelfriends.toString());
        dbHelper.deleteLocation(dbHelper.getLocationIDByName("Home"));
        dbHelper.deleteUser(trevorId);
        
        dbHelper.closeDB();
        

        
    }
}
