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
        dbHelper.addUser("Mandel", "Mandel", "asdf", "Password", "SALT");
        dbHelper.addUser("Trevor", "Trevor", "sdfg", "Password", "SALT");
        dbHelper.addUser("Benito", "Benito", "qewr", "Password", "SALT");
        dbHelper.addUser("Josh", "Josh", "zxcv", "Password", "SALT");
        dbHelper.addUser("Blade", "Blade", "wert", "Password", "SALT");
        dbHelper.addLocation("Home", 12.567888, 12.567, "Image");
        dbHelper.addLocation("School", 12.567888, 12.567, "Image");
        dbHelper.addLocation("EIC", 12.567888, 12.567, "Image");
        dbHelper.addLocation("Rudder", 12.567888, 12.567, "Image");
        dbHelper.addLocation("Zachary", 12.567888, 12.567, "Image");
        int mandelId = dbHelper.getUserIDByName("Mandel");
        int trevorId = dbHelper.getUserIDByName("Trevor");
        int benitoId = dbHelper.getUserIDByName("Benito");
        int joshId = dbHelper.getUserIDByName("Josh");
        int bladeId = dbHelper.getUserIDByName("Blade");

        dbHelper.addFriend(mandelId,trevorId);
        dbHelper.addFriend(mandelId,benitoId);
        dbHelper.addFriend(mandelId,joshId);
        dbHelper.addFriend(mandelId,bladeId);

        dbHelper.addFriend(trevorId,benitoId);
        dbHelper.addFriend(trevorId,joshId);
        dbHelper.addFriend(trevorId,bladeId);

        dbHelper.addFriend(benitoId,joshId);
        dbHelper.addFriend(benitoId,bladeId);

        dbHelper.addFriend(joshId,bladeId);


        ArrayList<String> mandelfriends = dbHelper.getFriends(mandelId);

        System.out.println("Mandel's friends: " + mandelfriends.toString() + "\n");

        dbHelper.checkIn(dbHelper.getUserIDByName("Mandel"), dbHelper.getLocationIDByName("Home"), 100000001, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Mandel"), dbHelper.getLocationIDByName("School"), 100000002, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Mandel"), dbHelper.getLocationIDByName("EIC"), 100000003, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Mandel"), dbHelper.getLocationIDByName("Rudder"), 100000004, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Mandel"), dbHelper.getLocationIDByName("Zachary"), 100000005, "Method");

        dbHelper.checkIn(dbHelper.getUserIDByName("Benito"), dbHelper.getLocationIDByName("Home"), 110000001, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Benito"), dbHelper.getLocationIDByName("School"), 110000002, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Benito"), dbHelper.getLocationIDByName("EIC"), 110000003, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Benito"), dbHelper.getLocationIDByName("Rudder"), 110000004, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Benito"), dbHelper.getLocationIDByName("Zachary"), 110000005, "Method");

        dbHelper.checkIn(dbHelper.getUserIDByName("Trevor"), dbHelper.getLocationIDByName("Home"), 111000001, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Trevor"), dbHelper.getLocationIDByName("School"), 111000002, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Trevor"), dbHelper.getLocationIDByName("EIC"), 111000003, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Trevor"), dbHelper.getLocationIDByName("Rudder"), 111000004, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Trevor"), dbHelper.getLocationIDByName("Zachary"), 111000005, "Method");

        dbHelper.checkIn(dbHelper.getUserIDByName("Josh"), dbHelper.getLocationIDByName("Home"), 111100001, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Josh"), dbHelper.getLocationIDByName("School"), 111100002, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Josh"), dbHelper.getLocationIDByName("EIC"), 111100003, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Josh"), dbHelper.getLocationIDByName("Rudder"), 111100004, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Josh"), dbHelper.getLocationIDByName("Zachary"), 111100005, "Method");

        dbHelper.checkIn(dbHelper.getUserIDByName("Blade"), dbHelper.getLocationIDByName("Home"), 111110001, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Blade"), dbHelper.getLocationIDByName("School"), 111110002, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Blade"), dbHelper.getLocationIDByName("EIC"), 111110003, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Blade"), dbHelper.getLocationIDByName("Rudder"), 111110004, "Method");
        dbHelper.checkIn(dbHelper.getUserIDByName("Blade"), dbHelper.getLocationIDByName("Zachary"), 111110005, "Method");


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
        System.out.println("Mandel's friends: " + mandelfriends.toString());
        //dbHelper.deleteLocation(dbHelper.getLocationIDByName("Home"));
        //dbHelper.deleteUser(trevorId);
        
        dbHelper.closeDB();
    }
}
