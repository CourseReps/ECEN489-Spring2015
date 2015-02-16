/**
 * Created by gmnealusn on 2/14/2015.
 */

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;
import java.io.*;
import java.util.ArrayList;


public class Bluetooth implements DiscoveryListener{    //Creates the Bluetooth Class that implements DiscoveryListener
    private static final Object lock=new Object();

    public ArrayList<RemoteDevice> devices;

    public Bluetooth() {
        devices = new ArrayList<RemoteDevice>();
    } //Creates an array of the available Bluetooth Devices

    public static void main(String[] args) {

        Bluetooth listener =  new Bluetooth();  //Creates a new Bluetooth Listener

        try{
            LocalDevice localDevice = LocalDevice.getLocalDevice(); //Gets the local Bluetooth Device
            DiscoveryAgent agent = localDevice.getDiscoveryAgent(); //Starts the DiscoveryAgent on the local device
            agent.startInquiry(DiscoveryAgent.GIAC, listener); //Starts an inquiry to find other devices in range
            try {
                synchronized(lock){
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("Device Inquiry Completed. ");
            UUID[] uuidSet = new UUID[1];   //The following sets the Unique Universal Identifier
            uuidSet[0]=new UUID(0x1105);  //UUID of the OBEX Object Push service

            int[] attrIDs =  new int[] {
                    0x0100 // Service name
            };
            for (RemoteDevice device : listener.devices) {  //This gets the information on the Remote Devices
                agent.searchServices(attrIDs,uuidSet,device,listener);
                try {
                    synchronized(lock){
                        lock.wait();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                System.out.println("Service search finished.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) { //Gets the name or address of the remote devices
        String name;
        try {
            name = btDevice.getFriendlyName(false);
        } catch (Exception e) {
            name = btDevice.getBluetoothAddress();
        }
        devices.add(btDevice);
        System.out.println("device found: " + name);
    }
    @Override
    public void inquiryCompleted(int arg0) { //Completes the inquiry began earlier
        synchronized(lock){
            lock.notify();
        }
    }
    @Override
    public void serviceSearchCompleted(int arg0, int arg1) {
        synchronized (lock) {
            lock.notify();
        }
    }
    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (int i = 0; i < servRecord.length; i++) {
            String bt_Address = servRecord[i].getConnectionURL(ServiceRecord.AUTHENTICATE_ENCRYPT, true);  //Here the addresses for the devices are found
            if (bt_Address == null) {
                continue;
            }
            DataElement serviceName = servRecord[i].getAttributeValue(0x0100);  //The service names are found
            if (serviceName != null) {
                System.out.println("service " + serviceName.getValue() + " found " + bt_Address);

                if(serviceName.getValue().toString().startsWith("OBEX Object Push")){   //Check to see if the service is OBEX
                    sendMessageToDevice(bt_Address);
                }
            } else {
                System.out.println("service found " + bt_Address);  //Prints the names of the discovered devices
            }
        }
    }

    private static void sendMessageToDevice(String bt_Address){
        try{
            System.out.println("Connecting to " + bt_Address);

            ClientSession clientSession = (ClientSession) Connector.open(bt_Address); //Here the connection is opened using the address of the remote device
            HeaderSet hsConnectReply = clientSession.connect(null); //Connection is complete
            if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) { //Check to see if Server response is wrong
                System.out.println("Failed to connect");
                return;
            }
            //The following lines create the HeaderSet and sets the headers
            HeaderSet hsOperation = clientSession.createHeaderSet();
            hsOperation.setHeader(HeaderSet.NAME, "ServerProperties.txt"); //Sets the name of the object to "ServerProperties.txt
            hsOperation.setHeader(HeaderSet.TYPE, "text");     //Sets the type of info in the header

            //Create PUT Operation and output stream
            Operation putOperation = clientSession.put(hsOperation);
            OutputStream os;

            //Get some useful data
            String userName = ("Server User Name: " + System.getProperty("user.name"));
            String osArch = ("\nServer's System Architecture: " + System.getProperty("os.arch"));
            String osName = ("\nServer's Operating System: " + System.getProperty("os.name"));
            String osVer = ("\nServer's Operating System Version: " + System.getProperty("os.version"));
            String userDir = ("\nServer's User's Home Directory: " + System.getProperty("user.home"));
            String jDir = ("\nServer's Java Directory: " + System.getProperty("java.home"));
            String jVer = ("\nServer's Java Version: " + System.getProperty("java.version"));
            String jVend = ("\nJava Vendor Name: " + System.getProperty("java.vendor"));

            // Send some data to server
            os = putOperation.openOutputStream();   //opens the output stream for the put operation

            //The following strings are encoded into an array of Bytes using the standard iso-8859-1 encoding
            byte data[] = userName.getBytes("iso-8859-1");
            os.write(data);
            data = osArch.getBytes("iso-8859-1");
            os.write(data);
            data = osName.getBytes("iso-8859-1");
            os.write(data);
            data = osVer.getBytes("iso-8859-1");
            os.write(data);
            data = userDir.getBytes("iso-8859-1");
            os.write(data);
            data = jDir.getBytes("iso-8859-1");
            os.write(data);
            data = jVer.getBytes("iso-8859-1");
            os.write(data);
            data = jVend.getBytes("iso-8859-1");
            os.write(data);

            //Finally close the outputStream and the putOperation, disconnect and close the clientSession.
            os.close();
            putOperation.close();
            clientSession.disconnect(null);
            clientSession.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
