import java.io.*;
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
        JSONObject command1 = new JSONObject();
        try {

            command.put("command", "takePicture");
            command.put("timestamp", "123456789");
            System.out.println(command.toString());

            command1.put("command", "takePicture");
            command1.put("timestamp", "111222333");
            System.out.println(command1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        int keepOpen = 0;

        System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());

        while (true) {
            try {
                //Wait for client to connect
                server = new ServerSocket(port);
                clientSocket = server.accept();

                //once client connects, create new ClientManger object and create new thread
                System.out.println("Client Connected!");

                BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                System.out.println(read.readLine());

                OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
                out.write(command.toString());
//                out.flush();
//                out.close();

                System.out.println("sent first command");

                System.out.println(read.readLine());

//                out = new OutputStreamWriter(clientSocket.getOutputStream());
                out.write(command1.toString());
//                out.flush();
//                out.close();

                Thread.sleep(2000);

                System.out.println("sent second command");

                System.out.println(read.readLine());

                out.close();
                System.out.println("Command sent to Client.");

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
