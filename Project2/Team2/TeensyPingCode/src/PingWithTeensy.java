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
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import org.json.*;

public class PingWithTeensy implements SerialPortEventListener {
    SerialPort serialPort = null;

    private static final String PORT_NAMES[] = {
//         "/dev/tty.usbmodem", // Mac OS X
//        "/dev/usbdev", // Linux
//        "/dev/tty", // Linux
//        "/dev/serial", // Linux
            "COM4", // Windows
    };

    String serverIP= "10.202.97.218";   //Server's IP Address
    int port =2222;

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

                        // Try to connect to the Teensy on this port
                        // Open serial port
                        serialPort = (SerialPort)currPortId.open(appName, TIME_OUT);
                        portId = currPortId;
                        System.out.println( "Connected on port" + currPortId.getName() );
                        break;
                    }
                }
            }

            if (portId == null || serialPort == null) {
                System.out.println("Failed to Connect to Teensy");
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
    public synchronized void serialEvent(SerialPortEvent oEvent) { //this will get the data from the Teensy
        double distance = 0;                                        //initialize the distance
        String ping = "Ping!";
        JSONObject pingFlag = new JSONObject();

        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE:
                    if ( input == null ) {
                        input = new BufferedReader(
                                new InputStreamReader(
                                        serialPort.getInputStream()));
                    }
                    String inputLine = input.readLine();
                    String[] parts = inputLine.split("-");
                    String part1 = parts[0];
                    distance = Double.parseDouble(part1); //turn the first part into a double for inches
                    System.out.println("The distance is:\t" + distance + " inches.");

                    if (distance <= 96){    //this checks if the person is within the ping's range
                        System.out.println(ping);
                        pingFlag.put("command", ping);


                        try (
                                Socket socket = new Socket(serverIP, port);
                                OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
                        )
                        {
                            out.write(pingFlag.toString());
                        }
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

    public PingWithTeensy() {
        appName = getClass().getName();
        try {
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public static void main(String[] args) throws Exception {

        PingWithTeensy test = new PingWithTeensy();
        if ( test.initialize() ) {
            try { Thread.sleep(2147483647); } catch (InterruptedException ie) {}
            test.close();
        }

        // Wait then shutdown
        try { Thread.sleep(2147483647); } catch (InterruptedException ie) {}
    }
}


