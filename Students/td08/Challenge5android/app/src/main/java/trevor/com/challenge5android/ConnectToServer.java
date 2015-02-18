package trevor.com.challenge5android;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/*
Class used to handle socket connection to server and feed local database entries to server database
*/

public class ConnectToServer implements Runnable {

    //instance variables
    private String serverIp;
    private DatabaseManager db;
    private MainActivity parent;
    private Socket client;
    private PrintWriter printWriter;
    private BufferedReader reader;
    private final int port = 9090;

    //static class variables
    static int LastID = 0;  //static variable used to keep track of last transmitted database entry

    ConnectToServer (MainActivity parent, DatabaseManager h) {
        this.parent = parent;
        this.db = h;
    }

    public void run() {
        try {
            //status box message
            parent.setStatusText("Attempting to connect to server..." + "\n@" + serverIp + ":" + port);

            //set up socket connection and stream objects
            client = new Socket(serverIp, port);  //connect to server
            printWriter = new PrintWriter(client.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            //wait for server ready message to begin sending data and update status box
            String received = null;
            received = reader.readLine();
            parent.setStatusText("Connected to server!" + "\nServer says: " + received);
            Thread.sleep(1000); //sleep so user can see server status

            //gets last transmitted row ID to server as well as local entries recorded since last connection
            int lastId = LastID == 0 ? 1 : LastID;  //local lastId variable used during transmission
            int entries = lastId + db.entries;

            //initialize sqlCommand string to be sent to server
            String sqlCommand = null;
            parent.setStatusText("Now sending data...");

            //loops through rows not yet transmitted until all new entries are sent to server
            while (lastId < entries) {
                sqlCommand = db.getData(lastId);
                if (sqlCommand.equals(null))
                    break;
                printWriter.println(sqlCommand);    //send to server
                Log.d("ConnectToServer", "Sent ID: " + lastId); //update log to show last entry sent
                lastId++;   //increment lastId for next entry to be sent
            }

            //update static LastID to most recent entry ID in table
            LastID = lastId;
            DatabaseManager.entries = 0;

            printWriter.write("Done");  //tells the server data transmission is complete
            printWriter.flush();    //send pending data to output stream if necessary
            printWriter.close();    //close printwriter
            client.close();   //closing the connection
            parent.setStatusText("Finished sending data!" + "\n\nPress sense to begin collecting new data");
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            parent.setStatusText("Error connecting to server!");
        }
        catch (SocketTimeoutException t) {
            parent.setStatusText("Host unreachable! \nTry again later");
        }
        catch (IOException e) {
            e.printStackTrace();
            parent.setStatusText("Error sending data!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            parent.senseEnable(true);
            parent.clearEnable(true);
            try {
                printWriter.flush();
                printWriter.close();
                reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException n) {
                n.printStackTrace();
            }
        }
    }
    public void setServerIp (String ip) {
        this.serverIp = ip;
    }
}
