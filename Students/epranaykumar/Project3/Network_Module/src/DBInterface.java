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
        ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();

        dbHelper.openDB();
        for (String f : friends) {
            checkIns = dbHelper.getCheckInByUser(dbHelper.getUserIDByName(f));
            //friendsWithLocs.add(new UserWithLocations(f, ))
        }

        return friendsWithLocs;
    }

}
