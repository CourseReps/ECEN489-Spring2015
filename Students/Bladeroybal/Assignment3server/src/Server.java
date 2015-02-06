import java.net.*;
import java.io.*;
import java.util.Properties;

public class Server {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        ServerSocket Server = new ServerSocket(portNumber);
        Socket clientSocket = Server.accept();
        ObjectOutputStream out =
                new ObjectOutputStream(clientSocket.getOutputStream());

        try

         //       BufferedReader in = new BufferedReader(
         //               new InputStreamReader(clientSocket.getInputStream()));
        {
            Properties p = new Properties(System.getProperties());
            out.writeObject(p);
        }
        finally {
            Server.close();
            clientSocket.close();
        }
    }
}