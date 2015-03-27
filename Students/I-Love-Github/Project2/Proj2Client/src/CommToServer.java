import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class CommToServer implements Runnable {

    public String ip = "127.0.0.1";
    public int port = 1337;

    private Master master;
    private Socket mySocket;
    private PrintWriter toServer;
    private BufferedReader toClient;

    CommToServer(Master master) {
        this.master = master;
    }

    public void run() {

        HashMap<String, String> jsonMap = new HashMap<String, String>();
        String stringIn;
        String jsonCommand;

        try {

            mySocket = new Socket(ip, port);
            toServer = new PrintWriter(mySocket.getOutputStream(), true);
            toClient = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

            while (mySocket.isConnected()) {

                stringIn = toClient.readLine();

                if (stringIn != null && stringIn.length() > 0) {
                    jsonMap = processString(stringIn);
                }

                jsonCommand = jsonMap.get("command");

                if (jsonCommand.contains("take_picture")) {

                    master.getCommToCsv.takepic();

                } else if (jsonCommand.contains("ping") || jsonCommand.contains("boom")) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("command", jsonCommand);
                    toServer.println(jsonObject.toJSONString());
                }
            }

        } catch (Exception e) {

        }
    }

    public void sendList(LinkedHashMap<String, String> macList) {

        if (mySocket.isConnected()) {
            macList.put("jsonCommand", "macs");
            macList.put("timestamp", String.valueOf(System.currentTimeMillis()));
            String jsonText = JSONValue.toJSONString(macList);
            toServer.println(jsonText);
        }
    }

    private HashMap<String, String> processString(String jsonText) {

        HashMap<String, String> returnList =  new HashMap<String, String>();

        jsonText = jsonText.replaceAll("\\{", "");
        jsonText = jsonText.replaceAll("\\}", "");

        StringTokenizer commaTokenizer = new StringTokenizer(jsonText, ",");
        String[] thisColon;
        String thisComma;
        while (commaTokenizer.hasMoreElements()) {

            thisComma = commaTokenizer.nextToken();

            if (thisComma != null && thisComma.length() > 0) {

                thisColon = thisComma.split("\":\"", 2);
                thisColon[0] = thisColon[0].replaceAll("\"", "");
                thisColon[1] = thisColon[1].replaceAll("\"", "");
                returnList.put(thisColon[0], thisColon[1]);
            }
        }

        return returnList;
    }
}
