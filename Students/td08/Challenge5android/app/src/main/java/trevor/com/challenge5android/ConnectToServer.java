package trevor.com.challenge5android;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectToServer implements Runnable {

    private String serverIp;
    private Socket client;
    private PrintWriter printwriter;
    private BufferedReader reader;
    private String messsage = "Done";
    private DatabaseManager db;
    private int lastId;
    private int entries;

    ConnectToServer (String ip, DatabaseManager h) {
        this.serverIp = ip;
        this.db = h;
    }

    public void run() {
        try {
            client = new Socket(serverIp, 9090);  //connect to server
            printwriter = new PrintWriter(client.getOutputStream(),true);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            //wait for server ready message
            String received = null;
            received = reader.readLine();

            String sqlData = null;
            lastId = MainActivity.LASTID;

            sqlData = db.getData(lastId);
            printwriter.println(sqlData);
            entries = lastId + db.entries;
            while (lastId < entries) {
                sqlData = db.getData(lastId);
                if (sqlData.equals(null))
                    break;
                printwriter.println(sqlData);
                Log.d("ToServer", "Sent ID: " + lastId);
                lastId++;
            }

            MainActivity.LASTID = lastId;
            printwriter.write(messsage);  //write the message to output stream
            printwriter.flush();
            printwriter.close();
            client.close();   //closing the connection
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
