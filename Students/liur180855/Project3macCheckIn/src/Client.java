import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Rong Liu on 4/28/2015.
 */
public class Client {

    /*    static HashMap<String, String> map = new HashMap<String, String>();

        public static void main(String[] args) throws IOException {
            //Building HashMap
            map.put("00:0a:95:9d:68:16", "Rong");
            map.put("00:1a:45:9d:66:11", "Evelyn");


            String serverAddress =new String( "10.202.124.136");
            //InetAddress.getLocalHost()
            Socket s = new Socket(serverAddress, 9898);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            JSONObject answer1 = JSONWrapping.stringToJSON(answer);
            String mac = (String)answer1.get("mac");
            System.out.println(mac);
            PrintWriter out =
                    new PrintWriter(s.getOutputStream(), true);
            JSONObject jsonRequest = JSONWrapping.getCheckInJSON("rongliu","college station",2,"mac");

            if (map.containsKey(mac)) {
                System.out.println("MAC matched");
                System.out.println(map.get(mac));

            }
            //JSONObject jsonResonse = JSONWrapping.serverUnwrapping(jsonRequest);

            //System.out.println("Received timestamp: " + jsonObject.get("timestamp").toString());

            //System.out.println(jsonResonse.toString());
            out.print(jsonRequest + "\r\n"); // send the response to client
            out.flush();
            System.exit(0);
        }
        */
    public void macCheckIn(String userName, Integer timpstamp) throws IOException {
        JSONObject jsonRequest = JSONWrapping.getCheckInJSON(userName,"college station",timpstamp,"mac");
        String serverAddress =new String( "165.91.210.26");
        //InetAddress.getLocalHost()
        Socket s = new Socket(serverAddress, 9898);
        PrintWriter out =
                new PrintWriter(s.getOutputStream(), true);
        out.print(jsonRequest + "\r\n"); // send the response to client
        out.flush();
        s.close();
        //return 0;
    }

}
