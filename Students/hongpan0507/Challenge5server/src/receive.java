/**
 * Created by hpan on 1/29/15.
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.ObjectInputStream;
import java.io.StringWriter;

public class receive extends Thread{
    private ObjectInputStream input_stream = null;
    private String message = "";
    private String client_ip = "";
    private int client_port = 0;
    private JSONObject j_obj = new JSONObject();
    private JSONArray j_array;
    private Object obj;
    private String[] data = new String[6];

    public receive(ObjectInputStream _input_stream, String _client_ip, int port){
        input_stream = _input_stream;
        client_ip = _client_ip;
        client_port = port;
    }	//end of constructor

    public void run(){
        try{
            database sql_db = new database("opt_out5.db");
            sql_db.open_db();
            String[] column_name = {"LATITUDE", "LONGITUDE", "AZIMUTH", "PITCH", "ROLL", "WIFI_RSSI"};
            String[] column_type = {"DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL"};
            sql_db.create_table("Asus_Transformer", column_name, column_type);

            while(message != ".disconnect"){
                obj = JSONValue.parse(input_stream.readObject().toString());
                j_obj = (JSONObject) obj;

                if(j_obj!=null) {
                    if (j_obj.containsKey("Location")) {
                        j_array = (JSONArray) j_obj.get("Location");
                        if (!j_array.isEmpty()) {
                            data[0] = String.valueOf(j_array.get(0));
                            data[1] = String.valueOf(j_array.get(1));
                        } else {
                            data[0] = null;
                            data[1] = null;
                        }
                    }
                    if (j_obj.containsKey("Orientation")) {
                        j_array = (JSONArray) j_obj.get("Orientation");
                        if (!j_array.isEmpty()) {
                            data[2] = String.valueOf(j_array.get(0));
                            data[3] = String.valueOf(j_array.get(1));
                            data[4] = String.valueOf(j_array.get(2));
                        } else {
                            data[2] = null;
                            data[3] = null;
                            data[4] = null;
                        }
                    }
                    if (j_obj.containsKey("Wifi RSSI")) {
                        data[5] = j_obj.get("Wifi RSSI").toString();
                    }
                }
                sql_db.insert_val("Asus_Transformer",data);
                System.out.print("Client @ " + client_ip + "_" + client_port + ": ");
                System.out.println(j_obj);    //for debugging only
            }	//end of while
            System.out.println("Connection terminated by the client @ " + client_ip + " + " + client_port);
            input_stream.close();
            //sql_db.close_db();
        } catch (Exception e) {
            System.out.println("Error occurred when receiving message" + e);
        }	//end of try block
    }	//end of run	
}

