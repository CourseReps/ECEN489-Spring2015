import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

class ClientRunnable implements Runnable {

    final static public String QUIT = "/quit";
    final static public String JOIN_SERVER = "/join";

    public final static int DEFAULT_PORT = 5555;

    private int[] votes = new int[4];

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
            if (socket == null || socket.isClosed()) {
                socket = new Socket(host, port);
                parent.connectButtonManip("Disconnect", true);
            }

            isConnected = true;

            toServer = new PrintWriter(socket.getOutputStream(), true);
            toClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (handshake) {
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
            parent.newMessage(e.getMessage());
            //TODO: IO Exceptions should be handled

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

        parent.connectButtonManip("Connect", true);
        parent.newMessage("Connection Closed!");
        isRunning = false;
    }


    // METHODS //
    synchronized public void processString(String string) {

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

        } else if (string.startsWith("/ques")) {

            int whichOne = Integer.valueOf(string.substring(5, 6));
            this.selected[whichOne] = true;
            this.questions[whichOne] = string.substring(6);

        } else if (string.startsWith("/rem")) {

            int remains = Integer.valueOf(string.substring(4));
            parent.remaining(remains);

        } else if (string.startsWith("/stvt")) {
            parent.setButtonText(questions, numQuestions);
            parent.newMessage("Voting has started!  Choose an option.");

        } else if (string.startsWith("/novo")) {

            final double first;
            final double second;
            final double third;
            final double fourth;
            final double sum = votes[0] + votes[1] + votes[2] + votes[3];

            first = votes[0]/sum;
            second = votes[1]/sum;
            third = votes[2]/sum;
            fourth = votes[3]/sum;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    result = new BarChrt();
                    result.draw(first, second, third, fourth);
                }
            }).start();

            int largest = 0;
            int winner = 0;
            for(int x = 0; x <= 3; x++) {
                if (votes[x] > largest) {
                    largest = votes[x];
                    winner = (x + 1);
                }
            }
            parent.newMessage("Voting has ended!  Option " + winner + " has won.");

        } else if (string.startsWith("/ansr")) {
            int whichOne = Integer.valueOf(string.substring(5, 6));
            this.votes[whichOne] = Integer.valueOf(string.substring(6));

        } else if (string.startsWith(QUIT)) {
            parent.newMessage("Server closed connection!");
            disconnect();
        }

//            parent.newMessage(string);
    }


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