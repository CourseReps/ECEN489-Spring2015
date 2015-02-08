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

    private void processString(String string) {

        parent.parent.updateStatusBox(string);

        if (string.startsWith("/sel")) {
            parent.setVote(Integer.valueOf(string.substring(4)));
            parent.parent.updateStatusBox("Client " + this.id + " chose option " + Integer.valueOf(string.substring(4)));

        } else if (string.startsWith(QUIT)) {
            closeSocket();

        } else if (string.startsWith(JOIN_SERVER)) {
            parent.clientJoined(this);

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