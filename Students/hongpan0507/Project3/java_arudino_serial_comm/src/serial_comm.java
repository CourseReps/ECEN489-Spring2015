import gnu.io.*;

import java.io.*;
import java.util.Scanner;


/**
 * Created by hpan on 4/30/15.
 */
public class serial_comm {
    public static void main(String[] args) {
        try {
            //-------MUST set system property before using serial port-------
            String serialPortID = "/dev/ttyACM0";
            System.setProperty("gnu.io.rxtx.SerialPorts", serialPortID);
            //---------------------------------------------------------------

            //open comm port
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier("/dev/ttyACM0");
            SerialPort serialPort = (SerialPort) portId.open("Teensy to PC", 5000); //timeout = 5000ms

            //setting parameters
            int DATA_RATE = 9600;
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(
                    SerialPort.FLOWCONTROL_NONE);

            //reading from serial port
            InputStream input = serialPort.getInputStream();
            SerialReader SR = new SerialReader(input);
            (new Thread(SR)).start();

            OutputStream outStream = serialPort.getOutputStream();
            Scanner keyIn = new Scanner(System.in);
            char c = ' ';
            while (c != '4') {
                System.out.print("Enter a number: ");
                c = keyIn.next().charAt(0);
                outStream.write(c);
                Thread.sleep(100);
            }

            System.out.println("Serial Comm Ended");
            serialPort.close();
        } catch (Exception io) {
            System.out.print(io);
        }
    }

    public static class SerialReader implements Runnable {
        InputStream in;
        BufferedReader reader;

        public SerialReader (InputStream in) {
            this.in = in;
            this.reader = new BufferedReader(new InputStreamReader(in));
        }

        public void run () {
            String line = null;
            try{
                while ((line = reader.readLine()) != null) {
                    System.out.println("Read from arduino: " + line);
                }
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}
