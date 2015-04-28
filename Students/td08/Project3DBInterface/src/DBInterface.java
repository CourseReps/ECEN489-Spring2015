import java.util.ArrayList;

/**
 * Created by Ian on 4/25/2015.
 */
public class DBInterface {

    public static ArrayList<String> selectFriends(String username) {
        DBHelper dbHelper = new DBHelper();
        ArrayList<String> friends = new ArrayList<String>();

        dbHelper.openDB();
        friends = dbHelper.getFriends(dbHelper.getUserIDByName(username));
        dbHelper.closeDB();

        return friends;
    }

    public static ArrayList<UserWithLocations> getRecentLocs(String username, ArrayList<String> friends) {
        DBHelper dbHelper = new DBHelper();
        ArrayList<UserWithLocations> friendsWithLocs = new ArrayList<UserWithLocations>();
        ArrayList<CheckIn> checkIns;


        dbHelper.openDB();
        for (String f : friends) {
            checkIns = dbHelper.getCheckInByUser(dbHelper.getUserIDByName(f));

            ArrayList<String> locs = new ArrayList<String>();

            for (int i = 0; i < 3; i++)
                locs.add(checkIns.get(i).getLocationName());

            friendsWithLocs.add(new UserWithLocations(f, locs));
        }

        return friendsWithLocs;
    }

    //Added by Trevor 4/27/15
    public static String getSessionID(String username) {
        DBHelper dbHelper = new DBHelper();
        String sessionID = null;

        dbHelper.openDB();
        sessionID = dbHelper.getSessionIDByUserName(username);
        dbHelper.closeDB();

        return sessionID;
    }

    public static String getPassword(String username) {
        DBHelper dbHelper = new DBHelper();
        String pass = null;

        dbHelper.openDB();
        pass = dbHelper.getPassByUserName(username);
        dbHelper.closeDB();

        return pass;
    }

    public static void addSessionID(String username, String ID) {
        DBHelper dbHelper = new DBHelper();
        dbHelper.openDB();
        dbHelper.addSessionID(username, ID);
        dbHelper.closeDB();
    }

    public static void resetSessionID(String username) {
        DBHelper dbHelper = new DBHelper();
        dbHelper.openDB();
        dbHelper.resetSessionID(username);
        dbHelper.closeDB();
    }

    //End added by Trevor

}
