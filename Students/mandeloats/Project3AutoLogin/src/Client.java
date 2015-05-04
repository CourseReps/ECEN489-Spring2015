import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rong Liu on 4/28/2015.
 */
public class Client {

       static HashMap<String, String> map = new HashMap<String, String>();

        static void macCheckIn(String username, String loc,String sessionID) throws IOException {
            String serverAddress =new String( "10.202.124.160");
            //String serverAddress =new String( "165.91.210.26");
            //InetAddress.getLocalHost()
            Socket s = new Socket(serverAddress, 9898);

            PrintWriter out =
                    new PrintWriter(s.getOutputStream(), true);
            JSONObject jsonRequest;
            jsonRequest = JSONWrapping.getCheckInJSON(username, loc, 100000008, "mac", sessionID);


            //System.out.println("Received timestamp: " + jsonObject.get("timestamp").toString());

            //System.out.println(jsonResonse.toString());
            out.print(jsonRequest + "\r\n"); // send the response to client
            out.flush();

            try{Thread.sleep(5000);}catch (InterruptedException ie){}
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            System.out.println(answer);

            ArrayList<String> locs = new ArrayList<String>();
            locs.add(loc);
            jsonRequest = JSONWrapping.getRecentFriendsJSON(username, locs, sessionID);
            out.print(jsonRequest + "\r\n"); // send the response to client
            out.flush();
            answer = input.readLine();
            System.out.println(answer);
            System.exit(0);
        }
        public static void main(String[] args) throws IOException {
            //Building HashMap
            map.put("00:0a:95:9d:68:16", "Mandel");
            map.put("00:1a:45:9d:66:11", "Trevor");


            //String serverAddress =new String( "10.202.124.73");
            //InetAddress.getLocalHost()
            //Socket s = new Socket(serverAddress, 9898);

            //PrintWriter out =
            //        new PrintWriter(s.getOutputStream(), true);

            String mac = "00:0a:95:9d:68:16";
            if (map.containsKey(mac)) {
                System.out.println("MAC matched");
                System.out.println(map.get(mac));

            }
            macCheckIn(map.get(mac),"EIC","asdf");
/*
            JSONObject jsonRequest;
            jsonRequest = JSONWrapping.getCheckInJSON(map.get(mac), "EIC", 100000008, "mac", "asdf");


            //System.out.println("Received timestamp: " + jsonObject.get("timestamp").toString());

            //System.out.println(jsonResonse.toString());
            out.print(jsonRequest + "\r\n"); // send the response to client
            out.flush();

            try{Thread.sleep(5000);}catch (InterruptedException ie){}
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            System.out.println(answer);

            ArrayList<String> locs = new ArrayList<String>();
            locs.add("EIC");
            jsonRequest = JSONWrapping.getRecentFriendsJSON(map.get(mac), locs, "asdf");
            out.print(jsonRequest + "\r\n"); // send the response to client
            out.flush();
            answer = input.readLine();
            System.out.println(answer);
            System.exit(0);
            */
        }

}
