import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ServerRunnable implements Runnable {

    public final static int DEFAULT_PORT = 5555;

    public ServerPanel parent;
    public boolean isRunning;
    public int port;
    ServerSocket serverSocket = null;

    private boolean stopPressed;
    private int numClients;
    private int idCounter;

    private int[] votes = new int[4];

    private boolean voting;

    private int numQuestions = 0;
    private boolean[] selected = new boolean[4];
    private String[] questions = new String[4];

    private Vector<Thread> clientThreadList;
    private Vector<ClientConnection> clientSocketList;


    // CONSTRUCTOR
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

        if (clientSocketList == null || clientSocketList.isEmpty()) {
            clientThreadList = new Vector<Thread>();
        }

        if (clientThreadList == null || clientThreadList.isEmpty()) {
            clientSocketList = new Vector<ClientConnection>();
        }

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
                parent.updateStatusBox("Listening for clients on port: " + String.valueOf(port));
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();
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

        parent.updateStatusBox("Stopping server...");

        // Server cleanup
        Vector<ClientConnection> snapshot = new Vector<ClientConnection>();
        snapshot.addAll(clientSocketList);
        Iterator<ClientConnection> clientIterator = snapshot.iterator();
        ClientConnection clientConnection;

        while (clientIterator.hasNext()) {
            clientConnection = clientIterator.next();
            parent.updateStatusBox("Stopping client from ServerRunnable Loop");
            disconnectClient(clientConnection);
        }
        snapshot.clear();

        parent.updateStatusBox("\nSERVER SHUTDOWN COMPLETE");
        parent.setStatus(Color.RED, "OFFLINE");

        isRunning = false;
        parent.setButtonEnabled(parent.GO_BUTTON, true);
    }


    // METHODS //
    synchronized public void clientConnected(Socket socket) {

        ClientConnection clientConnection = new ClientConnection(this, socket, idCounter);
        Thread clientSocketThread = new Thread(clientConnection);
        clientSocketThread.start();

        clientSocketList.add(clientConnection);
        clientThreadList.add(clientSocketList.indexOf(clientConnection), clientSocketThread);

        numClients++;
        idCounter++;

        parent.updateStatusBox("Client " + clientConnection.getUserName() + " has connected to port " + port);
        parent.updateStatusBox("There are " + numClients + " clients connected\n");
    }


    synchronized public void clientJoined(ClientConnection clientConnection) {
        clientConnection.xmitMessage("/numq" + numQuestions);

        if (voting) {
            for (int x = 0; x <= (numQuestions - 1); x++) {
                clientConnection.xmitMessage("/ques" + String.valueOf(x) + questions[x]);
            }

            clientConnection.xmitMessage("/stvt");
        }
    }


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


    synchronized public void disconnectClient(ClientConnection clientConnection) {

        if (clientConnection != null && clientConnection.isConnected() &&
                clientSocketList.get(clientSocketList.indexOf(clientConnection)) != null) {

            clientConnection.xmitMessage(ClientConnection.QUIT);
            clientSocketList.get(clientSocketList.indexOf(clientConnection)).closeSocket();
            parent.updateStatusBox(clientSocketList.indexOf(clientConnection) + " " + "Forcing disconnection of client " +
                    clientConnection.getUserName() + " on port " + port + "...");
        } else {
            clientThreadList.get(clientSocketList.indexOf(clientConnection)).interrupt();
            parent.updateStatusBox("Closing listen socket on port " + port + "...");
        }
    }


    synchronized public void clientThreadEnding(ClientConnection clientConnection) {

        parent.updateStatusBox(clientSocketList.indexOf(clientConnection) + " " + "Client " + clientConnection.getUserName() +
                " disconnected, removing from connection pool");

        clientThreadList.remove(clientSocketList.indexOf(clientConnection));
        clientSocketList.remove(clientSocketList.indexOf(clientConnection));
        numClients--;

        parent.updateStatusBox("There are " + numClients + " clients connected");
    }


    synchronized public void broadcastMsg(String message) {

        Iterator<ClientConnection> clientIterator = clientSocketList.iterator();
        ClientConnection clientConnection;
        while (clientIterator.hasNext()) {
            clientConnection = clientIterator.next();
            clientConnection.xmitMessage(message);
        }
    }


    public void setQuestions(boolean[] selected, String[] questions, int numQuestions) {

        this.numQuestions = numQuestions;
        this.questions = questions;
        this.selected = selected;
    }


    public void startVote() {

        broadcastMsg("/numq" + numQuestions);

        for (int x = 0; x <= (numQuestions - 1); x++) {
            broadcastMsg("/ques" + x + questions[x]);
        }
        broadcastMsg("/stvt");
        parent.updateStatusBox("Voting started!  " + numQuestions + " questions presented to clients.");
    }


    public void setVoting(boolean voting) {
        this.voting = voting;
    }


    public void setVote(int voteNo) {

        this.votes[voteNo]++;

        int totalVotes = votes[0] + votes[1] + votes[2] + votes[3];
        int votesRemaining = numClients - totalVotes;
        broadcastMsg("/rem" + votesRemaining);
        parent.updateStatusBox(totalVotes + " votes cast out of " + numClients + " clients.  "
                + votesRemaining + " votes remain.");
    }


    public void stopVote() {
        for (int x = 0; x <= (numQuestions - 1); x++) {
            broadcastMsg("/ansr" + x + votes[x]);
        }
        broadcastMsg("/novo");

        int largest = 0;
        int winner = 0;
        for(int x = 0; x <= 3; x++) {
            if (votes[x] > largest) {
                largest = votes[x];
                winner = (x + 1);
            }
        }
        parent.updateStatusBox("Voting ended!  Option " + winner + " has won.");

        for (int x = 0; x <= 3; x++) {
            this.votes[x] = 0;
        }
    }
}