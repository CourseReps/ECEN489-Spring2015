/**
 * Created by hpan on 1/29/15.
 */

public class chat_server {
    public static  void main (String args[]) {
        server chat_server = null;
        if (args.length != 2) {
            System.out.println("Usage: java chat_server port_number max_connection");
        } else {
            chat_server = new server(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            chat_server.start_server();
        }
    }
}


