import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.net.*;


/**
 * Created by kwilk_000 on 2/22/2015.
 */

public class DBFileWriter {
    static int numberFiles = 0;

    public static void main(String[] args) throws IOException {
        InputStream is = null;
        int keepOpen = 0;
        byte[] aByte = new byte[1];
        int bytesRead;
        int port = 0;
        Socket clientSocket = null;
        ServerSocket server = null;
        //System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());

        while(true) {
            try {
                port = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ServerSocket port: ", JOptionPane.YES_NO_OPTION));
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println("Server socket established on port number: " + port);

        while (true) {
            System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Server socket established on port number: " + port);
            try {
                //Wait for client to connect
                server = new ServerSocket(port);
                System.out.println("Waiting for client connection...");
                clientSocket = server.accept();
                //   is = clientSocket.getInputStream();
                System.out.println("Client Connected!");

                System.out.println("number of files read " + numberFiles);

                dbAcceptor writeDB = new dbAcceptor(clientSocket);

                writeDB.dbAccept();

            } catch (IOException ex) {
                System.out.println("Could not establish server socket on port " + port);
                return;
            }

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

                //ConnectToClient(9090);
                /*if ((keepOpen = JOptionPane.showConfirmDialog(null, "Continue with server operations?", "Make Selection", JOptionPane.YES_NO_OPTION)) == 1) {
                    break;
                }*/

        }
        //System.exit(0);
    }

}

