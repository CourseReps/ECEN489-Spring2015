import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

class ClientRunnable implements Runnable {

    final static public String MESSAGE = "/msg";
    final static public String USERLIST_ADD = "/add";
    final static public String USERLIST_REMOVE = "/rem";
    final static public String USERLIST_CHANGE = "/chg";

    final static public String QUIT = "/quit";
    final static public String JOIN_SERVER = "/join";
    final static public String SET_USERNAME = "/name";
    final static public String GET_USER_LIST = "/list";

    public final static int DEFAULT_PORT = 6668;
    public final static int IMGPORT = 6667;

    public boolean isRunning;
    public boolean isConnected;

    private boolean disconnect;
    private ClientPanel parent;

    private String userName;
    private String host;
    private int port;

    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader toClient;

    // CONSTRUCTOR
    ClientRunnable(ClientPanel parent, String host, int port, String userName) {
        this.parent = parent;
        this.host = host;
        this.port = port;
        this.userName = userName;

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
        boolean handshake = true;
        String stringIn;

        // SERVER CONNECTION LOOP AND CLIENT THREAD GENERATION
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(host, port);
                parent.connectButtonManip("Disconnect", true);
            }

            isConnected = true;

            toServer = new PrintWriter(socket.getOutputStream(), true);
            toClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (handshake) {
                toServer.println(SET_USERNAME + " " + userName);
                processString(toClient.readLine());
                toServer.println(GET_USER_LIST);
                processString(toClient.readLine());
                toServer.println(JOIN_SERVER);
                processString(toClient.readLine());
                parent.newMessage("Connected to host at " + String.valueOf(host) + ":" + String.valueOf(port));
            }

            while (!disconnect && socket.isConnected()) {
                stringIn = toClient.readLine();

                if (stringIn != null && stringIn.length() > 0) {
                    processString(stringIn);
                }
            }

        } catch(IOException e) {
//            parent.newMessage("Server I/O Error in main client loop");
            parent.newMessage(e.getMessage());
            //TODO: IO Exceptions should be handled

        } finally {
            try {
//                parent.newMessage("Closing connection...");
                if (isConnected) {
                    toServer.flush();
                    toServer.close();
                    toClient.close();
                }

                if (socket != null && socket.isConnected()) {
                    socket.close();
                    isConnected = false;
                }
            } catch(IOException e) {
//                parent.newMessage("I/O Error closing connection in main client loop");
                parent.newMessage(e.getMessage());
            }
        }

        parent.connectButtonManip("Connect", true);
        parent.newMessage("Connection Closed!");
        isRunning = false;
    }


    // METHODS //
    synchronized public void processString(String string) {

        if (string == null) {
            return;
        }

        if (string.startsWith(MESSAGE)) {
            String newString = string.replace(MESSAGE, "");
            parent.newMessage(newString);

        } else if (string.startsWith(QUIT)) {
            parent.newMessage("Server closed connection!");
            disconnect();

        } else if (string.startsWith(GET_USER_LIST)) {

            parent.clearUserList();
            String newString = string.replace(GET_USER_LIST, "");
            String currentName;
            StringTokenizer token = new StringTokenizer(newString , ",");
            while (token.hasMoreTokens()) {
                currentName = token.nextToken();

                if (!currentName.equals(userName)) {
                    parent.userManip(currentName, true);
                }
            }

        } else if (string.startsWith(SET_USERNAME)) {

            String newString = string.replace(SET_USERNAME, "");
            StringTokenizer token = new StringTokenizer(newString, ",");

            String newName = token.nextToken();
            String oldName = null;
            this.userName = newName;
            parent.changeNameBox(newName);

            if (token.hasMoreTokens()) {
                oldName = token.nextToken();
            }

            if (oldName != null) {
                parent.userManip(oldName, false);
            }

        } else if (string.startsWith(USERLIST_ADD)) {

            String newString = string.replace(USERLIST_ADD, "");

            parent.userManip(newString, true);
            parent.newMessage(newString + " joined");

        } else if (string.startsWith(USERLIST_REMOVE)) {

            String newString = string.replace(USERLIST_REMOVE, "");

            parent.userManip(newString, false);
            parent.newMessage(newString + " left");

        } else if (string.startsWith(USERLIST_CHANGE)) {
            String outputString;
            String newString = string.replace(USERLIST_CHANGE, "");

            StringTokenizer token = new StringTokenizer(newString, ",");
            String newName = token.nextToken();
            parent.userManip(newName, true);

            String oldName = token.nextToken();
            parent.userManip(oldName, false);
            outputString = "User " + oldName + " changed name to " + newName;

            if (oldName.equals(this.userName)) {
                this.userName = newName;
                parent.changeNameBox(newName);
            }

            parent.newMessage(outputString);
        }
    }


    synchronized public void sendString(String stringOut) {
        if (toServer != null && socket != null && socket.isConnected()) {

            if (stringOut.startsWith(QUIT)) {
                toServer.println(QUIT);
            } else if (stringOut.startsWith(GET_USER_LIST)) {
                toServer.println(GET_USER_LIST);
            } else if (stringOut.startsWith(JOIN_SERVER)) {
                toServer.println(JOIN_SERVER);
            } else if (stringOut.startsWith(SET_USERNAME)) {
                toServer.println(stringOut + "," + userName);
            } else {
                toServer.println(MESSAGE + stringOut);
            }
        }
    }


    synchronized public void disconnect() {

        sendString(String.valueOf(QUIT));
        parent.clearUserList();

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

    synchronized public void setUserName(String userName) {
        this.userName = userName;
    }
}