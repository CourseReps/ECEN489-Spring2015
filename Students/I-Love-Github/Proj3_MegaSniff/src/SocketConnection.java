import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnection implements Runnable {

    private int port = 11111;

    private MainRunnable parent;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public BufferedReader fromClient = null;
    public PrintWriter toClient = null;

    private boolean stayAlive;
    private boolean isConnected;

    public int leftAnt = 0;
    public int rightAnt = 0;
    public static int VERTICAL = 0;
    public static int HORIZONTAL = 1;

    SocketConnection(MainRunnable parent) {
        this.parent = parent;
        stayAlive = true;
        isConnected = false;
    }

    public void run() {

        while (stayAlive) {

            // CONNECT CLIENT
            try {
                System.out.print("Listening for clients on port: " + String.valueOf(port) + "\n");
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();

                System.out.print("Client connected\n");
                serverSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } finally {}

            String stringIn;

            try {
                toClient = new PrintWriter(this.clientSocket.getOutputStream(), true);
                fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

                System.out.print("Created data streams\n");
                isConnected = true;
                parent.setOperatingMode(MainRunnable.MODE_USER_INPUT);
                parent.getUI().updateClientStatus(true);

                while (clientSocket.isConnected()) {

                    stringIn = fromClient.readLine();
//                    System.out.println(stringIn);

                    if (stringIn != null && stringIn.length() > 0) {

                        processInput(Integer.parseInt("" + stringIn.charAt(0)));

                    } else {
                        return;
                    }
                }
            } catch(IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();

            } finally {

                isConnected = false;
                parent.setOperatingMode(MainRunnable.MODE_CYCLE);
                parent.getUI().updateClientStatus(false);

                try {
                    fromClient.close();
                    toClient.close();

                    if (this.clientSocket != null && this.clientSocket.isConnected()) {
                        this.clientSocket.close();
                    }

                } catch(IOException e) {
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                    //TODO: Handle this IOE
                }
            }
        }
    }

    public void simulateInput(int value) {
        processInput(value);
    }


    private void processInput(int value) {

        if (value == 0) {
            parent.getSerialComm().sendString(String.valueOf(3));
            parent.getSerialComm().sendString(String.valueOf(1));
            parent.getUI().setAntennas(true, true);
            this.leftAnt = VERTICAL;
            this.rightAnt = VERTICAL;

        } else if (value == 1) {
            parent.getSerialComm().sendString(String.valueOf(3));
            parent.getSerialComm().sendString(String.valueOf(0));
            parent.getUI().setAntennas(false, true);
            this.leftAnt = VERTICAL;
            this.rightAnt = HORIZONTAL;

        } else if (value == 2) {
            parent.getSerialComm().sendString(String.valueOf(2));
            parent.getSerialComm().sendString(String.valueOf(1));
            parent.getUI().setAntennas(true, false);
            this.leftAnt = HORIZONTAL;
            this.rightAnt = VERTICAL;

        } else if (value == 3) {
            parent.getSerialComm().sendString(String.valueOf(2));
            parent.getSerialComm().sendString(String.valueOf(0));
            parent.getUI().setAntennas(false, false);
            this.leftAnt = HORIZONTAL;
            this.rightAnt = HORIZONTAL;
        }
    }

    public void disableSocket() {

        stayAlive = false;

        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}