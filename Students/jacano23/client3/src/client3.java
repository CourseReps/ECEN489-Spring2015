import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by joshuacano on 3/26/15.
 */
public class client3 {
    public final static int SOCKET_PORT = 4444;

    public static void main(String[] args) throws IOException, JSONException {

        Socket socket = new Socket("localhost", SOCKET_PORT);
        System.out.println("Connected to Server");
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Command", "TakePicture");

        writer.write(jsonObject.toString() + "\n");
        writer.flush();

        String line = reader.readLine();
        jsonObject = new JSONObject(line);

        System.out.println("Received from Server: \n" + jsonObject.toString());

        socket.close();


    }
}