/**
 * Created by hpan on 1/29/15.
 */

import java.io.ObjectInputStream;

public class receive extends Thread{
    private ObjectInputStream input_stream = null;
    private String message = "";
    private String server_ip = "";
    private int server_port = 0;

    public receive(ObjectInputStream _input_stream, String _server_ip, int port){
        input_stream = _input_stream;
        server_ip = _server_ip;
        server_port = port;
    }	//end of constructor

    public void run(){
        try{
            while(message != ".disconnect"){
                message = input_stream.readUTF();
                System.out.print("Server @ " + server_ip + "_" + server_port + ": ");
                System.out.println(message);
            }	//end of while
            System.out.println("Connection terminated by the client @ " + server_ip + " + " + server_port);
            input_stream.close();
        } catch (Exception e) {
            System.out.println("Error occurred when receiving message");
        }	//end of try block
    }	//end of run	
}

