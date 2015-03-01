import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
        String[] column_name = {"TIME", "MAC"};
        String[] column_type = {"LONG NULL", "TEXT NULL"};
        String[] data = new String[2];

        Ethernet eth = new Ethernet();
        List<String> MAC_addr = new ArrayList<String>();
        HashSet hs_MAC = new HashSet();
        String temp_src;
        boolean trick = false;

        boolean new_time = true;
        long ms1 = 0;
        long ms2 = 0;
        Instant t1 = Instant.now();
        Instant t2 = Instant.now();
        Instant t3 = Instant.now();

        public void nextPacket(PcapPacket packet, String user) {
            try {
                if (trick == false) {
                    String stmt = "Create Table If Not Exists PBID " +
                            "(PBID LONG)";
                    sql_db.open_db();
                    sql_db.create_table("DATA", column_name, column_type);
                    sql_db.single_stmt(stmt);
                    stmt = "INSERT INTO PBID " +
                            "(PBID) VALUES (3);";
                    sql_db.single_stmt(stmt);
                    trick = true;
                }

                if (packet.hasHeader(eth)) {
                    //temp_dest = FormatUtils.mac(eth.destination());
                    temp_src = FormatUtils.mac(eth.source());
                    MAC_addr.add(temp_src);

                    if (new_time == true) {
                        t1 = Instant.now();
                        new_time = false;
                    }
                    t2 = Instant.now();
                    ms1 = Duration.between(t1, t2).toMillis();
                    ms2 = Duration.between(t2, t3).toMillis();
                    if (ms1 > 999) {    // 999 = 1 second
                        System.out.println(t2);
                        hs_MAC.addAll(MAC_addr);
                        MAC_addr.clear();
                        MAC_addr.addAll(hs_MAC);    //remove duplicates
                        hs_MAC.clear();
                        data[0] = Long.toString(packet.getCaptureHeader().timestampInMillis());
                        for (int i = 0; i < MAC_addr.size(); ++i) {
                            data[1] = MAC_addr.get(i);
                            sql_db.insert_val_string("DATA", data);
                        }
                        System.out.println(MAC_addr);
                        MAC_addr.clear();
                        new_time = true;
                    }
                    /*
                    if (ms2 > 99) {
                        System.out.println(t2);
                        hs_MAC.addAll(MAC_addr);
                        data[1] = Long.toString(packet.getCaptureHeader().timestampInMillis());
                        for (int i = 0; i < MAC_addr.size(); ++i) {
                            data[0] = MAC_addr.get(i);
                            sql_db.insert_val_string("MAC_address", data);
                        }
                        System.out.println(MAC_addr);
                        MAC_addr.clear();
                        new_time = true;
                    }*/
                    t3 = Instant.now();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
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


