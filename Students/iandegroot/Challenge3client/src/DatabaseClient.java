import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class DatabaseClient {

    public static void main(String[] args) throws IOException {


        String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is\n running the date service on port 9090:");

        //String serverAddress = "127.0.0.1";

        //Create a socket object on the address given
        Socket s = new Socket(serverAddress, 9090);

        System.out.println("Connected to server. Beginning to send cursor position data every 3 seconds.");
        System.out.println("Place cursor in top left corner to end the connection.\n");

        //Create stream for sending objects
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

        try {
            Point p = MouseInfo.getPointerInfo().getLocation();

            //Get the cursor info and send it to the server every 3 seconds
            while (p.getX() != 0 || p.getY() != 0) {
                p = MouseInfo.getPointerInfo().getLocation();

                System.out.println("Sending Point (" + p.getX() + ", " + p.getY() + ") to the server");

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {

                }
                oos.writeObject(p);
            }
        }
        finally {
            //Close the stream
            oos.close();
            System.exit(0);
        }
    }
}