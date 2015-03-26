/**
 * Created by RhoadsWylde on 3/26/2015.
 */
/**
 * Created by RhoadsWylde on 3/25/2015.
 */
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.*;
import java.net.Socket;
import java.util.Enumeration;
import org.json.JSONObject;

public class Teensy implements SerialPortEventListener {
    SerialPort serialPort = null;

    private static final String PORT_NAMES[] = {
//            "/dev/tty.usbmodem", // Mac OS X
//        "/dev/usbdev", // Linux
//        "/dev/tty", // Linux
//        "/dev/serial", // Linux
            "COM5", // Windows
    };

   String serverAddress;
   Socket socketNew;      //creates new socket
   ObjectOutput output;  //constructs output stream for the system info


    private String appName;
    private BufferedReader input;

    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port

    public boolean initialize() {

        try {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            // Enumerate system ports and try connecting to Arduino over each
            System.out.println( "Trying:");
            while (portId == null && portEnum.hasMoreElements()) {
                // Iterate through your host computer's serial port IDs
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                System.out.println( "   port" + currPortId.getName() );
                for (String portName : PORT_NAMES) {
                    if ( currPortId.getName().equals(portName)
                            || currPortId.getName().startsWith(portName)) {

                        // Try to connect to the Arduino on this port
                        // Open serial port
                        serialPort = (SerialPort)currPortId.open(appName, TIME_OUT);
                        portId = currPortId;
                        System.out.println( "Connected on port" + currPortId.getName() );
                        break;
                    }
                }
            }

            if (portId == null || serialPort == null) {
                System.out.println("Oops... Could not connect to Arduino");
                return false;
            }

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            // Give the Arduino some time
            try { Thread.sleep(2147483647);}
            catch (InterruptedException ie) {}

             return true;
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        return false;
    }

    //
    // This should be called when you stop using the port
    //
    public synchronized void close() {
        if ( serialPort != null ) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    //
    // Handle serial port event
    //
    public synchronized void serialEvent(SerialPortEvent oEvent) { //this will get the data from the arduino
        double distance = 0;
        double presPadOn = 0;
        String ping1 = "ping";
        String ping2 = "boom";
        JSONObject pingFlag = new JSONObject();
        JSONObject boomFlag = new JSONObject();


        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE:
                    if ( input == null ) {
                        input = new BufferedReader(
                                new InputStreamReader(
                                        serialPort.getInputStream()));
                    }
                    String inputLine = input.readLine();
                    String[] parts = inputLine.split("-");//take the string from the arduino and split it into two
                    String part1 = parts[0]; //part 1 of the split
                    String part2 = parts[1]; //part 2 of the split
                    distance = Double.parseDouble(part1); //turn the first part into a double for inches
                    presPadOn = Double.parseDouble(part2); //turn the second part into a double for presPadOn
                    //System.out.println(distance);
                    //System.out.println(presPadOn);
                    if (distance <= 96 && presPadOn > 1000){ //this loop will check for presPadOn high and if the person is range of the ping sensor.
                        System.out.println(ping1);
                        System.out.println(ping2);
                        pingFlag.put("command", ping1);
                        boomFlag.put("command", ping2);
                        /*System.out.println(pingFlag);
                        System.out.println(boomFlag);*/
                        output.writeObject(pingFlag);
                        output.writeObject(boomFlag);
                    }
                    else if (distance <= 96){
                        System.out.println(ping1);
                        pingFlag.put("command", ping1);
                        /*System.out.println(pingFlag);*/
                        output.writeObject(pingFlag);

                    }
                    else if (presPadOn > 1000){
                        System.out.println(ping2);
                        boomFlag.put("command", ping2);
                        /*System.out.println(boomFlag);*/
                        output.writeObject(boomFlag);
                    }
                    else {}

                    break;

                default:
                    break;
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public Teensy() {
        appName = getClass().getName();
        try {
            socketNew = new Socket(serverAddress, 9000);      //creates new socket
            output = new ObjectOutputStream(socketNew.getOutputStream());  //constructs output stream for the system info
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public static void main(String[] args) throws Exception {

        Teensy test = new Teensy();
        if ( test.initialize() ) {
            try { Thread.sleep(2147483647); } catch (InterruptedException ie) {}
            test.close();
        }

        // Wait 5 seconds then shutdown
        try { Thread.sleep(2147483647); } catch (InterruptedException ie) {}
    }
}


