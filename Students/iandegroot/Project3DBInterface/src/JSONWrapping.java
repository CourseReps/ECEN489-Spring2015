/**
 * Created by Ian on 4/17/2015.
 */

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JSONWrapping {

    //Takes a string in JSON format and outputs a JSON object
    @SuppressWarnings("unchecked")
    static JSONObject stringToJSON(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = new JSONObject();

        try {
            json = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return json;
    }

    /*********** Client Wrapping ***********/

    //Takes in a username and a password and outputs a JSON object with this information
    @SuppressWarnings("unchecked")
    static JSONObject getLoginJSON(String username, String password) {
        JSONObject login = new JSONObject();
        JSONObject user = new JSONObject();

        user.put("username", username);
        user.put("password", password);

        login.put("login", user);

        return login;
    }

    //Takes in a username and a password and outputs a JSON object with this information
    @SuppressWarnings("unchecked")
    static JSONObject getLogoutJSON(String username, String sessID) {
        JSONObject logout = new JSONObject();
        JSONObject user = new JSONObject();

        user.put("username", username);
        user.put("sessionID", sessID);

        logout.put("logout", user);

        return logout;
    }

    //Takes in a username and a list of friends and outputs a JSON object with this information
    @SuppressWarnings("unchecked")
    static JSONObject getAddFriendsJSON(String username, ArrayList<String> friends, String sessID) {
        JSONObject addingFriends = new JSONObject();
        JSONObject user = new JSONObject();
        JSONArray jsonFriends = new JSONArray();

        user.put("username", username);
        user.put("sessionID", sessID);

        for (String f : friends)
            jsonFriends.add(f);

        user.put("friends", jsonFriends);

        addingFriends.put("addFriends", user);

        return addingFriends;
    }

    //Takes in a username, location, time, and method and outputs the JSON object used to insert a new check in
    @SuppressWarnings("unchecked")
    static JSONObject getCheckInJSON(String user, String loc, int timeStamp, String method, String sessID) {
        JSONObject checkIn = new JSONObject();
        JSONObject checkInData = new JSONObject();

        checkInData.put("username", user);
        checkInData.put("method", method);
        checkInData.put("time", timeStamp);
        checkInData.put("location", loc);
        checkInData.put("sessionID", sessID);

        checkIn.put("checkIn", checkInData);

        return checkIn;
    }

    //Takes in a username and location and outputs the JSON object used to get that user's last 5 friends at that location
    @SuppressWarnings("unchecked")
    static JSONObject getRecentFriendsJSON(String user, ArrayList<String> locs, String sessID) {
        JSONObject recentFriends = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray jsonLocs = new JSONArray();

        data.put("username", user);
        data.put("sessionID", sessID);

        for (String l : locs)
            jsonLocs.add(l);

        data.put("locations", jsonLocs);

        recentFriends.put("recentFriends", data);

        return recentFriends;
    }

    //Takes in a username and a list of friends and outputs the JSON object used to get those friend's 5 recent locations
    @SuppressWarnings("unchecked")
    static JSONObject getRecentLocsJSON(String user, ArrayList<String> friends, String sessID) {
        JSONObject recentLocs = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray jsonFriends = new JSONArray();

        data.put("username", user);
        data.put("sessionID", sessID);

        for (String f : friends)
            jsonFriends.add(f);

        data.put("friends", jsonFriends);

        recentLocs.put("recentLocs", data);

        return recentLocs;
    }

    //Takes in a username and outputs the JSON object used to get that user's friends
    @SuppressWarnings("unchecked")
    static JSONObject getSelectFriendsJSON(String user, String sessID) {
        JSONObject friends = new JSONObject();
        JSONObject data = new JSONObject();

        data.put("username", user);
        data.put("sessionID", sessID);

        friends.put("selectFriends", data);
        return friends;
    }

    /*********** Server Wrapping ***********/

    //Takes in a username and a password and outputs a JSON object with this information
    @SuppressWarnings("unchecked")
    static JSONObject getLoginOutcomeJSON(boolean outcome, String sessID) {
        JSONObject loginOutcome = new JSONObject();
        JSONObject user = new JSONObject();

        if (outcome)
            user.put("outcome", "success");
        else
            user.put("outcome", "failure");

        user.put("sessionID", sessID);

        loginOutcome.put("loginOutcome", user);

        return loginOutcome;
    }

    //Takes in a boolean value and outputs an insert outcome JSON object
    //True -> success
    //False -> failure
    @SuppressWarnings("unchecked")
    static JSONObject getOutcomeJSON(boolean outcome) {
        JSONObject reply = new JSONObject();

        if (outcome)
            reply.put("outcome", "success");
        else
            reply.put("outcome", "failure");
        return reply;
    }

    /*//Takes in a list of locations and and list of friends and outputs a JSON object with 5 most recent friends for each location
    @SuppressWarnings("unchecked")
    static JSONObject getLocsRecentFriendsJSON(ArrayList<String> locs, ArrayList<ArrayList<String>> friends) {
        JSONObject recentFriends = new JSONObject();
        JSONArray locations = new JSONArray();

        for (int i = 0; i < locs.size(); i++) {
            JSONArray jsonfriends = new JSONArray();
            JSONObject location = new JSONObject();

            for (int j = 0; j < friends.size(); j++) {
                jsonfriends.add(friends.get(i).get(j));
            }

            location.put("location", locs.get(i));
            location.put("friends", jsonfriends);

            locations.add(location);
        }

        recentFriends.put("recentFriends", locations);

        return recentFriends;
    }*/

    //Takes in a list of locations and and list of friends and outputs a JSON object with 5 most recent friends for each location
    @SuppressWarnings("unchecked")
    static JSONObject getLocsRecentFriendsJSON(ArrayList<LocWithCheckIns> locsWithCheckIns) {
        JSONObject recentFriends = new JSONObject();
        JSONArray locations = new JSONArray();

        for (int i = 0; i < locsWithCheckIns.size(); i++) {
            JSONArray jsonfriends = new JSONArray();
            JSONObject location = new JSONObject();

            ArrayList<CheckInData> temps = new ArrayList<CheckInData>(locsWithCheckIns.get(i).checkIns);

            for (int j = 0; j < temps.size(); j++) {
                jsonfriends.add(temps.get(j));
            }

            location.put("location", locsWithCheckIns.get(i).name);
            location.put("friends", jsonfriends);

            locations.add(location);
        }

        recentFriends.put("recentFriends", locations);

        return recentFriends;
    }

    /*//Takes in a list of usernames and a list of locations and outputs a JSON object with the 5 most recent locations for each friend
    @SuppressWarnings("unchecked")
    static JSONObject getRecentLocsJSON(ArrayList<UserWithLocations> usersWithLocs) {
        JSONObject friendsLocations = new JSONObject();
        JSONArray jsonfriends = new JSONArray();

        for (int i = 0; i < usersWithLocs.size(); i++) {
            JSONArray locations = new JSONArray();
            JSONObject friend = new JSONObject();

            ArrayList<String> locs = new ArrayList<String>(usersWithLocs.get(i).locations);

            for (int j = 0; j < locs.size(); j++) {
                locations.add(locs.get(j));
            }

            friend.put("username", usersWithLocs.get(i).username);
            friend.put("locations", locations);

            jsonfriends.add(friend);
        }

        friendsLocations.put("recentLocs", jsonfriends);

        return friendsLocations;
    }*/

    //Takes in a list of usernames and a list of locations and outputs a JSON object with the 5 most recent locations for each friend
    @SuppressWarnings("unchecked")
    static JSONObject getReturnRecentLocsJSON(ArrayList<UserWithCheckIns> usersWithCheckIns) {
        JSONObject friendsLocations = new JSONObject();
        JSONArray jsonfriends = new JSONArray();

        for (int i = 0; i < usersWithCheckIns.size(); i++) {
            JSONArray locations = new JSONArray();
            JSONObject friend = new JSONObject();

            ArrayList<CheckInData> locs = new ArrayList<CheckInData>(usersWithCheckIns.get(i).checkIns);

            for (int j = 0; j < locs.size(); j++) {
                locations.add(locs.get(j));
            }

            friend.put("username", usersWithCheckIns.get(i).username);
            friend.put("locations", locations);

            jsonfriends.add(friend);
        }

        friendsLocations.put("recentLocs", jsonfriends);

        return friendsLocations;
    }

    //Takes in a list of friends and outputs a JSON object
    @SuppressWarnings("unchecked")
    static JSONObject getFriendsJSON(ArrayList<String> friends){
        JSONObject data = new JSONObject();
        JSONArray jsonFriends = new JSONArray();

        for (String f : friends)
            jsonFriends.add(f);

        data.put("friends", jsonFriends);

        return data;
    }

    /*********** Client Unwrapping ***********/
/*
    static ArrayList<Location> unwrapRecentFriends(JSONArray jsonlocations) {
        JSONObject data;
        JSONArray jsonfriends;
        ArrayList<String> friends = new ArrayList<String>();
        ArrayList<Location> locs = new ArrayList<Location>();

        for(int i = 0; i < jsonlocations.size(); i++) {
            data = (JSONObject)jsonlocations.get(i);

            jsonfriends = (JSONArray)data.get("friends");

            for (int j = 0; j < jsonfriends.size(); j++)
                friends.add(jsonfriends.get(j));

            ArrayList<String> temp = new ArrayList<String>(friends);

            locs.add(new Location((String)data.get("location"), temp));

            friends.clear();
        }

        return locs;
    }

    static ArrayList<Friend> unwrapRecentLocs(JSONArray jsonfriendsWithLocs) {
        JSONObject data;
        JSONArray jsonlocs;
        ArrayList<String> locs = new ArrayList<String>();
        ArrayList<Friend> friends = new ArrayList<Friend>();

        for(int i = 0; i < jsonfriendsWithLocs.size(); i++) {
            data = (JSONObject)jsonfriendsWithLocs.get(i);

            jsonlocs = (JSONArray)data.get("locations");

            for (int j = 0; j < jsonlocs.size(); j++)
                locs.add((String)jsonlocs.get(j));

            ArrayList<String> temp = new ArrayList<String>(locs);

            friends.add(new Friend((String)data.get("username"), temp));

            locs.clear();
        }

        return friends;
    }*/

    static ArrayList<LocWithCheckIns> unwrapRecentFriends(JSONArray jsonlocations) {
        JSONObject data;
        JSONArray jsonfriends;
        ArrayList<CheckInData> friends = new ArrayList<CheckInData>();
        ArrayList<LocWithCheckIns> locs = new ArrayList<LocWithCheckIns>();

        for(int i = 0; i < jsonlocations.size(); i++) {
            data = (JSONObject)jsonlocations.get(i);

            jsonfriends = (JSONArray)data.get("friends");

            for (int j = 0; j < jsonfriends.size(); j++)
                friends.add((CheckInData)jsonfriends.get(j));

            ArrayList<CheckInData> temp = new ArrayList<CheckInData>(friends);

            locs.add(new LocWithCheckIns((String)data.get("location"), temp));

            friends.clear();
        }

        return locs;
    }

    static ArrayList<UserWithCheckIns> unwrapRecentLocs(JSONArray jsonfriendsWithLocs) {
        JSONObject data;
        JSONArray jsonlocs;
        ArrayList<CheckInData> locs = new ArrayList<CheckInData>();
        ArrayList<UserWithCheckIns> friends = new ArrayList<UserWithCheckIns>();

        for(int i = 0; i < jsonfriendsWithLocs.size(); i++) {
            data = (JSONObject)jsonfriendsWithLocs.get(i);

            jsonlocs = (JSONArray)data.get("locations");

            for (int j = 0; j < jsonlocs.size(); j++)
                locs.add((CheckInData)jsonlocs.get(j));

            ArrayList<CheckInData> temp = new ArrayList<CheckInData>(locs);

            friends.add(new UserWithCheckIns((String)data.get("username"), temp));

            locs.clear();
        }

        return friends;
    }

    static ArrayList<String> unwrapFriends(JSONArray jsonfriends) {
        JSONObject data;
        ArrayList<String> friends = new ArrayList<String>();

        for(int i = 0; i < jsonfriends.size(); i++)
            friends.add((String)jsonfriends.get(i));

        return friends;
    }

    static void clientUnwrapping(JSONObject jsonResponse) {
        String response, sessionID;
        JSONArray locations;
        JSONArray friendsWithLocs;
        JSONArray friendsArray;
        JSONObject loginOutcome;

        ArrayList<Location> locArray;
        ArrayList<LocWithCheckIns> testLocArray;
        ArrayList<Friend> friendArray;
        ArrayList<String> sFriendArray;
        ArrayList<UserWithCheckIns> testFriendArray;

        if ((loginOutcome = (JSONObject)jsonResponse.get("loginOutcome")) != null) {
                response = (String)loginOutcome.get("outcome");
                sessionID = (String)loginOutcome.get("sessionID");

                System.out.println(response + " " + sessionID + "\n");
        }
        else if ((response = (String)jsonResponse.get("outcome")) != null) {
            if (response.equals("success"))
                System.out.println("Insert was successful\n");
            else
                System.out.println("Insert failed\n");
        }
        else if ((locations = (JSONArray)jsonResponse.get("recentFriends")) != null) {
            testLocArray = unwrapRecentFriends(locations);
            System.out.println(testLocArray + "\n");
        }
        else if ((friendsWithLocs = (JSONArray)jsonResponse.get("recentLocs")) != null) {
            testFriendArray = unwrapRecentLocs(friendsWithLocs);
            System.out.println(testFriendArray + "\n");
        }
        else if ((friendsArray = (JSONArray)jsonResponse.get("friends")) != null) {
            sFriendArray = unwrapFriends(friendsArray);
            System.out.println(sFriendArray + "\n");
        }

    }

    /*********** Server Unwrapping ***********/

    static UserWithFriends unwrapAddFriends(JSONObject jsonFriendsToAdd) {
        String user;
        JSONArray jsonfriends = new JSONArray();
        ArrayList<String> friends = new ArrayList<String>();

        user = (String)jsonFriendsToAdd.get("username");

        jsonfriends = (JSONArray)jsonFriendsToAdd.get("friends");

        for (int i = 0; i < jsonfriends.size(); i++)
            friends.add((String)jsonfriends.get(i));

        return (new UserWithFriends(user, friends));
    }

    static CheckInData unwrapCheckIn(JSONObject data) {
        String method, loc, username;
        int time;

        method = (String)data.get("method");
        loc = (String)data.get("location");
        username = (String)data.get("username");
        time = (Integer)data.get("time");

        return (new CheckInData(method, loc, username, time));
    }

    static UserWithLocations unwrapRecentFriends(JSONObject jsonRecentFriends) {
        String user;
        JSONArray jsonlocs = new JSONArray();
        ArrayList<String> locs = new ArrayList<String>();

        user = (String)jsonRecentFriends.get("username");

        jsonlocs = (JSONArray)jsonRecentFriends.get("locations");

        for (int i = 0; i < jsonlocs.size(); i++) {
            locs.add((String)jsonlocs.get(i));
        }

        return (new UserWithLocations(user, locs));
    }

    static UserWithFriends unwrapRecentLocs(JSONObject jsonRecentLocs) {
        String user;
        JSONArray jsonfriends = new JSONArray();
        ArrayList<String> friends = new ArrayList<String>();

        user = (String)jsonRecentLocs.get("username");

        jsonfriends = (JSONArray)jsonRecentLocs.get("friends");

        for (int i = 0; i < jsonfriends.size(); i++) {
            friends.add((String)jsonfriends.get(i));
        }

        return (new UserWithFriends(user, friends));
    }

    static JSONObject serverUnwrapping(JSONObject jsonRequest) {
        String selectFriendsUser, sessionID;
        String newUser, newUserPassword;
        String logoutUser;
        JSONObject jsonlogin;
        JSONObject jsonlogout;
        JSONObject friendsToAdd;
        JSONObject jsonCheckInData;
        JSONObject recentFriends;
        JSONObject recentLocations;
        JSONObject jsonSelectFriends;
        JSONObject response;

        UserWithFriends uwf;
        CheckInData cid;
        UserWithLocations uwl;

        response = null;


        if ((jsonlogin = (JSONObject)jsonRequest.get("login")) != null) {
            newUser = (String)jsonlogin.get("username");
            newUserPassword = (String)jsonlogin.get("password");

            System.out.println(newUser + " " + newUserPassword + "\n");
        }
        else if ((jsonlogout = (JSONObject)jsonRequest.get("logout")) != null) {
            logoutUser = (String)jsonlogout.get("username");
            sessionID = (String)jsonlogout.get("sessionID");

            System.out.println(logoutUser + " " + sessionID + "\n");
        }
        else if ((friendsToAdd = (JSONObject)jsonRequest.get("addFriends")) != null) {
            uwf = unwrapAddFriends(friendsToAdd);

            System.out.println(uwf + "\n");
        }
        else if ((jsonCheckInData = (JSONObject)jsonRequest.get("checkIn")) != null) {
            cid = unwrapCheckIn(jsonCheckInData);
            System.out.println(cid + "\n");
        }
        else if ((recentFriends = (JSONObject)jsonRequest.get("recentFriends")) != null) {
            uwl = unwrapRecentFriends(recentFriends);
            System.out.println(uwl + "\n");
        }
        else if ((recentLocations = (JSONObject)jsonRequest.get("recentLocs")) != null) {
            uwf = unwrapRecentLocs(recentLocations);
            //response = getReturnRecentLocsJSON(DBInterface.getRecentLocs(uwf.username, uwf.friends));
            System.out.println(uwf + "\n");
        }
        else if ((jsonSelectFriends = (JSONObject)jsonRequest.get("selectFriends")) != null) {
            //Get friends based on username
            //response = getFriendsJSON(DBInterface.selectFriends(selectFriendsUser));
            sessionID = (String)jsonSelectFriends.get("sessionID");
            selectFriendsUser = (String)jsonSelectFriends.get("username");
            System.out.println(selectFriendsUser + " " + sessionID + "\n");
        }

        return response;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args)
    {
        JSONObject result = new JSONObject();

        ArrayList<String> testFriends = new ArrayList<String>();
        ArrayList<ArrayList<String>> testFriendsArray = new ArrayList<ArrayList<String>>();
        ArrayList<String> testLocs = new ArrayList<String>();
        ArrayList<ArrayList<String>> testLocsArray = new ArrayList<ArrayList<String>>();
        ArrayList<UserWithLocations> usersWithLocs = new ArrayList<UserWithLocations>();
        ArrayList<UserWithCheckIns> usersWithCheckIns = new ArrayList<UserWithCheckIns>();
        ArrayList<LocWithCheckIns> locsWithCheckIns = new ArrayList<LocWithCheckIns>();
        ArrayList<CheckInData> checkIns = new ArrayList<CheckInData>();

        testFriends.add("Alice");
        testFriends.add("Bob");
        testFriends.add("Charlie");
        testFriends.add("David");
        testFriends.add("Elliot");

        testLocs.add("EIC");
        testLocs.add("ZachShack");
        testLocs.add("Bright");
        testLocs.add("MSC");
        testLocs.add("Rudder");

        testFriendsArray.add(testFriends);
        testFriendsArray.add(testFriends);
        testFriendsArray.add(testFriends);
        testFriendsArray.add(testFriends);
        testFriendsArray.add(testFriends);

        testLocsArray.add(testLocs);
        testLocsArray.add(testLocs);
        testLocsArray.add(testLocs);
        testLocsArray.add(testLocs);
        testLocsArray.add(testLocs);

        checkIns.add(new CheckInData("openCV", "EIC", "Alice", 1000));
        checkIns.add(new CheckInData("openCV", "ZachShack", "Bob", 1001));
        checkIns.add(new CheckInData("openCV", "Bright", "Charlie", 1002));
        checkIns.add(new CheckInData("openCV", "MSC", "David", 1003));
        checkIns.add(new CheckInData("openCV", "Rudder", "Elliot", 1004));

        usersWithLocs.add(new UserWithLocations("Alice", testLocs));
        usersWithLocs.add(new UserWithLocations("Bob", testLocs));
        usersWithLocs.add(new UserWithLocations("Charlie", testLocs));
        usersWithLocs.add(new UserWithLocations("David", testLocs));
        usersWithLocs.add(new UserWithLocations("Elliot", testLocs));

        usersWithCheckIns.add(new UserWithCheckIns("Alice", checkIns));
        usersWithCheckIns.add(new UserWithCheckIns("Bob", checkIns));
        usersWithCheckIns.add(new UserWithCheckIns("Charlie", checkIns));
        usersWithCheckIns.add(new UserWithCheckIns("David", checkIns));
        usersWithCheckIns.add(new UserWithCheckIns("Elliot", checkIns));

        locsWithCheckIns.add(new LocWithCheckIns("EIC", checkIns));
        locsWithCheckIns.add(new LocWithCheckIns("ZachShack", checkIns));
        locsWithCheckIns.add(new LocWithCheckIns("Bright", checkIns));
        locsWithCheckIns.add(new LocWithCheckIns("MSC", checkIns));
        locsWithCheckIns.add(new LocWithCheckIns("Rudder", checkIns));

        /*********** Test Wrapping and Unwrapping ***********/

        result = getLoginJSON("Bob", "pass");

        System.out.println(result);

        serverUnwrapping(result);

        result = getLogoutJSON("Bob", "asdf");

        System.out.println(result);

        serverUnwrapping(result);

        result = getAddFriendsJSON("Bob", testFriends, "asdf");

        System.out.println(result);

        serverUnwrapping(result);

        result = getCheckInJSON("Bob", "EIC", 100000000, "openCV", "asdf");

        System.out.println(result);

        serverUnwrapping(result);

        result = getRecentFriendsJSON("Bob", testLocs, "asdf");

        System.out.println(result);

        serverUnwrapping(result);

        result = getRecentLocsJSON("Bob", testFriends, "asdf");

        System.out.println(result);

        serverUnwrapping(result);

        result = getSelectFriendsJSON("Bob", "asdf");

        System.out.println(result);

        serverUnwrapping(result);

        result = getLoginOutcomeJSON(true, "asdf");

        System.out.println(result);

        clientUnwrapping(result);

        result = getOutcomeJSON(true);

        System.out.println(result);

        clientUnwrapping(result);

        result = getLocsRecentFriendsJSON(locsWithCheckIns);

        System.out.println(result);

        clientUnwrapping(result);

        result = getReturnRecentLocsJSON(usersWithCheckIns);

        System.out.println(result);

        clientUnwrapping(result);

        result = getFriendsJSON(testFriends);

        System.out.println(result);

        clientUnwrapping(result);
    }

}
