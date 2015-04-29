import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Rong Liu on 4/29/2015.
 */
public class macCheckIn {

    public void macCheckIn(String userName, Integer timpstamp) throws IOException {
        JSONObject jsonRequest = JSONWrapping.getCheckInJSON(userName,"college station",timpstamp,"mac");
        String serverAddress =new String( "165.91.210.26");
        Socket s = new Socket(serverAddress, 9898);
        PrintWriter out =
                new PrintWriter(s.getOutputStream(), true);
        out.print(jsonRequest + "\r\n"); // send the response to client
        out.flush();
        s.close();
    }
}
