package www.foureightynine.com.challenge4;

import android.database.Cursor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Iterator;

// This class handles all client-server interactions on the client side
class CommHandler implements Runnable {

    // This list of message types is incomplete -- more can be found down in processMessage(String message)
    final static public String QUIT = "/quit";
    final static public String JOIN_SERVER = "/join";

    // Default server port
    public final static String DEFAULT_IP = "10.202.104.72";
    public final static int DEFAULT_PORT = 5555;

    // Connection status booleans
    public boolean isRunning;
    public boolean isConnected;
    private boolean disconnect;

    // Fields inhereted from parent
    private ClientRunnable parent;
    private String host;
    private int port;

    // Socket fields
    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader toClient;


    // CONSTRUCTOR
    // Assigns inherited fields and initializes connection status fields
    CommHandler(ClientRunnable parent, String host, int port) {
        this.parent = parent;
        this.host = host;
        this.port = port;

        initialize();
    }

    CommHandler(ClientRunnable parent) {
        this.parent = parent;
        this.host = DEFAULT_IP;
        this.port = DEFAULT_PORT;

        initialize();
    }

    private void initialize() {
        this.isRunning = false;
        this.disconnect = false;
        this.socket = null;
        this.toServer = null;
    }

    // RUN METHOD
    public void run() {

        isRunning = true;
        disconnect = false;

        // Client initialization
        parent.newMessage("Connecting...");
        String stringIn;

        while (!disconnect) {
            // SERVER CONNECTION LOOP AND CLIENT THREAD GENERATION
            try {

                Thread.sleep(3000);

                // If a socket doesn't exist, create one and tell the user (connect button)
                if (socket == null || socket.isClosed()) {
                    socket = new Socket(host, port);
                }

                // Socket command is blocking, the status and datastreams don't get updated until a connection is made
                isConnected = true;
                toServer = new PrintWriter(socket.getOutputStream(), true);
                toClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Initial handshake message to server
                toServer.println(JOIN_SERVER);
                processString(toClient.readLine());
                parent.newMessage("Connected to host at " + String.valueOf(host) + ":" + String.valueOf(port));

                // Connection loop -- read received strings and process them accordingly
                // Send operations are generated automatically or through the user interface -- typically on another thread
                while (!disconnect && socket.isConnected()) {
                    stringIn = toClient.readLine();

                    if (stringIn != null && stringIn.length() > 0) {
                        processString(stringIn);
                    }
                }

                // Catch blocks are required by try statements -- just reports back to the user right now
            } catch (IOException e) {
                parent.newMessage(e.getMessage());
                //TODO: IO Exceptions should be handled


            } catch (InterruptedException e) {
                parent.newMessage(e.getMessage());

                // If the connection loop ends or the connection is lost, flush the buffers and close outstanding connections
            } finally {
                try {
                    if (isConnected) {
                        toServer.flush();
                        toServer.close();
                        toClient.close();
                    }

                    if (socket != null && socket.isConnected()) {
                        socket.close();
                        isConnected = false;
                    }
                } catch (IOException e) {
                    parent.newMessage(e.getMessage());
                }
            }
        }

        // Update the UI to allow the user to reconnect, then end
        parent.newMessage("Connection Closed!");
        isRunning = false;
    }

    // PROCESS_STRING IS WHERE ALL OF THE MAGIC HAPPENS
    //
    // TABLE OF MESSAGE TYPES (server AND client)
    //
    //      STRING TYPE         FORMAT          OPERATION
    //  --------------------    ------------    ---------------------------------------------------------------------------------------
    //      Join Server         /join           Part of the handshake -- tells the server that the client is ready for initial commands
    //      Leave Server        /quit           From client: requests a disconnect || From server: forces client to disconnect

    // For easy reference, the following if-else if statements follow the same order as the above table
    synchronized public void processString(String string) {

        // Fail-safe -- we shouldn't get any null strings, but this will keep us from throwing NullExceptions
        if (string == null) {
            return;
        }

        // Request User Id
        if (string.startsWith("/rid")) {

            long myID = parent.getDB().getID();
            sendString("/id" + String.valueOf(myID));

            // Latest Timestamp
        } else if (string.startsWith("/lt")) {

            long ts = Long.valueOf(string.substring(3));
            parent.setServerTS(ts);

            if (ts < parent.getDB().getLatestTime()) {

                collectAndSendData();
            }
        } else if (string.startsWith(QUIT)) {
            parent.newMessage("Server closed connection!");
            disconnect();
        }

        parent.updateUI(string);
    }

    private void collectAndSendData() {
        String key;
        String time;
        String name;
        String data;

        try {
            Iterator<DBHandler.DataPoint> xmitList = parent.getDB().gatherDataForServer();
            DBHandler.DataPoint thisPoint;
//            Cursor xmitList = parent.getDB().gatherDataForServer();

//            while (xmitList.moveToNext()) {
//                key = String.valueOf(xmitList.getInt(xmitList.getColumnIndex("ID")));
//                time = String.valueOf(xmitList.getLong(xmitList.getColumnIndex("TIME")));
//                name = xmitList.getString(xmitList.getColumnIndex("NAME"));
//                data = xmitList.getString(xmitList.getColumnIndex("DATA"));

            while (xmitList.hasNext()) {

                thisPoint = xmitList.next();

                key = String.valueOf(thisPoint.id);
                time = String.valueOf(thisPoint.time);
                name = thisPoint.name;
                data = thisPoint.data;

                sendString("/data" + key + ", " + time + ", '" + name + "', '" + data + "'");
            }

            parent.newMessage("Flushing data to server.");
            parent.setServerTS(parent.getMyTS());

        } catch (Exception e) {

            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);

                    collectAndSendData();
                } catch (InterruptedException e) {
                    // Don't do anything here
                }
            }
        }).start();
    }

    // PUBLIC METHODS //

    // This method sends messages to the server
    // The if-else statement may be redundant at this point -- it is a vestige of the old chat server architecture
    //      this program is built upon
    synchronized public void sendString(String stringOut) {
        if (toServer != null && socket != null && socket.isConnected()) {

            toServer.println(stringOut);
        }
    }

    // Request disconnection from the server and leave the connection loop
    synchronized public void disconnect() {

        sendString(String.valueOf(QUIT));

        this.disconnect = true;

        try {
            if (socket != null && socket.isConnected()) {
                parent.newMessage("Disconnecting...");
                socket.close();
            }

        } catch(IOException e) {
            parent.newMessage(e.getMessage());
            //TODO: Handle this IOE
        }
    }

    public void lastSend() {
        collectAndSendData();
    }
}