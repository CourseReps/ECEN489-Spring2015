/**
 * Created by hpan on 1/29/15.
 */

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class send extends Thread {
    private ObjectOutputStream output_stream = null;
    private String message = "";

    // constructor
    public send(ObjectOutputStream _output_stream) {
        output_stream = _output_stream;
    }

    public void run() {
        try {
            while (message != ".disconnect") {
                mouse_pos position = new mouse_pos();
                position.get_loc();
                output_stream.writeObject(position);
                //output_stream.flush();
                System.out.print("X location = " + position.x_loc);
                System.out.println("\tY location = " + position.y_loc);
                Thread.sleep(100);
            } // end of while
            System.out.println("The client has been disconnected");
            output_stream.close();
        } catch (IOException e) {
            System.out.println("send message error" + e);
        } catch (InterruptedException IE) {
            System.out.println("sleep error" + IE);
        }
    } // end of run
}
