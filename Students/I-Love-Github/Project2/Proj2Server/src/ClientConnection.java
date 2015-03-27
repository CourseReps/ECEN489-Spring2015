import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientConnection implements Runnable {

    public long timestamp;
    public HashMap<String, String> myMacs;

    private Socket mySocket;
    private ServerRunnable parent;

    private BufferedReader toServer = null;
    private PrintWriter toClient = null;

    ClientConnection(Socket mySocket, ServerRunnable parent) {
        this.mySocket = mySocket;
        this.parent = parent;
    }

    public void run() {

        try {
            toClient = new PrintWriter(mySocket.getOutputStream(), true);
            toServer = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

            String stringIn;
            HashMap<String, String> JSONmap;
            String command;

            while (mySocket.isConnected()) {
                stringIn = toServer.readLine();

                if (stringIn != null && stringIn.length() > 0) {

                    JSONmap = processString(stringIn);
                    command = JSONmap.get("command");

                    if (command.contains("macs")) {

                        JSONmap.remove("command");

                        timestamp = Long.valueOf(JSONmap.get("timestamp"));
                        JSONmap.remove("timestamp");

                        myMacs = JSONmap;

//                        Iterator<String> macs = JSONmap.keySet().iterator();
//                        String thisMac;
//                        while (macs.hasNext()) {
//
//                            thisMac = macs.next();
//                            System.out.print(thisMac + " | " + JSONmap.get(thisMac) + "\n");
//                        }

                    } else if (command.contains("ping") || command.contains("boom")) {

                        parent.gotPing();
                    }
                }
            }

        } catch(IOException e) {
            System.out.print(e.getMessage());

        } finally {
            try {
                toServer.close();
                toClient.close();

                if (mySocket != null && mySocket.isConnected()) {
                    mySocket.close();
                }

            } catch(IOException e) {
                System.out.print(e.getMessage());
                //TODO: Handle this IOE
            }
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

    public void xmitMessage(String string) {
        if (toClient != null) {
            toClient.println(string);
        }
    }
}