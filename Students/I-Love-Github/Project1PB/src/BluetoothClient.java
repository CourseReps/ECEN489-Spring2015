import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.Vector;

/**
 * A simple SPP client that connects with an SPP server
 */
public class BluetoothClient implements DiscoveryListener, Runnable {

    ServerRunnable parent;

    BluetoothClient(ServerRunnable parent) {
        this.parent = parent;
    }

    //object used for waiting
    private static Object lock=new Object();

    //vector containing the devices discovered
    private static Vector vecDevices=new Vector();

    private static String connectionURL=null;

    public void run() {

        try {
            //display local device address and name
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());

            //find devices
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();

            System.out.println("Starting device inquiry...");
            agent.startInquiry(DiscoveryAgent.GIAC, this);

            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("Device Inquiry Completed. ");

            //print all devices in vecDevices
            int deviceCount = vecDevices.size();

            if (deviceCount <= 0) {
                System.out.println("No Devices Found .");
                System.exit(0);
            } else {
                //print bluetooth device addresses and names in the format [ No. address (name) ]
                System.out.println("Bluetooth Devices: ");
                for (int i = 0; i < deviceCount; i++) {
                    RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                    System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(true) + ")");
                }
            }

//            System.out.print("Choose Device index: ");
//            BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
//
//            String chosenIndex = bReader.readLine();
//            int index = Integer.parseInt(chosenIndex.trim());

            for (int index = 0; index < vecDevices.size(); index++) {
                //check for spp service
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(index);
                UUID[] uuidSet = new UUID[1];
                uuidSet[0] = new UUID("446118f08b1e11e29e960800200c9a66", false);

                System.out.println("\nSearching for service...");
                agent.searchServices(null, uuidSet, remoteDevice, this);

                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (connectionURL == null) {
                    System.out.println("Device does not support Simple SPP Service.");
                    System.exit(0);
                }


                System.out.println("Connecting to client...");
                //connect to the server and send a line of text
                StreamConnection streamConnection = (StreamConnection) Connector.open(connectionURL);
                OutputStream tmpOut = streamConnection.openOutputStream();
                InputStream tmpIn = streamConnection.openInputStream();
                BufferedReader readInStream = new BufferedReader(new InputStreamReader(tmpIn));
                BufferedWriter writeOutStream = new BufferedWriter(new OutputStreamWriter(tmpOut));

                String recv = readInStream.readLine();
                long androidID = Long.getLong(recv);
                parent.newMessage("Android device ID is " + recv);

                // Send the android device my ID
                writeOutStream.write(String.valueOf(parent.getDB().getMyID()));
                writeOutStream.flush();

                // CHECK client ID and see what the latest DB line they have
                recv = readInStream.readLine();
                long latestLine = Long.getLong(recv);
                parent.newMessage("Most recent DB line received byclient is: " + recv);

                // Do some processing to build the DB for transmission and calculate the file size
                File db = new File("./prombox.db");
                writeOutStream.write(String.valueOf(db.length()));
                writeOutStream.flush();
                parent.newMessage("File length I will send is " + String.valueOf(db.length()));

                readInStream.close();
                writeOutStream.close();

                /////////////////////////////////////////////////////////////////////////////////
                // BEGIN DOWNLOAD CODE //////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////
                FileInputStream fileIn = new FileInputStream(db);
                int downloadCounter = 0;
                boolean streamsOpen = false;

                long filesize = db.length();
                String filename = "testDB";


                try {

                    byte[] b = new byte[2048];
                    int length;

                    streamsOpen = true;
                    while ((length = fileIn.read()) != -1) {
                        tmpOut.write(b, 0, length);
                        downloadCounter += length;

                        parent.newMessage("Uploading: " + downloadCounter + "/" + filesize);
                    }

                } catch (FileNotFoundException fnfe) {
                    parent.newMessage("Bluetooth: " + fnfe.getMessage());
                } catch (IOException ioe) {
                    parent.newMessage("Bluetooth: " + ioe.getMessage());
                } catch (Exception e) {
                    if (streamsOpen) {
                        parent.newMessage("Bluetooth: " + e.getMessage());
                    }
                    parent.newMessage("The current download has been stopped by another process");
                } finally {
                    if (streamsOpen) {
                        try {
                            fileIn.close();
                        } catch (Exception e) {
                            parent.newMessage("Bluetooth: " + e.getMessage());
                        }
                    }
                }

                /////////////////////////////////////////////////////////////////////////////////
                // END DOWNLOAD CODE ////////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////

                System.out.println("Closing all connections and ending program...");
                tmpIn.close();
                tmpOut.close();
                streamConnection.close();
            }


        } catch (Exception e) {
            parent.newMessage("Bluetooth Comm: " + e.getMessage());
        }
    }

    //methods of DiscoveryListener
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        //add the device to the vector
        if(!vecDevices.contains(btDevice)){
            vecDevices.addElement(btDevice);
        }
    }

    //implement this method since services are not being discovered
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if(servRecord!=null && servRecord.length>0){
            connectionURL=servRecord[0].getConnectionURL(0,false);
        }
        synchronized(lock){
            lock.notify();
        }
    }

    //implement this method since services are not being discovered
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized(lock){
            lock.notify();
        }
    }


    public void inquiryCompleted(int discType) {
        synchronized(lock){
            lock.notify();
        }

    }//end method

}