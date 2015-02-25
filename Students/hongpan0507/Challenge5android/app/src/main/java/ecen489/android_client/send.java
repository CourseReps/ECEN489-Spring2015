package ecen489.android_client; /**
 * Created by hpan on 1/29/15.
 */

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.io.ObjectOutputStream;
import java.io.IOException;


public class send implements Runnable {
    private ObjectOutputStream output_stream = null;
    public int rssi = 0;
    private client_activity parent;

    // constructor
    public send(ObjectOutputStream _output_stream, client_activity parent_class) {
        output_stream = _output_stream;
        this.parent = parent_class;
        rssi = 0;
    }

    public void run() {
        try {
            output_stream.flush();
            while (!Thread.currentThread().isInterrupted()) {
                if (parent.send_com == true) {
                    output_stream.writeObject(parent.send_complete_data.toString());
                    output_stream.flush();
                    parent.new_data = false;
                    parent.send_com = false;
                    //Thread.sleep(100);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  catch(Exception e){
            e.printStackTrace();
        }
    }	//end of run

    public void test() {

    }
}
