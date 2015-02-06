
import java.io.*; // ioexception and objectoutputstream
import java.net.*;
import java.util.*;// Date and Scanner

public class ServerByPranay {

    public static void main(String[] args) {
        // write your code here

        try {
            ServerSocket server = new ServerSocket(6012);
            System.out.println("Server IP address:" + InetAddress.getLocalHost().getHostAddress() + "\nServer PORT number:6012");
            System.out.println("Waiting for client connection to this port");

            Socket socket = server.accept();

            System.out.println("\nClient connected!\n");
            ObjectInputStream serverInput =new ObjectInputStream(socket.getInputStream());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());
            System.out.printf("%s", serverInput.readObject());

            System.out.println("Data received from client: ");

            serverInput.close();
            socket.close();




        }
        catch (UnknownHostException e) {
            System.err.println("ip address error");
        } catch (IOException e1) {
            System.err.println("Couldn't get I/O connection ");
        }
        catch (ClassNotFoundException e1) {
            System.err.println("Couldn't get I/O connection ");
        }

    }
}
