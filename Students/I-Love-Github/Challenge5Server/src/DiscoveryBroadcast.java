import java.net.*;
import java.util.Enumeration;

public class DiscoveryBroadcast implements Runnable {

    CommHandler parent;
    String myIP;
    boolean running;

    DiscoveryBroadcast(CommHandler parent, String myIP) {
        this.parent = parent;
        this.myIP = myIP;
        this.running = true;
    }

    @Override
    public void run() {

        while (running) {

            try {

                DatagramSocket c = new DatagramSocket();
                c.setBroadcast(true);

                byte[] sendData = ("CHALLENGE5_DISCOVERY_" + myIP).getBytes();

                try {

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            InetAddress.getByName("255.255.255.255"), 15565);
                    c.send(sendPacket);

                    parent.newMessage("UDP discovery broadcast");
                } catch (Exception e) {

                    parent.newMessage(e.getMessage());
                }

                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {

                    NetworkInterface networkInterface = interfaces.nextElement();

                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {

                        continue;
                    }

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {

                        InetAddress broadcast = interfaceAddress.getBroadcast();

                        if (broadcast == null) {
                            continue;
                        }

                        try {

                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 15565);
                            c.send(sendPacket);

                        } catch (Exception e) {

                            parent.newMessage(e.getMessage());
                        }
                    }

                    byte[] recvBuf = new byte[15000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                    c.receive(receivePacket);
                }

                Thread.sleep(3000);

            } catch (Exception e) {

                parent.newMessage(e.getMessage());
            }
        }
    }



    public void stopDiscovery() {
        this.running = false;
    }
}