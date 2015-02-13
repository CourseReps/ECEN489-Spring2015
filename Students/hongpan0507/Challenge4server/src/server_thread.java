/**
 * Created by hpan on 1/29/15.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class server_thread extends Thread {
    private Socket sock = null;
    private ObjectInputStream input_stream = null;
    private ObjectOutputStream output_stream = null;
    private int client_port = 0;

    //constructor
    public server_thread( Socket _sock) {
        sock = _sock;
        client_port = sock.getPort();
    }   //end of constructor

    public void run() {
        try {
            input_stream = new ObjectInputStream(sock.getInputStream());
            output_stream = new ObjectOutputStream(sock.getOutputStream());
            output_stream.flush();

            receive incoming = new receive(input_stream, sock.getInetAddress().getHostAddress(), client_port);
            send outgoing = new send(output_stream);
            incoming.start();
            outgoing.start();
            incoming.join();
            outgoing.join();
            input_stream.close();
            input_stream.close();
            sock.close();
        } catch (IOException IOE) {
            System.out.println("Error occurred when using server_thread" + IOE);
        } catch (Exception E) {
            System.out.println("Error occurred when using server_thread" + E);
        }

    }   //end of run()

}
