/**
 * Created by hpan on 1/29/15.
 */
public class chat_client {
    public static void main (String args[]) {
        client chat_client = null;
        if (args.length != 2) {
            System.out.println("Usage: java chat_client server_ip server_port");
        } else {
            chat_client = new client(args[0], Integer.parseInt(args[1]));
            chat_client.start();
        }
    }
}
