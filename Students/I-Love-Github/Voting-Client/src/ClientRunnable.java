import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

// This class handles all client-server interactions on the client side
class ClientRunnable implements Runnable {

    // This list of message types is incomplete -- more can be found down in processMessage(String message)
    final static public String QUIT = "/quit";
    final static public String JOIN_SERVER = "/join";

    // Default server port
    public final static int DEFAULT_PORT = 5555;

    // At the end of the vote, the server reports back the results.  Those results are stored in this array.
    private int[] votes = new int[4];

    // Connection status booleans
    public boolean isRunning;
    public boolean isConnected;
    private boolean disconnect;

    // Fields inhereted from parent
    private ClientPanel parent;
    private String host;
    private int port;

    // Socket fields
    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader toClient;

    // Voting fields
    private int numQuestions = 0;
    private boolean[] selected = new boolean[4];
    private String[] questions = new String[4];
    private BarChrt result;


    // CONSTRUCTOR
    // Assigns inherited fields and initializes connection status fields
    ClientRunnable(ClientPanel parent, String host, int port, String userName) {
        this.parent = parent;
        this.host = host;
        this.port = port;

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
            // If a socket doesn't exist, create one and tell the user (connect button)
            if (socket == null || socket.isClosed()) {
                socket = new Socket(host, port);
            parent.connectButtonManip("Disconnect", true);
            }

            // Socket command is blocking, the status and datastreams don't get updated until a connection is made
            isConnected = true;
            toServer = new PrintWriter(socket.getOutputStream(), true);
            toClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Handshake is true by default -- perform handshake with server
            if (handshake) {
                toServer.println(JOIN_SERVER);
            processString(toClient.readLine());
            parent.newMessage("Connected to host at " + String.valueOf(host) + ":" + String.valueOf(port));
            }

            // Connection loop -- read received strings and process them accordingly
            // Send operations are generated automatically or through the user interface -- typically on another thread
            while (!disconnect && socket.isConnected()) {
                stringIn = toClient.readLine();

            if (stringIn != null && stringIn.length() > 0) {
                processString(stringIn);
                }
            }

        // Catch blocks are required by try statements -- just reports back to the user right now
        } catch(IOException e) {
            parent.newMessage(e.getMessage());
        //TODO: IO Exceptions should be handled

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
            } catch(IOException e) {
                parent.newMessage(e.getMessage());
            }
        }

    // Update the UI to allow the user to reconnect, then end
    parent.connectButtonManip("Connect", true);
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
    //  Number of Questions     /numq#          Tells us how many questions there are
    //       Question           /ques#<str>     Gives us the string <str> of question number #
    //      Start Vote          /stvt           Standalone command: tells the client to enable voting options
    //    Selected Option       /sel#           Tells the server that this client has chosen option #
    //       Remaining          /rem#           After every received vote, the server will tell all clients how many votes remain
    //    Number of Votes       /ansr#$         Before ending the vote, the server reports that $ votes were cast for option #
    //       End Vote           /novo           Standalone command: tells the client to disable voting and generate results
    //      Leave Server        /quit           From client: requests a disconnect || From server: forces client to disconnect

    // For easy reference, the following if-else if statements follow the same order as the above table
    synchronized public void processString(String string) {

        // Fail-safe -- we shouldn't get any null strings, but this will keep us from throwing NullExceptions
        if (string == null) {
            return;
        }

        if (string.startsWith("/numq")) {
            parent.clearButtons();
            this.numQuestions = Integer.valueOf(string.substring(5));

        } else if (string.startsWith("/ques")) {

            int whichOne = Integer.valueOf(string.substring(5, 6));
            this.selected[whichOne] = true;
            this.questions[whichOne] = string.substring(6);

        } else if (string.startsWith("/stvt")) {
            parent.setButtonText(questions, numQuestions);
            parent.newMessage("Voting has started!  Choose an option.");

        } else if (string.startsWith("/rem")) {

            int remains = Integer.valueOf(string.substring(4));
            parent.remaining(remains);

        } else if (string.startsWith("/ansr")) {
            int whichOne = Integer.valueOf(string.substring(5, 6));
            this.votes[whichOne] = Integer.valueOf(string.substring(6));

        } else if (string.startsWith("/novo")) {

            // Do some math to find the percentage of total votes that each option received
            final double first;
            final double second;
            final double third;
            final double fourth;
            final double sum = votes[0] + votes[1] + votes[2] + votes[3];

            first = votes[0]/sum;
            second = votes[1]/sum;
            third = votes[2]/sum;
            fourth = votes[3]/sum;

            // Generate a bar chart (occurs in a separate thread to absorb crashes from multiple calls to result.draw()
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result = new BarChrt();
                    result.draw(first, second, third, fourth);
                }
            }).start();

            // Calculate the winner and report to the user
            int largest = 0;
            int winner = 0;
            for(int x = 0; x <= 3; x++) {
                if (votes[x] > largest) {
                    largest = votes[x];
                    winner = (x + 1);
                }
            }
            parent.newMessage("Voting has ended!  Option " + winner + " has won.");

        } else if (string.startsWith(QUIT)) {
            parent.newMessage("Server closed connection!");
            disconnect();
        }
    }

    // PUBLIC METHODS //

    // This method sends messages to the server
    // The if-else statement may be redundant at this point -- it is a vestige of the old chat server architecture
    //      this program is built upon
    synchronized public void sendString(String stringOut) {
        if (toServer != null && socket != null && socket.isConnected()) {

            if (stringOut.startsWith(QUIT)) {
                toServer.println(QUIT);
            } else if (stringOut.startsWith(JOIN_SERVER)) {
                toServer.println(JOIN_SERVER);
            } else {
                toServer.println(stringOut);
            }
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
}