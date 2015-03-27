import java.io.*;
import java.net.*;

public class Server {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        String input = "";

        InetAddress ipAddress = InetAddress.getLocalHost();
        System.out.println("Current Server IP Address: " + ipAddress.getHostAddress());   //prints the array in a window
        ServerSocket listener = new ServerSocket(9090);  //creates new server socket on port 9000

        try {
            while (true) {
                System.out.println("Waiting for connection");
                Socket socket = listener.accept();  //listens for the accept from client
                System.out.println("Client connected");
                BufferedReader readerB = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!input.equals("quit")) {
                    input = readerB.readLine();
                    System.out.println(input);
                }
            }
        }
        finally {
            listener.close();
        }


    }
}
