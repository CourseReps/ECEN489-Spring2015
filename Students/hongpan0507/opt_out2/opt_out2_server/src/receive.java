/**
 * Created by hpan on 1/29/15.
 */
import java.io.*;

public class receive extends Thread{
    private DataInputStream input_stream = null;
    private String message = "";
    private String client_ip = "";
    private int client_port = 0;

    public receive(DataInputStream _input_stream, String _client_ip, int port){
        input_stream = _input_stream;
        client_ip = _client_ip;
        client_port = port;
    }	//end of constructor

    public void run(){
        try{
            while(message != ".disconnect"){
                message = input_stream.readUTF();
                System.out.print("Client @ " + client_ip + "_" + client_port + ": ");
                System.out.println(message);
            }	//end of while
            System.out.println("Connection terminated by the client @ " + client_ip + " + " + client_port);
            input_stream.close();
        } catch (Exception e) {
            System.out.println("Error occurred when receiving message");
        }	//end of try block
    }	//end of run	
}

