import java.util.ArrayList;

/**
 * Created by Ian on 4/25/2015.
 */
public class DBInterface {

    public static ArrayList<String> selectFriends(String username) {
        DBHelper dbHelper = new DBHelper();
        ArrayList<String> friends = new ArrayList<String>();

        dbHelper.openDB();
        friends = dbHelper.getFriends(dbHelper.getUserIDByUserName(username));
        dbHelper.closeDB();

        return friends;
    }

    public static ArrayList<UserWithLocations> getRecentLocs(String username, ArrayList<String> friends) {
        DBHelper dbHelper = new DBHelper();
        ArrayList<UserWithLocations> friendsWithLocs = new ArrayList<UserWithLocations>();
        ArrayList<CheckIn> checkIns;


        dbHelper.openDB();
        for (String f : friends) {
            checkIns = dbHelper.getCheckInByUser(dbHelper.getUserIDByUserName(f));

            ArrayList<String> locs = new ArrayList<String>();

            for (int i = 0; i < 3; i++)
                locs.add(checkIns.get(i).getLocationName());

            friendsWithLocs.add(new UserWithLocations(f, locs));
        }

        return friendsWithLocs;
    }

    // Updated by Nagaraj on 4/28/2015

    public static Boolean AddFriends(UserWithFriends userWithFriends){
        Boolean outcome = false;  // default
        DBHelper dbHelper = new DBHelper();
        Integer userID = dbHelper.getUserIDByUserName(userWithFriends.username);
        dbHelper.openDB();
        for (String friendName : userWithFriends.friends){
            Integer friendUserID = dbHelper.getUserIDByName(friendName);
            dbHelper.addFriend(userID,friendUserID);
            dbHelper.addFriend(friendUserID,userID);
        }
        outcome =true;

        return outcome;
    }

}