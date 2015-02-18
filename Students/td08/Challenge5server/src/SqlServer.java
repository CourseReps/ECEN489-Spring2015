import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlServer
{
    public static void main(String[] args) throws IOException
    {
        int keepOpen = 0;
        int port = 9090;
        System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());

        ServerSetup setup = new ServerSetup(port);
        Thread setupThread = new Thread(setup);
        setupThread.start();

        while (true) {
            //ConnectToClient(9090);
            if ((keepOpen = JOptionPane.showConfirmDialog(null, "Continue with server operations?", "Make Selection", JOptionPane.YES_NO_OPTION)) == 1) {
                break;
            }
        }
        System.exit(0);
    }
} 