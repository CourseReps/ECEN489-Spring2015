/**
 * Created by hpan on 1/29/15.
 */
import java.io.*;

public class receive extends Thread{
    private ObjectInputStream input_stream = null;
    private String message = "";
    private int x_loc = 0;
    private int y_loc = 0;
    private String client_ip = "";
    private int client_port = 0;

    public receive(ObjectInputStream _input_stream, String _client_ip, int port){
        input_stream = _input_stream;
        client_ip = _client_ip;
        client_port = port;
    }	//end of constructor

    public void run(){
        try{
            database sql_db = new database("opt_out3.db");
            sql_db.open_db();
            sql_db.create_table("Mouse");
            mouse_pos position = new mouse_pos();
            while(message != ".disconnect"){
                position = (mouse_pos)input_stream.readObject();
                sql_db.insert_val("Mouse", position.x_loc, position.y_loc);
                System.out.print("Client @ " + client_ip + "_" + client_port + ": ");
                System.out.print("x location = " + position.x_loc);
                System.out.println(" y location = " + position.y_loc);
            }	//end of while
            System.out.println("Connection terminated by the client @ " + client_ip + " + " + client_port);
            input_stream.close();
            sql_db.close_db();
        } catch (Exception e) {
            System.out.println("Error occurred when receiving message" + e);
        }	//end of try block
    }	//end of run	
}

