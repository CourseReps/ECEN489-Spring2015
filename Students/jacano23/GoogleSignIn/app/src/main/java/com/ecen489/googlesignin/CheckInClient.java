package com.ecen489.googlesignin;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class CheckInClient {
    public String clientLoginHandler(User user) {
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

            }
            else {
                response = "Failure";
            }

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    public void clientLogoutHandler(User user) {
        String jsonLogout = "";
        jsonLogout = JSONWrapping.getLogoutJSON(user.getUserName(), user.getSessionId()).toJSONString();

        // Output stream and Response
        WebServiceHttps.invokeHelloWorldWS(jsonLogout, "getServerResponse");
    }


}
/*



    //Add friends querry

            ArrayList<String> frineds = new ArrayList<String>();
                    frineds.add("Trevor");
                    frineds.add("Benito");

            JSONObject friend_json= JSONWrapping.getAddFriendsJSON("Mandel", frineds, sessionID);

            wr_to_server.println(friend_json.toString());
            wr_to_server.flush();
            System.out.println("sent Mandel's addfriends query");

            System.out.println("Waiting for Mandel's addfriends response");
            line = input.readLine();
            JSONObject resp_friendslist = (JSONObject) JSONValue.parse(line);


            System.out.println("unwrapping Mandel's addfriends response");
            //JSONWrapping.clientUnwrapping(resp_friendslist);
            if ((friendsArray = (JSONArray)resp_friendslist.get("friends")) != null) {
            sFriendArray = unwrapFriends(friendsArray);
            System.out.println(sFriendArray + "\n");
        }



//checkin querry
        wr_to_server.println(JSONWrapping.getCheckInJSON("Mandel","EIC",256223,"OpenCV",sessionID).toJSONString());
        //username,location,timestamp,method,session id
        wr_to_server.flush();
        System.out.println("Sent Mandel's Check-in info ");

        System.out.println("Waiting for Mandel's Check-in response");
        line = input.readLine();
        JSONObject resp_checkin = (JSONObject) JSONValue.parse(line);

        System.out.println("unwrapping Mandel's Check-in response");
        JSONWrapping.clientUnwrapping(resp_checkin);

//get recent locations
                //recent locs of friends
                wr_to_server.println(JSONWrapping.getRecentLocsJSON("Mandel", frineds, sessionID).toJSONString());
                //username,friends Array,session id
                wr_to_server.flush();
                System.out.println("Sent Mandel's  get_friend_recent_locs querry ");

                line = input.readLine();

                System.out.println(line);
*/