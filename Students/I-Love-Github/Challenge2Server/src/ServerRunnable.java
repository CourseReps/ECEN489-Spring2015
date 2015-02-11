import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ServerRunnable implements Runnable {

    public final static int DEFAULT_PORT = 6668;
    public final static int IMGPORT = 6667;

    public ServerPanel parent;
    public boolean isRunning;
    public int port;
    ServerSocket serverSocket = null;

    private boolean stopPressed;
    private int numClients;
    private int idCounter;

//    private Vector<ClientThreadItem> clientVector;
    private Vector<Thread> clientThreadList;
    private Vector<ClientConnection> clientSocketList;
//    private Vector<Integer> removeList;
    private Vector<String> nameList;

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

        if (nameList == null || nameList.isEmpty()) {
            nameList = new Vector<String>();
        }

//        if (clientVector == null || clientVector.isEmpty()) {
//            clientVector = new Vector<ClientThreadItem>();
//        }

        if (clientSocketList == null || clientSocketList.isEmpty()) {
            clientThreadList = new Vector<Thread>();
        }

        if (clientThreadList == null || clientThreadList.isEmpty()) {
            clientSocketList = new Vector<ClientConnection>();
        }

//        if (removeList == null || removeList.isEmpty()) {
//            removeList = new Vector<Integer>();
//        }

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

//                Iterator removeItems = removeList.iterator();

//                while (removeItems.hasNext()) {
//
//                    clientSocketList.remove(removeItems.next());
//                    removeItems.remove();
//                }

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

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    getThisThread().interrupt();
//                } catch (InterruptedException e) {
//                    // This thread will never be interrupted
//                }
//            }
//        }).start();

//        while (clientSocketList.size() > 0) {
//            try {
//                Thread.sleep(100);
//                Iterator removeItems = removeList.iterator();
//
//                while (removeItems.hasNext()) {
//
//                    clientSocketList.remove(removeItems.next());
//                    removeItems.remove();
//                }
//            } catch (InterruptedException e) {
//                parent.updateStatusBox("Forced disconnect timed out for " + clientSocketList.size() + " clients.");
//                clientSocketList = new Vector<ClientConnection>();
//                clientSocketList = new Vector<ClientConnection>();
//            }
//        }

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
        broadcastMsg(ClientConnection.USERLIST_ADD + clientConnection.getUserName());
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

//            parent.updateStatusBox("Stopping client from ServerRunnable disconnectClient method");
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

//        removeList.add(clientSocketList.indexOf(clientConnection));
        clientThreadList.remove(clientSocketList.indexOf(clientConnection));
        clientSocketList.remove(clientSocketList.indexOf(clientConnection));
        numClients--;

        nameList.remove(nameList.indexOf(clientConnection.getUserName()));
        broadcastMsg(ClientConnection.USERLIST_REMOVE + clientConnection.getUserName());

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

    synchronized public String checkUserName(String name, String oldName) {
        Iterator<String> iterator;
        boolean doOver = true;
        int incrementer = 1;
        String returnName = name;

        while (doOver) {

            doOver = false;
            iterator = nameList.iterator();

            while (iterator.hasNext()) {
                if (returnName.equals(iterator.next())) {
                    doOver = true;
                    returnName = name + String.valueOf(incrementer);
                    incrementer++;
                }
            }
        }

        nameList.add(returnName);

        if (oldName != null) {
            nameList.remove(nameList.indexOf(oldName));
        }
        return returnName;
    }

    // SETTERS AND GETTERS //

    public Vector<String> getUserList() {
        return nameList;
    }

    private Thread getThisThread() {
        return parent.getServerThread();
    }

    public int getNumClients() {
        return numClients;
    }
}