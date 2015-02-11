import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// This class manages a connection to a client and its associated input stream
public class ClientConnection implements Runnable {

    // This list of message types is incomplete -- more can be found down in processMessage(String message)
    final static public String QUIT = "/quit";
    final static public String JOIN_SERVER = "/join";

    // This is used by the parent class to keep track of the connected clients
    final public int id;

    // Fields inherited from the parent
    private ServerRunnable parent;
    private Socket clientSocket;
    private PrintWriter toClient;

    // CONSTRUCTOR
    // Populates inherited fields
    ClientConnection(ServerRunnable parent, Socket clientSocket, int id) {
        this.clientSocket = clientSocket;
        this.parent = parent;
        this.id = id;
    }

    // RUN FUNCTION
    public void run() {

        BufferedReader toServer = null;

        try {
            // Create the I/O streams
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            toServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Connection loop -- read in client messages and process them
            String stringIn;
            while (clientSocket.isConnected()) {
                stringIn = toServer.readLine();

                if (stringIn != null && stringIn.length() > 0) {
                    processString(stringIn);
                }
            }

            // If the loop is broken, the server lets us know about it
            parent.parent.updateStatusBox("Stopping client from ClientConnection");

        } catch(IOException e) {
            parent.parent.updateStatusBox(id + " " + e.getMessage());

            //TODO: IO Exceptions should be handled
        // Close all client connections in the finally block -- just in case they weren't taken care of elsewhere
        } finally {
            try {
                toServer.close();
                toClient.close();

                if (clientSocket != null && clientSocket.isConnected()) {
                    clientSocket.close();
                }

            } catch(IOException e) {
                parent.parent.updateStatusBox(e.getMessage());
                //TODO: Handle this IOE
            }
        }

        // Let ServerRunnable that this thread is ending so it can do housekeeping
        parent.clientThreadEnding(this);
    }

    // Checks to see if this client is still connected -- and if it is, tell it to go away
    public void closeSocket() {
        try {
            if (clientSocket != null && clientSocket.isConnected()) {
                toClient.write('q');
                toClient.flush();
                clientSocket.close();
                clientSocket.shutdownInput();
            }
        } catch(IOException e) {
            parent.parent.updateStatusBox(id + " " + e.getMessage());
            // TODO: handle IO exception
        }
    }

    // For reference, the complete list of message types (client AND server side) is presented below
    //
    // TABLE OF MESSAGE TYPES (server AND client)
    //
    //      STRING TYPE         FORMAT          OPERATION
    //  --------------------    ------------    ---------------------------------------------------------------------------------------
    //      Join Server         /join           Part of the handshake -- tells the server that the client is ready for initial commands
    //  Number of Questions     /numq#          Tells us how many questions there are
    //       Question           /ques#<str>     Gives us the string <str> of question number #
    //      Start Vote          /stvt           Standalone command: tells the client to enable voting options
    //    Selected Option       /sel#           Tells the server that this client has chosen option #
    //       Remaining          /rem#           After every received vote, the server will tell all clients how many votes remain
    //    Number of Votes       /ansr#$         Before ending the vote, the server reports that $ votes were cast for option #
    //       End Vote           /novo           Standalone command: tells the client to disable voting and generate results
    //      Leave Server        /quit           From client: requests a disconnect || From server: forces client to disconnect

    // For easy reference, the following if-else if statements follow the same order as the above table
    private void processString(String string) {

        parent.parent.updateStatusBox(string);

        // On the server side, the server performs some housekeeping when a client joins and constructs an appropriate response
        if (string.startsWith(JOIN_SERVER)) {
            parent.clientJoined(this);

        // If a client selects an option, handle it in the ServerRunnable
        } else if (string.startsWith("/sel")) {
            parent.setVote(Integer.valueOf(string.substring(4)));
            parent.parent.updateStatusBox("Client " + this.id + " chose option " + Integer.valueOf(string.substring(4)));

        // If a client requests disconnection, kill the socket
        } else if (string.startsWith(QUIT)) {
            closeSocket();
        }
    }

    // If a connection to this client exists, send a message
    public void xmitMessage(String string) {
        if (toClient != null && !clientSocket.isClosed()) {
            toClient.println(string);
        }
    }

    // Check to see if this client is still connected (used during the cleanup portion of ServerRunnable)
    public boolean isConnected() {
        if (clientSocket != null) {
            return clientSocket.isConnected();
        } else {
            return false;
        }
    }
}