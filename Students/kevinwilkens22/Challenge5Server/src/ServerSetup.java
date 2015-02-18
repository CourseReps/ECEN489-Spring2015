
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSetup implements Runnable {

    //initialize socket variables
    Socket clientSocket = null;
    ServerSocket server = null;
    int port;

    ServerSetup (int port)
    {
        this.port = port;
    }

    public void run() {
        System.out.println("Setting up server...");
        DBModule db = new DBModule();
        Thread dbThread = new Thread(db);
        dbThread.start();
        System.out.println("Waiting for client connection...");

        //create socket and wait for client connection
        while (true) {
            try {
                //Wait for client to connect
                server = new ServerSocket(port);
                clientSocket = server.accept();

                //once client connects, create new ClientManger object and create new thread
                System.out.println("Client Connected!");
                ClientConnect manager = new ClientConnect(clientSocket, db);
                Thread clientThread = new Thread(manager);
                clientThread.start();
                db.createTable();

            } catch (IOException e) {
                System.out.println("Could not establish server socket on port " + port);
                return;
            }
            finally {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
