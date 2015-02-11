import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

// This class handles all client socket threads and handles the creation of new sockets
class ServerRunnable implements Runnable {

    // Default port constant
    public final static int DEFAULT_PORT = 5555;

    // Fields inherited from parent
    public ServerPanel parent;
    public boolean isRunning;
    public int port;

    // Listener socket looking for clients
    ServerSocket serverSocket = null;

    // Status fields -- contains basic information about the state of the object and its child connections
    private boolean stopPressed;
    private boolean voting;
    private int numClients;
    private int idCounter;

    // Voting fields
    private int numQuestions = 0;                   // How many questions are being delivered to clients
    private int[] votes = new int[4];               // How many votes have been received for each question
    private String[] questions = new String[4];     // Question strings to be delivered to clients
    private boolean[] selected = new boolean[4];    // Whether or not said question is valid (has a checkbox checked)

    // ClientConnection housekeeping vectors -- keeps track of all objects/threads created by ServerRunnable
    private Vector<Thread> clientThreadList;
    private Vector<ClientConnection> clientSocketList;


    // CONSTRUCTOR
    // Assigns inherited fields and initializes object status fields
    ServerRunnable(ServerPanel parent, int port) {
        this.parent = parent;
        this.port = port;

        this.isRunning = false;
        this.stopPressed = false;
        this.numClients = 0;
        this.idCounter = 0;
    }


    // RUN METHOD
    public void run() {

        for (int x = 0; x <= 3; x++) {
            selected[x] = false;
            questions[x] = "";
        }

        // Create new lists for the clients and threads
        if (clientSocketList == null || !clientSocketList.isEmpty()) {
            clientSocketList = new Vector<ClientConnection>();
        }

        if (clientThreadList == null || !clientThreadList.isEmpty()) {
            clientThreadList = new Vector<Thread>();
        }

        // Now we're ready to go
        isRunning = true;
        stopPressed = false;

        // Server initialization
        parent.updateStatusBox("SERVER ONLINE");
        parent.setStatus(new Color(32, 160, 32), "ONLINE");
        parent.setButtonEnabled(parent.STOP_BUTTON, true);

        Socket clientSocket = null;

        // SERVER CONNECTION LOOP AND CLIENT THREAD GENERATION
        while (!stopPressed) {
            try {
                // Create a new socket and wait for a client connection
                parent.updateStatusBox("Listening for clients on port: " + String.valueOf(port));
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();

                // Once a connection is established, we pass it off to clientConnected, which creates a new thread
                //      to manage this client
                clientConnected(clientSocket);

            } catch (IOException e) {
                // No need to implement anything here -- cleanup is taken care of outside the loop
                // TODO: Move cleanup to a FINALLY block
                parent.updateStatusBox(e.getMessage());
            } finally {

                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    parent.updateStatusBox(e.getMessage());
                }
            }
        }

