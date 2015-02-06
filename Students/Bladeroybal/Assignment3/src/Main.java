import java.io.*;
import java.net.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length != 2){
            System.err.println("Usage: Java Client <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Socket socket = new Socket(hostName, portNumber);
        BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        String serverOutput;
        Properties p;

        try {
            p = (Properties) ois.readObject();
            p.list(System.out);
            //p.list(read.readLine());
            //System.out.println(serverOutput);
            //serverOutput = read.readLine(); how to read string in from server
        }
        catch (Exception e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
    }
}
