package com.slscomm;


import com.slscomm.HashMachine;
import com.slscomm.JSONWrapping;
import com.slscomm.ws.CommHandler;
import com.slscomm.ws.CommHandlerImplService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Nagaraj on 4/28/2015.
 */

public class CheckInClient {

    public static void main(String[] args) throws IOException {
        try {
            CommHandlerImplService commHandlerImplService = new CommHandlerImplService();
            CommHandler commHandler = commHandlerImplService.getCommHandlerImplPort();
            String sessionID=null;

            System.out.println("connected to Server.");


            String password= HashMachine.generateUnsaltedUserHash("password");

            System.out.println("sent Mandel's login info");
          String json_string = JSONWrapping.getLoginJSON("joshua.cano18@gmail.com", password).toJSONString();


            String line = commHandler.getServerResponse(json_string);
            JSONObject jsonResponse = (JSONObject) JSONValue.parse(line);
            // Unwrapping
            JSONObject loginOutcome = new JSONObject();
            String response=null;
            if ((loginOutcome = (JSONObject)jsonResponse.get("loginOutcome")) != null) {
                response = (String)loginOutcome.get("outcome");
                sessionID = (String)loginOutcome.get("sessionID");
                System.out.println(response + " " + sessionID + "\n");
            }
            if(response.equals("success")){
                System.out.println("Login Successful \n SessionID:"+sessionID);
            }
            else {
                System.out.println("Login Failure");
            }

            String[] userlist = {"Trevor", "Mandel", "Benito", "Josh", "Blade"};
            ArrayList<String> friends = new ArrayList<String>();
            friends.add("Nagaraj Thenkarai Janakiraman");
//            friends.add("bladeroybal@gmail.com");

            ArrayList<String> locs = new ArrayList<String>();
            locs.add("EIC");
            locs.add("Zachary");

            JSONObject friend_json= JSONWrapping.getAddFriendsJSON("joshua.cano18@gmail.com", friends, sessionID);


            System.out.println(friend_json.toString());

            System.out.println("Waiting for Mandel's addfriends response");
            line = commHandler.getServerResponse(friend_json.toString());
            JSONObject resp_friendslist = (JSONObject) JSONValue.parse(line);


            System.out.println("unwrapping Mandel's addfriends response");
            JSONWrapping.clientUnwrapping(resp_friendslist);



//            System.out.println("Sent Mandel's Check-in info ");
//
//            System.out.println("Waiting for Mandel's Check-in response");
//            line = commHandler.getServerResponse(JSONWrapping.getCheckInJSON("joshua.cano18@gmail.com", "EIC", (int)System.currentTimeMillis(), "OpenCV", sessionID).toString());
//            JSONObject resp_checkin = (JSONObject) JSONValue.parse(line);
//
//            System.out.println("unwrapping Mandel's Check-in response");
//            JSONWrapping.clientUnwrapping(resp_checkin);
//
//
//            /***** Select Friends Request *****/
//
//            System.out.println("Sent Mandel's select friends request ");
//
//            System.out.println("Waiting for Mandel's select friends request");
//            line = commHandler.getServerResponse(JSONWrapping.getSelectFriendsJSON("joshua.cano18@gmail.com", sessionID).toString());
//            JSONObject select_friends_resp = JSONWrapping.stringToJSON(line);
//
//            System.out.println(select_friends_resp);
//
//            System.out.println("unwrapping Mandel's select friends request");
//            //JSONWrapping.clientUnwrapping(resp_checkin);
//
//            JSONArray friendsArray;
//            ArrayList<String> sFriendArray;
//
//            if ((friendsArray = (JSONArray)select_friends_resp.get("friends")) != null) {
//                sFriendArray = JSONWrapping.unwrapFriends(friendsArray);
//                System.out.println(sFriendArray + "\n");
//            }
//
//            /***** Get Recent Locations *****/
//
//            System.out.println("Sent Mandel's recent locations request");
//
//            System.out.println("Waiting for Mandel's recent locations request");
//            line = commHandler.getServerResponse(JSONWrapping.getRecentLocsJSON("joshua.cano18@gmail.com", friends, sessionID).toString());
//            System.out.println(line);
//            JSONObject recent_locs_resp = JSONWrapping.stringToJSON(line);
//
//            System.out.println(recent_locs_resp);
//
//            JSONArray friendsWithLocs;
//            ArrayList<UserWithCheckIns> testFriendArray;
//
//            System.out.println("unwrapping Mandel's select friends request");
//            if ((friendsWithLocs = (JSONArray)recent_locs_resp.get("recentLocs")) != null) {
//                testFriendArray = JSONWrapping.unwrapRecentLocs(friendsWithLocs);
//                System.out.println(testFriendArray + "\n");
//            }
//            else
//                System.out.println("Did not get recent locations");
//
//            /***** Get Recent Friends *****/
//
//            System.out.println("Sent Mandel's recent friends request");
//
//            System.out.println("Waiting for Mandel's recent friends request");
//            line = commHandler.getServerResponse(JSONWrapping.getRecentFriendsJSON("joshua.cano18@gmail.com", locs, sessionID).toString());
//            System.out.println(line);
//            JSONObject recent_friends_resp = JSONWrapping.stringToJSON(line);
//
//            System.out.println(recent_friends_resp);
//
//            JSONArray locations;
//            ArrayList<LocWithCheckIns> testLocArray;
//
//            System.out.println("unwrapping Mandel's select friends request");
//            if ((locations = (JSONArray)recent_friends_resp.get("recentFriends")) != null) {
//                testLocArray = JSONWrapping.unwrapRecentFriends(locations);
//                System.out.println(testLocArray + "\n");
//            }
//            else
//                System.out.println("Did not get recent friends");


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

// for getfriends Querry

/*            for(String user: userlist) {

       // Sending Querry

                JSONObject friends = getSelectFriendsJSON(user);
                System.out.println("created the " + user + " getfriends query");

                wr_to_server.println(friends.toString());
                wr_to_server.flush();
                System.out.println("sent the " + user + " getfriends query");


        // receiving Response

                System.out.println("Waiting for the " + user + " getfriends response");
                String line = input.readLine();
                JSONObject resp_friendslist = (JSONObject) JSONValue.parse(line);

                System.out.println("unwrapping the " + user + " getfriends response");
                clientUnwrapping(resp_friendslist);
            }

            wr_to_server.println("disconnect");
            wr_to_server.flush();

            clientSocket.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        }

    @SuppressWarnings("unchecked")
    static JSONObject getSelectFriendsJSON(String user) {
        JSONObject friends = new JSONObject();

        friends.put("selectFriends", user);
        return friends;
    }

// unwrapping Response

    static ArrayList<String> unwrapFriends(JSONArray jsonfriends) {
        JSONObject data;
        ArrayList<String> friends = new ArrayList<String>();

        for(int i = 0; i < jsonfriends.size(); i++)
            friends.add((String)jsonfriends.get(i));

        return friends;
    }

    static ArrayList<Location> unwrapRecentFriends(JSONArray jsonlocations) {
        JSONObject data;
        JSONArray jsonfriends;
        ArrayList<String> friends = new ArrayList<String>();
        ArrayList<Location> locs = new ArrayList<Location>();

        for(int i = 0; i < jsonlocations.size(); i++) {
            data = (JSONObject)jsonlocations.get(i);

            jsonfriends = (JSONArray)data.get("friends");

            for (int j = 0; j < jsonfriends.size(); j++)
                friends.add((String)jsonfriends.get(j));

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
    }

    // unwrapping Main Function
    static void clientUnwrapping(JSONObject jsonResponse) {
        String response;
        JSONArray locations;
        JSONArray friendsWithLocs;
        JSONArray friendsArray;

        ArrayList<Location> locArray;
        ArrayList<Friend> friendArray;
        ArrayList<String> sFriendArray;

        if ((response = (String)jsonResponse.get("outcome")) != null) {
            if (response.equals("success"))
                System.out.println("Insert was successful\n");
            else
                System.out.println("Insert failed\n");
        }
        else if ((locations = (JSONArray)jsonResponse.get("recentFriends")) != null) {
            locArray = unwrapRecentFriends(locations);
            System.out.println(locArray + "\n");
        }
        else if ((friendsWithLocs = (JSONArray)jsonResponse.get("recentLocs")) != null) {
            friendArray = unwrapRecentLocs(friendsWithLocs);
            System.out.println(friendArray + "\n");
        }
        else if ((friendsArray = (JSONArray)jsonResponse.get("friends")) != null) {
            sFriendArray = unwrapFriends(friendsArray);
            System.out.println(sFriendArray + "\n");
        }

    }*/

    }