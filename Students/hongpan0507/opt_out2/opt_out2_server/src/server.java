/**
 * Created by hpan on 1/29/15.
 */
import java.net.*;
import java.io.*;

public class server {
    private ServerSocket server_sock = null;
    private Socket sock = null;
    private int port_num = 0;
    private int max_num = 0;

    //constructor
    public server(int _port_num, int _max_num) {
        port_num = _port_num;
        max_num = _max_num;
    }   //end of constructor

    public void start_server() {
        try {
            System.out.println("Binding to port: " + port_num);
            server_sock = new ServerSocket(port_num, max_num);
            System.out.println("Server running: " + server_sock);
            System.out.println("Waiting for clients to connect...");
            while (true) {
                try {
                    sock = server_sock.accept();
                    if (sock != null) {
                        server_thread client_connection = new server_thread(sock);
                        System.out.println(sock.getInetAddress() + " Connected");
                        client_connection.start();
                    }
                } catch (IOException IOE) {
                    System.out.println("server socket accept() error " + IOE);
                }
            }   //end of while
        } catch (IOException IOE) {
            System.out.println(IOE);
        }
    }   //end of start_sever()
}   // end of server
