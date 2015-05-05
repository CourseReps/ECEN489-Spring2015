import jssc.SerialPortException;
import jssc.SerialPort;
import jssc.SerialPortList;

import java.util.Vector;

public class SerialComm {

    private SerialPort serialPort;
    private boolean connected;

    public SerialComm() {

        serialPort = null;
        connected = false;
    }

    public boolean connect(String connectionName) {
        serialPort = new SerialPort(connectionName);

        try {
            serialPort.openPort(); //Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);

            connected = true;
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        connected = false;
        return false;
    }

    public boolean sendString(String string) {
        if (connected) {
            try {
                serialPort.writeBytes((string).getBytes());
                return true;
            } catch (SerialPortException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return false;
    }
}
