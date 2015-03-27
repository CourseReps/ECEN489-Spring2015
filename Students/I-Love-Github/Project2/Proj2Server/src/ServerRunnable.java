import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerRunnable implements Runnable {

    public int port = 1337;
    public boolean ping;

    private ArrayList<ClientConnection> clientSockets;

    public void run() {

        while (true) {
            try {
                System.out.print("Listening for clients on port: " + String.valueOf(port));
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(new ClientConnection(clientSocket, this));

            } catch (IOException e) {
                System.out.print(e.getMessage());
            } finally {
            }
        }
    }

    public ArrayList<ClientConnection> getClientSockets() {
        return clientSockets;
    }

    public void gotPing() {
        ping = true;
    }

    public void resetPing() {
        ping = false;
    }
}
