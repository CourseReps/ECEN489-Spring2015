import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.*;

/**
 * Created by kwilk_000 on 3/25/2015.
 */
public class sendCommand {

    public Socket clientSocket = null;
    public  ServerSocket server = null;
    public int port;

    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        ServerSocket server = null;
        int port = 9000;
        JSONObject command = new JSONObject();
        try {

            command.put("Command", "take picture");
            command.put("DeviceID", "1234");

        }
        catch (Exception e){
            e.printStackTrace();
        }


        int keepOpen = 0;

        System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());

        try {
            //Wait for client to connect
            server = new ServerSocket(port);
            clientSocket = server.accept();

            //once client connects, create new ClientManger object and create new thread
            System.out.println("Client Connected!");

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(command);


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
