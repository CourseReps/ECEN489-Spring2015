import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by joshuacano on 3/26/15.
 */
public class server {
    public final static int SOCKET_PORT = 4444;

    public static void main(String[] args) throws IOException {
        System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());
        ServerSocket serverSocket = new ServerSocket(SOCKET_PORT);

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                startHandler(socket);
            }
        } finally {
            serverSocket.close();
        }
    }

    private static void startHandler(final Socket socket) throws IOException{
        System.out.println("Client Connected");
        Thread thread = new Thread() {
            @Override
            public void run(){
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

                    String line = reader.readLine();
                    JSONObject jsonObject = new JSONObject(line);

                    writer.write(jsonObject.toString() + "\n");
                    writer.flush();

                    System.out.println("Received from Client: \n" + jsonObject.toString());
                } catch(IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    closeSocket();
                }
            }

            private void closeSocket() {
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        };
        thread.start();

    }
}