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

    public static void checkIn(String username, String location, String userPassword,int timestamp ) throws IOException {
        try {
            String sessionID=null;
            String ip = "165.91.210.26";
            int portNumber = 9898;
            Socket clientSocket = new Socket(ip, portNumber);
            System.out.println("connected to Server.");

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter wr_to_server = new PrintWriter(clientSocket.getOutputStream(), true);
            String password=HashMachine.generateUnsaltedUserHash(userPassword);

            System.out.println("sent Users's login info");
          String json_string = JSONWrapping.getLoginJSON(username, password).toJSONString();
            wr_to_server.println(json_string);
            wr_to_server.flush();

            String line = input.readLine();
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
            


            wr_to_server.println(JSONWrapping.getCheckInJSON(username,location,timestamp,"MAC Detection",sessionID).toJSONString());
            wr_to_server.flush();
            System.out.println("Sent User's Check-in info ");

            System.out.println("Waiting for User's Check-in response");
            line = input.readLine();
            JSONObject resp_checkin = (JSONObject) JSONValue.parse(line);

            System.out.println("unwrapping User's Check-in response");
            JSONWrapping.clientUnwrapping(resp_checkin);

            wr_to_server.println("disconnect");
            wr_to_server.flush();



            clientSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
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