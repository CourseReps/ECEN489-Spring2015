import java.io.*;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.net.Socket;
import java.util.Enumeration;
import java.util.Scanner;
import org.json.JSONObject;


public class TeensyInterface implements SerialPortEventListener {
    SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
//            "/dev/tty.usbserial-A9007UX1", // Mac OS X
//            "/dev/ttyACM0", // Raspberry Pi
//            "/dev/ttyUSB0", // Linux
            "COM4" // Windows
    };
    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader input;
    /** The output stream to the port */
    private static PrintWriter pwJSON;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;

    static String serverAddress = "10.202.104.106";
    static Socket socket;

    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            pwJSON.println("quit");

            try {
                input.close();
                pwJSON.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        JSONObject pingJSON = new JSONObject();

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                Double inches = Double.parseDouble(inputLine);

                if (inches <= 86 && inches >= 60) {
                    System.out.println(inches);
                    pingJSON.put("command", "PersonFound");
                    pwJSON.println(pingJSON);
                    //pwJSON.println("PersonFound");
                    System.out.println(inches);
                }


            } catch (Exception e) {
                //System.err.println(e.toString());
                System.out.println("Error sending to server...");
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public static void main(String[] args) throws Exception {
        Scanner userIn = new Scanner(System.in);
        byte data = -1;

        try {
            socket = new Socket(serverAddress, 9090);
            pwJSON = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to server");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TeensyInterface main = new TeensyInterface();
        main.initialize();

        System.out.println("Started - Enter 0 to close:");

        while(data != 0) {
            data = userIn.nextByte();

            //try {Thread.sleep(500);} catch (InterruptedException ie) {}
        }

        System.out.println("Finished");
        main.close();
        System.out.println("Port Closed");
    }
}