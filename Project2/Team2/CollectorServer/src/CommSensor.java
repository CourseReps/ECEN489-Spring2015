import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by joshuacano on 3/26/15.
 */
public class CommSensor implements Runnable  {
    public int port = 4444;

    private Master master;
    private Socket socket;
    private ServerSocket server;
    private OutputStreamWriter writer;
    private BufferedReader reader;

    CommSensor(Master master) {
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
            System.out.println("Sensor Client Connected");
            writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            while (socket.isConnected()) {
                line = reader.readLine();

                if (line != null && line.length() > 0) {
                    jsonMap = processString(line);

                }
                jsonCommand = jsonMap.get("command");
                if (jsonCommand.contains("Ping!")) {
                    master.server.sendCommand(jsonCommand);
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
}