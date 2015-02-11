import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import javax.swing.JOptionPane;

public class Client{

    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is\n" +
                        "running the date service on port 60025:");
        Socket socket = new Socket(serverAddress, 60025);

        PrintWriter out =
                new PrintWriter(socket.getOutputStream(), true);
        out.println(new Date().toString());
        out.println("JRE Vendor Name = " + System.getProperty("java.vendor"));
        out.println("JRE Version Number = " + System.getProperty("java.version"));
        out.println("Operating System Architecture = " + System.getProperty("os.arch"));
        out.println("Operating System Name = " + System.getProperty("os.name"));
        out.println("Operating System version = " + System.getProperty("os.version"));
        out.println("User Home Directory = " + System.getProperty("user.dir"));
        out.println("User Account Name = " + System.getProperty("user.name"));


        System.exit(0);
    }
}
