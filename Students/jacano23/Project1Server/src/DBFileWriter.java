import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.net.*;

public class DBFileWriter {

    public static void main(String[] args) throws IOException
    {
        InputStream is = null;
        int keepOpen = 0;
        byte[] aByte = new byte[1];
        int bytesRead;
        Socket clientSocket = null;
        ServerSocket server = null;
        System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());
        int port = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ServerSocket port: ", JOptionPane.YES_NO_OPTION));
        System.out.println("Server socket established on port number: " + port);
            try {
                //Wait for client to connect
                server = new ServerSocket(port);
                System.out.println("Waiting for client connection...");
                clientSocket = server.accept();
             //   is = clientSocket.getInputStream();
                System.out.println("Client Connected!");

                dbAcceptor writeDB = new dbAcceptor(clientSocket);
                Thread dbWriteThread = new Thread(writeDB);
                dbWriteThread.start();
            }
            catch(IOException ex) {
                System.out.println("Could not establish server socket on port " + port);
                return;
            }
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

        while (true) {
            //ConnectToClient(9090);
            if ((keepOpen = JOptionPane.showConfirmDialog(null, "Continue with server operations?", "Make Selection", JOptionPane.YES_NO_OPTION)) == 1) {
                break;
            }
        }
        System.exit(0);
        }


    }

