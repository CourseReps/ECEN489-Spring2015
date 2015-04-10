import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by joshuacano on 3/26/15.
 */
public class CommOCV implements Runnable {
    public int port = 9019;

    private Master master;
    private Socket socket;
    private ServerSocket server;
    private PrintWriter writer;
    private BufferedReader reader;

    CommOCV(Master master) {
        this.master = master;
    }

    @Override
    public void run() {
        HashMap<String, String> jsonMap = new HashMap<String, String>();
        String line;
        String jsonCommand;
        try {
            server = new ServerSocket(port);
            socket = server.accept();
            System.out.println("Facial Recognition Client Connected");
            //socket = new Socket(ip, port);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            while(socket.isConnected()) {
                line = reader.readLine();
                if (line != null && line.length() > 0) {
                    jsonMap = processString(line);
                }

                jsonCommand = jsonMap.get("command");

                if (jsonCommand.contains("filename")) {
                    System.out.print("\n\nReceived filename from opencsv\n\n");
                    master.getCommToServer();

                }

            }


        } catch (Exception e) {

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
    public void sendCommand(String command) {

        System.out.print("CSV Called sendCommand\n");

        if (socket != null && socket.isConnected()) {
            HashMap<String, String> jsonCommand = new HashMap<String, String>();
            jsonCommand.put("command", command);
            jsonCommand.put("timestamp", String.valueOf(System.currentTimeMillis()));
            String jsonString = JSONValue.toJSONString(jsonCommand);

            writer.println(jsonString);
            writer.flush();

            System.out.print(jsonString + "\n");
        }
    }
}