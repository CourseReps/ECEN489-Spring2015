import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import javax.swing.JOptionPane;

public class Client {

    public static void main(String[] args) {
        String serverAddress = JOptionPane.showInputDialog("Enter IP address of server:");

        try {
            Socket socket = new Socket(serverAddress, 9090);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to Server!");

            //gets period of time, in seconds, to collect data
            int collectTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Collection Time (seconds)"));

            //wait for initial server welcome message
            String init = input.readLine();
            System.out.println(init);

            //output date, IP address, and mouse location
            int  i = 0;
            while (i < collectTime) {
                output.println(new Date().toString());
                output.println(InetAddress.getLocalHost().getHostAddress());
                output.println(String.valueOf(MouseInfo.getPointerInfo().getLocation().getX()));
                output.println(String.valueOf(MouseInfo.getPointerInfo().getLocation().getY()));
                i++;
                Thread.sleep(1000);
            }
            //exit keyword sent when finished sending data
            output.println("DONE");
            System.out.println("Finished!");

            //close connections and streams
            input.close();
            output.close();
            socket.close();
            System.exit(0);
        }
        catch (SocketException se) {
            System.out.println("Host unreachable! Please try again later.");
            System.exit(-1);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
