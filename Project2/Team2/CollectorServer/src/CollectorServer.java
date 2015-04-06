import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by joshuacano on 3/26/15.
 */
public class CollectorServer {

    public static void main(String[] args) {
        try {
            System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());
        } catch(UnknownHostException e) {
            e.printStackTrace();
        }
        Master master = new Master();
    }
}