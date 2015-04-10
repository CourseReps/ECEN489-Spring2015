import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

/**
 * Created by joshuacano on 3/26/15.
 */
public class CommSvr implements Runnable {

    public String ip = "10.202.115.89";
    public int port = 1337;

    private Master master;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    CommSvr(Master master) {
        this.master = master;
    }

    @Override
    public void run() {
        System.out.println("started Server Client");
        HashMap<String, String> jsonMap = new HashMap<String, String>();
        String line;
        String jsonCommand;
        try {
            socket = new Socket(ip, port);
            System.out.print("connected to server\n");

            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            while (socket.isConnected()) {
                line = reader.readLine();

                if (line != null && line.length() > 0) {
                    jsonMap = processString(line);
                }
                System.out.println("Facial Recognition is connected to server");
                jsonCommand = jsonMap.get("command");

                if (jsonCommand.contains("take_picture")) {

                    System.out.print("\n\nasked to take picture\n\n");
                    master.facialRecog.sendCommand("take_picture");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendList(LinkedHashMap<String, String> macList) {

        System.out.print("sendlist\n");
        if (socket != null && socket.isConnected()) {
            macList.put("command", "macs");
            macList.put("timestamp", String.valueOf(System.currentTimeMillis()));
            String jsonText = JSONValue.toJSONString(macList);
            System.out.print(jsonText + "\n");
            writer.println(jsonText);
        }
    }

    public void sendCommand(String command) {

        if (socket != null && socket.isConnected()) {

            HashMap<String, String> jsonCommand = new HashMap<String, String>();
            jsonCommand.put("command", command);
            jsonCommand.put("data", String.valueOf(System.currentTimeMillis()));
            String jsonString = JSONValue.toJSONString(jsonCommand);

            System.out.print(jsonString + "\n");
            writer.println(jsonString);
        }
    }

    public void sendJSON(HashMap<String, String> json) {

        if (socket != null && socket.isConnected()) {

            String jsonString = JSONValue.toJSONString(json);
            System.out.print(jsonString + "\n");
            writer.println(jsonString);
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