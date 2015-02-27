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
            catch(IOException ex){
                    System.out.println("Could not establish server socket on port " + port);
                    return;
            }

                //once client connects, create new ClientManger object and create new thread

                               // db.createTable();
              /*  ByteArrayOutputStream baos = new ByteArrayOutputStream();

                if (is != null) {

                    FileOutputStream fos = null;
                    BufferedOutputStream bos = null;
                    try {
                        fos = new FileOutputStream("D:\\Softwares\\SQLite\\sqlite-shell-win32-x86-3080801\\AndroidTest.db");
                        bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(aByte, 0, aByte.length);
                        System.out.println("Server is Still Running, bytesRead Complete, input stream is not null.");
                        do {
                            baos.write(aByte);
                            bytesRead = is.read(aByte);
                        } while (bytesRead != -1);
                        System.out.println("Server is Still Running, exited bytesRead != 1 loop.");

                        bos.write(baos.toByteArray());
                        bos.flush();
                        bos.close();
                        clientSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        // Do exception handling
                    }
                }

                else
                    System.out.println("Server received null from client");*/


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

