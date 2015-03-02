import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.io.*;
import java.net.*;

public class DBFileWriter {
    static int numberFiles = 0;

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
                System.out.println("Client Connected!");
                BufferedReader numFiles = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                numberFiles = Integer.parseInt(numFiles.readLine());
                System.out.println("number of files read " + numberFiles);

                dbAcceptor writeDB = new dbAcceptor(clientSocket);
                writeDB.dbAccept();

            }
            catch(IOException ex){
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

            if ((keepOpen = JOptionPane.showConfirmDialog(null, "Continue with server operations?", "Make Selection", JOptionPane.YES_NO_OPTION)) == 1) {
                break;
            }
        }
        System.exit(0);
        }


    }

