import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

// This class handles all client socket threads and handles the creation of new sockets
class CommHandler implements Runnable {

    // Default port constant
    public final static int DEFAULT_PORT = 5555;

    // Fields inherited from parent
    public ServerRunnable parent;
    public boolean isRunning;
    public int port;

    // Listener socket looking for clients
    ServerSocket serverSocket = null;

    // Status fields -- contains basic information about the state of the object and its child connections
    private boolean stopPressed;
    private int numClients;
    private int idCounter;

    // ClientConnection housekeeping vectors -- keeps track of all objects/threads created by ServerRunnable
    private Vector<Thread> clientThreadList;
    private Vector<ClientConnection> clientSocketList;
    private DiscoveryBroadcast broadcast;
    private Thread discoveryThread;


    // CONSTRUCTOR
    // Assigns inherited fields and initializes object status fields
    CommHandler(ServerRunnable parent) {
        this.parent = parent;
        this.port = DEFAULT_PORT;

        initialize();
    }

    CommHandler(ServerRunnable parent, int port) {
        this.parent = parent;
        this.port = port;

        initialize();
    }

    private void initialize() {
        this.isRunning = false;
        this.stopPressed = false;
        this.numClients = 0;
        this.idCounter = 0;
    }


    // RUN METHOD
    public void run() {

        // Create new lists for the clients and threads
        if (clientSocketList == null || !clientSocketList.isEmpty()) {
            clientSocketList = new Vector<ClientConnection>();
        }

        if (clientThreadList == null || !clientThreadList.isEmpty()) {
            clientThreadList = new Vector<Thread>();
        }

        String s = "Not Detected";
        try {
            s = InetAddress.getLocalHost().getHostAddress();
        } catch(UnknownHostException e) {
            // nothing here
        }
        parent.newMessage("My IP address is: " + s);
//        broadcast = new DiscoveryBroadcast(this, s);
//        discoveryThread = new Thread(broadcast);
//        discoveryThread.start();

        // Now we're ready to go
        isRunning = true;
        stopPressed = false;

        // Server initialization
        parent.newMessage("SERVER ONLINE");

        Socket clientSocket = null;

        // SERVER CONNECTION LOOP AND CLIENT THREAD GENERATION
        while (!stopPressed) {
            try {
                // Create a new socket and wait for a client connection
                parent.newMessage("Listening for clients on port: " + String.valueOf(port));
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();

                // Once a connection is established, we pass it off to clientConnected, which creates a new thread
                //      to manage this client
                clientConnected(clientSocket);

            } catch (IOException e) {
                // No need to implement anything here -- cleanup is taken care of outside the loop
                // TODO: Move cleanup to a FINALLY block
                parent.newMessage(e.getMessage());
            } finally {

                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    parent.newMessage(e.getMessage());
                }
            }
        }

        // We're responsible adults...once we leave the server connection loop, we need to clean up (close all
        //      connections) before we can officially stop the server.
        parent.newMessage("Stopping server...");

        // Instead of locking clientSocketList inside of a huge synchronized block, just make a copy of it
        //      This is a good idea because as clients disconnect, they will be asking to remove themselves from
        //      the clientSocketList, and we don't want them to wait
        Vector<ClientConnection> snapshot = new Vector<ClientConnection>();
        snapshot.addAll(clientSocketList);
        Iterator<ClientConnection> clientIterator = snapshot.iterator();
        ClientConnection clientConnection;

        // This loop iterates through all of the currently connected clients and disconnects them
        while (clientIterator.hasNext()) {
            clientConnection = clientIterator.next();
            parent.newMessage("Stopping client from ServerRunnable Loop");
            disconnectClient(clientConnection);
        }
        snapshot.clear();

        // Once we've cleaned up, we let the user know that the server is down and re-enable the Start Server button
        parent.newMessage("\nSERVER SHUTDOWN COMPLETE");
//        parent.setStatus(Color.RED, "OFFLINE");
        isRunning = false;
//        parent.setButtonEnabled(parent.GO_BUTTON, true);
    }


    // PUBLIC METHODS //

    // Once a client is connected to the server, their connection object is handed off to this method for handling
    synchronized public void clientConnected(Socket socket) {

        if (idCounter <= 4) {
            parent.getUI().setClientStatus(idCounter, true);
        }

        // Create a ClientConnection to contain this new client, pass off the socket, and start the new thread
        ClientConnection clientConnection = new ClientConnection(this, socket, idCounter);
        Thread clientSocketThread = new Thread(clientConnection);
        clientSocketThread.start();

        // Add this connection to our socket and thread lists so we can keep track of who is alive
        clientSocketList.add(clientConnection);
        clientThreadList.add(clientSocketList.indexOf(clientConnection), clientSocketThread);

        numClients++;       // numClients just keeps track of how many clients are connected
        idCounter++;        // idCounter NEVER decrements, so we will never have two clientConnections trying to occupy
        //      the same space in the vector

        // Let the server status box know that someone has connected
        parent.newMessage("Client " + clientConnection.id + " has connected on port " + port);
        parent.newMessage("There are " + numClients + " clients connected\n");
    }

    // This method is called when a request has been made to close a client connection
    synchronized public void disconnectClient(ClientConnection clientConnection) {

        // Ensures this connection exists (a necessary check when this is being called from outside of a clientConnection)
        if (clientConnection != null && clientConnection.isConnected() &&
                clientSocketList.get(clientSocketList.indexOf(clientConnection)) != null) {

            // Send the client a message telling it to close its socket
            clientConnection.xmitMessage(ClientConnection.QUIT);

            // Then close that socket...the user really doesn't have a choice.  We're just asking to be nice.
            clientSocketList.get(clientSocketList.indexOf(clientConnection)).closeSocket();
            parent.newMessage(clientSocketList.indexOf(clientConnection) + " " + "Forcing disconnection of client " +
                    clientConnection.id + " on port " + port + "...");
        } else {

            // If this thread is NOT connected, it may be stuck, so let's send an interrupt to wake it up
            clientThreadList.get(clientSocketList.indexOf(clientConnection)).interrupt();
            parent.newMessage("Closing listen socket on port " + port + "...");
        }
    }

    // This method is called when a client has disconnected from the server and its thread is about to end
    // It just presents a status message and removes the given client and thread from our lists
    synchronized public void clientThreadEnding(ClientConnection clientConnection) {

        if (clientConnection.id <= 4) {
            parent.getUI().setClientStatus(clientConnection.id, false);
        }

        parent.newMessage(clientSocketList.indexOf(clientConnection) + " " + "Client " + clientConnection.id +
                " disconnected, removing from connection pool");

        clientThreadList.remove(clientSocketList.indexOf(clientConnection));
        clientSocketList.remove(clientSocketList.indexOf(clientConnection));
        numClients--;

        parent.newMessage("There are " + numClients + " clients connected");
    }

    public void newMessage(String string) {
        parent.newMessage(string);
    }

    public DBHandler getDB() {
        return parent.getDB();
    }

    public ServerPanel getUI() { return parent.getUI(); }
}