        // We're responsible adults...once we leave the server connection loop, we need to clean up (close all
        //      connections) before we can officially stop the server.
        parent.updateStatusBox("Stopping server...");

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
            parent.updateStatusBox("Stopping client from ServerRunnable Loop");
            disconnectClient(clientConnection);
        }
        snapshot.clear();

        // Once we've cleaned up, we let the user know that the server is down and re-enable the Start Server button
        parent.updateStatusBox("\nSERVER SHUTDOWN COMPLETE");
        parent.setStatus(Color.RED, "OFFLINE");
        isRunning = false;
        parent.setButtonEnabled(parent.GO_BUTTON, true);
    }


    // PUBLIC METHODS //

    // Once a client is connected to the server, their connection object is handed off to this method for handling
    synchronized public void clientConnected(Socket socket) {

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
        parent.updateStatusBox("Client " + clientConnection.id + " has connected on port " + port);
        parent.updateStatusBox("There are " + numClients + " clients connected\n");
    }

    // This method is called as a part of the client handshake -- the client makes a request to join the server
    //      and this method runs to check for information that should be sent back before they're ready to operate
    // NOTE: In the original chat server implementation, this also sent the client information about the connected
    //      users and that kind of thing -- a handshake method like this is helpful when you want to provide some
    //      kind of immediate functionality to users who connect in the middle of something already in progress
    //      (like a vote or chat already in progress)
    synchronized public void clientJoined(ClientConnection clientConnection) {

        // If voting is already in progress, send the user the question list and allow them to start voting
        if (voting) {

            clientConnection.xmitMessage("/numq" + numQuestions);

            for (int x = 0; x <= (numQuestions - 1); x++) {
                clientConnection.xmitMessage("/ques" + String.valueOf(x) + questions[x]);
            }

            clientConnection.xmitMessage("/stvt");
        } else {
            // As a part of the handshake, the client is expecting some form of reply.  This is placeholder for
            //      something actually useful
            clientConnection.xmitMessage("You're connected!");
        }
    }

    // This method sets a boolean value that tells the server to leave the socket generation loop and begin cleanup
    public void notifyStopPressed() {
        stopPressed = true;
        parent.getServerThread().interrupt();

        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            parent.updateStatusBox(e.getMessage());
        }
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
            parent.updateStatusBox(clientSocketList.indexOf(clientConnection) + " " + "Forcing disconnection of client " +
                    clientConnection.id + " on port " + port + "...");
        } else {

            // If this thread is NOT connected, it may be stuck, so let's send an interrupt to wake it up
            clientThreadList.get(clientSocketList.indexOf(clientConnection)).interrupt();
            parent.updateStatusBox("Closing listen socket on port " + port + "...");
        }
    }

    // This method is called when a client has disconnected from the server and its thread is about to end
    // It just presents a status message and removes the given client and thread from our lists
    synchronized public void clientThreadEnding(ClientConnection clientConnection) {

        parent.updateStatusBox(clientSocketList.indexOf(clientConnection) + " " + "Client " + clientConnection.id +
                " disconnected, removing from connection pool");

        clientThreadList.remove(clientSocketList.indexOf(clientConnection));
        clientSocketList.remove(clientSocketList.indexOf(clientConnection));
        numClients--;

        parent.updateStatusBox("There are " + numClients + " clients connected");
    }

    // THIS IS WHERE THE MAGIC HAPPENS -- on the server side at least
    // This method sends some given message to ALL connected clients by creating an iterator from clientSocketList
    //      and executing the xmitMessage method for each element in that iterator
    synchronized public void broadcastMsg(String message) {

        Iterator<ClientConnection> clientIterator = clientSocketList.iterator();
        ClientConnection clientConnection;
        while (clientIterator.hasNext()) {
            clientConnection = clientIterator.next();
            clientConnection.xmitMessage(message);
        }
    }

    // This method just populates the appropriate voting-related fields.  It does not affect the voting operation
    public void setQuestions(boolean[] selected, String[] questions, int numQuestions) {

        this.numQuestions = numQuestions;
        this.questions = questions;
        this.selected = selected;
    }

    // This method starts the voting process
    public void startVote() {

        this.voting = true;                             // Status boolean

        broadcastMsg("/numq" + numQuestions);           // Let all clients know how many questions are coming

        for (int x = 0; x <= (numQuestions - 1); x++) {
            broadcastMsg("/ques" + x + questions[x]);   // Send each question in turn to the clients
        }

        // Tell the clients that voting has started (doubles as a "no more questions" notification
        broadcastMsg("/stvt");
        parent.updateStatusBox("Voting started!  " + numQuestions + " questions presented to clients.");
    }

    // GETTER for the "voting" status boolean
    public boolean isVoting() { return voting; }


    // This method keeps track of how many votes have been cast, useful for letting the server manager know when to stop a vote
    public void setVote(int voteNo) {

        this.votes[voteNo]++;

        int totalVotes = votes[0] + votes[1] + votes[2] + votes[3];
        int votesRemaining = numClients - totalVotes;
        broadcastMsg("/rem" + votesRemaining);
        parent.updateStatusBox(totalVotes + " votes cast out of " + numClients + " clients.  "
                + votesRemaining + " votes remain.");
    }

    // This method stops the voting process
    public void stopVote() {

        this.voting = false;                            // Status boolean

        for (int x = 0; x <= (numQuestions - 1); x++) {
            broadcastMsg("/ansr" + x + votes[x]);       // Tells all clients how many votes were cast for each question
        }
        broadcastMsg("/novo");                          // Tells all users that voting has ended

        parent.updateStatusBox("Voting has ended!  Voting Results: \n1: " + votes[0] + " \n2: " + votes[1] + " \n3: " + votes[2] + " \n4: " + votes[3] );


        // Perform some simple math to calculate the vote winner, then let the server know which option won
        int largest = 0;
        int winner = 0;
        for(int x = 0; x <= 3; x++) {
            if (votes[x] > largest) {
                largest = votes[x];
                winner = (x + 1);
            }
        }
        parent.updateStatusBox("Option " + winner + " was reported as the winner.");

        for (int x = 0; x <= 3; x++) {
            this.votes[x] = 0;
        }
    }
}