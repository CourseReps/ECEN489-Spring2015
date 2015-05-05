package com.ecen489.slidermenu;

import android.util.Log;

import com.ecen489.slidermenu.HashMachine;
import com.ecen489.slidermenu.JSONWrapping;
import com.ecen489.slidermenu.WebServiceHttps;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class CheckInClient {
    public String clientLoginHandler(UserInfo user) {
        String sessionID = null;
        String jsonLogin = "";
        String response = "";
        String myResponse = "";
        try {
            String password = HashMachine.generateUnsaltedUserHash("password");
            user.setPassword(password);
            jsonLogin = JSONWrapping.getLoginJSON(user.getUserName(), user.getPassword()).toString();

            // Output stream and Response
            String line = WebServiceHttps.invokeHelloWorldWS(jsonLogin, "getServerResponse");

            JSONObject jsonResponse = (JSONObject) JSONValue.parse(line);

            JSONWrapping.clientUnwrapping(jsonResponse);
            myResponse = jsonResponse.toString();

            // Unwrapping
            JSONObject loginOutcome = new JSONObject();
            if((loginOutcome = (JSONObject)jsonResponse.get("loginOutcome"))!=null) {
                response = (String) loginOutcome.get("outcome");
                sessionID = (String) loginOutcome.get("sessionID");
                user.setSessionId(sessionID);
                System.out.println(user.getSessionId()+"CheckInClient");

            }
            else {
                response = "Failure";
            }

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionID;
    }
    public static void clientLogoutHandler(UserInfo user) {
        String jsonLogout = "";
        jsonLogout = JSONWrapping.getLogoutJSON(user.getUserName(), user.getSessionId()).toJSONString();

        // Output stream and Response
        WebServiceHttps.invokeHelloWorldWS(jsonLogout, "getServerResponse");
    }

    public static void clientFriendHandler(UserInfo user) {
        String response = null;
        JSONObject friend_json = JSONWrapping.getAddFriendsJSON(user.getUserName(), user.getFriendsList(), user.getSessionId());
        String line = WebServiceHttps.invokeHelloWorldWS(friend_json.toString(), "getServerResponse");
        System.out.println(friend_json.toString());
        JSONObject jsonResponse = (JSONObject) JSONValue.parse(line);
        System.out.println(line);
        //System.out.println(jsonResponse.toString());

    }

    public static void clientLocationHandler(UserInfo user, String location){
        JSONObject location_json = JSONWrapping.getCheckInJSON(user.getUserName(), location, (int) System.currentTimeMillis(), "opencv", user.getSessionId());
        String line = WebServiceHttps.invokeHelloWorldWS(location_json.toString(), "getServerResponse");
        System.out.println(location_json.toString());
        JSONObject jsonResponse = (JSONObject) JSONValue.parse(line);
        System.out.println(line);

    }

    public static ArrayList<LocWithCheckIns> clientRecentLocations(UserInfo user, ArrayList<String> locations){
        ArrayList<LocWithCheckIns> testLocArray = null;
        System.out.println(locations+"sjfsoifjo" );
        JSONObject recentLocations_json = JSONWrapping.getRecentFriendsJSON(user.getUserName(), locations, user.getSessionId());

        String line = WebServiceHttps.invokeHelloWorldWS(recentLocations_json.toString(), "getServerResponse");
        System.out.println(recentLocations_json.toString());
        JSONObject jsonResponse = (JSONObject) JSONWrapping.stringToJSON(line);
        System.out.println(line);

        JSONArray mylocations;

        System.out.println("unwrapping Mandel's select friends request");
        if ((mylocations = (JSONArray)jsonResponse.get("recentFriends")) != null) {
            testLocArray = JSONWrapping.unwrapRecentFriends(mylocations);
        }
        return testLocArray;
    }



}