import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection implements Runnable {

    final static public String QUIT = "/quit";
    final static public String JOIN_SERVER = "/join";

    final public int id;

    private String userName;
    private ServerRunnable parent;

    private Socket clientSocket;
    private PrintWriter toClient;

    ClientConnection(ServerRunnable parent, Socket clientSocket, int id) {
        this.clientSocket = clientSocket;
        this.parent = parent;
        this.id = id;
    }

    public void run() {

        boolean handshake = true;

        BufferedReader toServer = null;

        try {
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            toServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String stringIn;

            while (clientSocket.isConnected()) {
                stringIn = toServer.readLine();

                if (stringIn != null && stringIn.length() > 0) {
                    processString(stringIn);
                }
            }
            parent.parent.updateStatusBox("Stopping client from ClientConnection");

        } catch(IOException e) {
            parent.parent.updateStatusBox(id + " " + e.getMessage());

            //TODO: IO Exceptions should be handled
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

        parent.clientThreadEnding(this);
    }

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


    public void xmitMessage(String string) {
        if ((userName != null || toClient != null) && !clientSocket.isClosed()) {
            toClient.println(string);
        }
    }

    public String getUserName() {
        return userName;
    }

    public boolean isConnected() {
        if (clientSocket != null) {
            return clientSocket.isConnected();
        } else {
            return false;
        }
    }
}