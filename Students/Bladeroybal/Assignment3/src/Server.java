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

        System.out.println("Server Running. Waiting for Client to Connect");
        
        Socket clientSocket = Server.accept();
        ObjectOutputStream out =
                new ObjectOutputStream(clientSocket.getOutputStream());
        //PrintWriter out2 = new PrintWriter(clientSocket.getOutputStream(), true); //If I wanted to send string by string


        try
        {
            Properties p = new Properties(System.getProperties());
            out.writeObject(p);
         //   out2.println(System.getProperty("java.class.path")); //Pushing out string by string
        }
        finally {
            Server.close();
            clientSocket.close();
        }
    }
}