/**
 * Created by hpan on 1/29/15.
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class client {
    private Socket sock = null;
    private DataOutputStream output_stream = null;
    private DataInputStream input_stream = null;
    private BufferedReader user_input = null;
    private String server_ip = "";
    private int server_port = 0;

    //constructor
    public client (String _server_ip, int _server_port) {
        server_ip = _server_ip;
        server_port = _server_port;
    }   //end of constructor

    public void start () {
        System.out.println("Connecting to " + server_ip + " @ port " + server_port);
        try {
            sock = new Socket(server_ip, server_port);
            System.out.println("connected to " + sock);
            output_stream = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
            input_stream = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
            receive incoming = new receive(input_stream, server_ip, server_port);
            send outgoing = new send(output_stream);
            incoming.start();
            outgoing.start();
            incoming.join();
            outgoing.join();
            input_stream.close();
            input_stream.close();
            sock.close();
        } catch (UnknownHostException UHE) {
            System.out.println(UHE.getMessage());
        } catch (IOException IOE) {
            System.out.println(IOE.getMessage());
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
    }
}
