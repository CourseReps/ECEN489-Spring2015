import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;


/*
work flow:
    1. Setup an error buffer
    2. Get a list of all available interfaces and pick one (see Pcap.findAllDevs()
    3. Open either a live network interface, discovered in previous step, or open an offline, a capture file (see Pcap.openLive() or Pcap.openOffline())
    4. Read either one packet at a time (Pcap.nextEx()) or setup a dispatch loop (see Pcap.loop() or Pcap.dispatch())
    5. If using a dispatch loop, wait in your callback method (see PcapPacketHandler.nextPacket()) and receive incoming packets.
    6. Once you have received a packet, typically the packet is either processed on the spot or put on a queue that is read by another thread
    7. When the dispatch loop exists due to either an interruption (see Pcap.breakLoop())or simply the requested number of packets at the time the loop was setup, then process the queue if it hasn't been handed off or cleanup
    8. Always the last step is to close the pcap handle (see Pcap.close()) to allow Pcap to release all its resources held

 */

public class pack_sniff {
    public void run_app () throws Exception {
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        /***************************************************************************
         * First get a list of devices on this system
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }

        System.out.println("Network devices found:");

        int i = 0;
        for (PcapIf device : alldevs) {
            String description = (device.getDescription() != null) ? device.getDescription() : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
        }

        PcapIf device = new PcapIf();
        for(int j = 0; j < alldevs.size(); ++j) {
            if(alldevs.get(j).getName().toString().equals("wlan1")) {
                device = alldevs.get(j); // We know we have at least 1 device
                System.out.printf("\nChoosing '%s':\n", (device.getDescription() != null) ? device.getDescription() : device.getName());
                System.out.println("MAC Address: " + asString(device.getHardwareAddress()));
            }
        }

        /***************************************************************************
         * Second we open up the selected device
         **************************************************************************/
        final int snaplen = 64 * 1024;           // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10;           // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: " + errbuf.toString());
            return;
        }

        pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "jNetPcap rocks!");

        pcap.close();
    }


    /***************************************************************************
     * Fourth we enter the loop and tell it to capture 10 packets. The loop
     * method does a mapping of pcap.datalink() DLT value to JProtocol ID, which
     * is needed by JScanner. The scanner scans the packet buffer and decodes
     * the headers. The mapping is done automatically, although a variation on
     * the loop method exists that allows the programmer to sepecify exactly
     * which protocol ID to use as the data link type for this pcap interface.
     **************************************************************************/

    public PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
        database sql_db = new database("project1.db");
        String[] column_name = {"MAC", "TIME_STAMP"};
        String[] column_type = {"TEXT NULL", "LONG NULL"};
        String[] data = new String[2];

        Ethernet eth = new Ethernet();
        List<String> MAC_addr = new ArrayList<String>();
        String temp_src;
        String temp_dest;
        boolean sig = false;
        boolean trick = false;

        public void nextPacket(PcapPacket packet, String user) {
            if(trick == false) {
                try {
                    String stmt = "Create Table If Not Exists ROOT " +
                                    "(PBID LONG)";
                    sql_db.open_db();
                    sql_db.create_table("MAC_address", column_name, column_type);
                    sql_db.single_stmt(stmt);
                    stmt = "INSERT INTO ROOT " +
                            "(PBID) VALUES (0851);";
                    sql_db.single_stmt(stmt);
                    //sql_db.create_table("ROOT", root_col, root_type);
                    //sql_db.insert_val_number("ROOT", root_id);
                    trick = true;
                } catch (Exception e) {

                }
            }

            if (packet.hasHeader(eth)) {
                temp_dest = FormatUtils.mac(eth.destination());
                temp_src = FormatUtils.mac(eth.source());
                System.out.printf("Received packet at %s\n",
                        packet.getCaptureHeader().timestampInMillis());

                /*
                if (MAC_addr.isEmpty()) {
                    MAC_addr.add(temp_dest);
                    MAC_addr.add(temp_src);
                    sig = true;
                    System.out.println("Src MAC : " + MAC_addr.get(0));
                    System.out.println("Dest MAC : " + MAC_addr.get(1));
                }

                for (int i = 0; i < MAC_addr.size(); ++i) {
                    if (MAC_addr.get(i).equals(temp_src)) {
                        sig = false;
                        break;
                    } else {
                        sig = true;
                    }
                }
                */
                sig = true;
                if (sig == true) {
                    try {
                        //MAC_addr.add(temp_dest);
                        //MAC_addr.add(temp_src);
                        data[0] = temp_src;
                        data[1] = Long.toString(packet.getCaptureHeader().timestampInMillis());
                        sql_db.insert_val_string("MAC_address", data);
                        System.out.println("Src MAC : " + temp_src);
                        System.out.println("Dest MAC : " + temp_dest);
                        sig = false;
                    } catch (Exception e) {}
                }

            }

                /*
                if(packet.hasHeader(ip4)) {
                    temp_dest = FormatUtils.mac(ip4.destination());
                    temp_src = FormatUtils.mac(ip4.source());

                    if(MAC_addr.isEmpty()){
                        MAC_addr.add(temp_dest);
                        MAC_addr.add(temp_src);
                        sig = true;
                        System.out.println("Src MAC : " + MAC_addr.get(0));
                        System.out.println("Dest MAC : " + MAC_addr.get(1));
                    }
                    for(int i = 0; i < MAC_addr.size(); ++i){
                        if(MAC_addr.get(i).equals(temp_dest) || MAC_addr.get(i).equals(temp_src)){
                            sig = false;
                            break;
                        } else {
                            sig = true;
                        }
                    }
                    if(sig == true) {
                        MAC_addr.add(temp_dest);
                        MAC_addr.add(temp_src);
                        String hexdump = packet.toHexdump(packet.size(), false, true, false);
                        //System.out.println("Src MAC : " + hexdump);
                        System.out.println("Src MAC : " + temp_src);
                        System.out.println("Dest MAC : " + temp_dest);
                        sig = false;
                    }
                }*/
                /*
                if(packet.hasHeader(tcp)) {
                    System.out.printf("Received packet at %s caplen=%-4d len=%-4d test=%-4d %s\n",
                            new Date(packet.getCaptureHeader().timestampInMillis()),
                            packet.getCaptureHeader().caplen(),  // Length actually captured
                            packet.getCaptureHeader().wirelen(), // Original length
                            user                                 // User supplied object
                    );

                }*/
        }
    };



    private String asString(final byte[] mac) {
        final StringBuilder buf = new StringBuilder();
        for (byte b : mac) {
            if (buf.length() != 0) {
                buf.append(':');
            }
            if (b >= 0 && b < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString((b < 0) ? b + 256 : b).toUpperCase());
        }
        return buf.toString();
    }
}


