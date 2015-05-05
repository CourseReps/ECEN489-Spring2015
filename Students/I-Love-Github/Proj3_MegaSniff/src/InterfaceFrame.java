import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class InterfaceFrame extends JFrame {

    final private MainRunnable parent;

    private final JPanel megaSniffPanel;
    private final JLabel leftAntenna;
    private final JLabel rightAntenna;
    private final JTable macTable;

    private final JLabel interfaceClientLabel;
    private final JLabel interfaceClientField;
    private final JLabel timestampLabel;
    private final JLabel timestampField;
    private final JLabel realTimeLabel;
    private final JLabel realTimeField;
    private final JLabel packetsReceivedLabel;
    private final JLabel packetsReceivedField;

    private boolean leftAntennaVertical;
    private boolean rightAntennaVertical;

    private static String[] columnNames = {
       /*"timeMS",*/ "MAC", "RSSI"
    };

    private String[][] tableData = {
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },{ " ", " ", " " },
            { " ", " ", " " }
    };

    InterfaceFrame(String windowName, final MainRunnable parent) {

        super(windowName);
        this.parent = parent;

        megaSniffPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        leftAntenna = new JLabel(new ImageIcon(getClass().getResource("vertical.png")));
        rightAntenna = new JLabel(new ImageIcon(getClass().getResource("vertical.png")));

        macTable = new JTable(tableData, columnNames);
        JScrollPane scrollPane = new JScrollPane(macTable);
        scrollPane.setPreferredSize(new Dimension(200,270));
        macTable.setFillsViewportHeight(true);

        interfaceClientLabel = new JLabel("Remote Interface");
        interfaceClientField = new JLabel("Disconnected");
        interfaceClientField.setForeground(Color.RED);
        timestampLabel = new JLabel("Latest timestamp");
        timestampField = new JLabel("null");
        realTimeLabel = new JLabel("...in \"real\" time");
        realTimeField = new JLabel("null");
        packetsReceivedLabel = new JLabel("Packets Received");
        packetsReceivedField = new JLabel("null");

        leftAntennaVertical = true;
        rightAntennaVertical = true;

        c.insets.set(4, 6, 4, 6);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        megaSniffPanel.add(leftAntenna, c);
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        megaSniffPanel.add(rightAntenna, c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridheight = 50;
        c.anchor = GridBagConstraints.CENTER;
        megaSniffPanel.add(scrollPane, c);


        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.WEST;
        megaSniffPanel.add(interfaceClientLabel, c);
        c.gridy = 3;
        megaSniffPanel.add(interfaceClientField, c);


        c.insets.set(16, 6, 4, 6);
        c.gridy = 4;
        megaSniffPanel.add(timestampLabel, c);
        c.insets.set(4, 6, 4, 6);
        c.gridy = 5;
        megaSniffPanel.add(timestampField, c);
        c.gridy = 6;
        c.insets.set(16, 6, 4, 6);
        megaSniffPanel.add(realTimeLabel, c);
        c.gridy = 7;
        c.insets.set(4, 6, 4, 6);
        megaSniffPanel.add(realTimeField, c);
        c.gridy = 8;
        c.insets.set(16, 6, 4, 6);
        megaSniffPanel.add(packetsReceivedLabel, c);
        c.gridy = 9;
        c.insets.set(4, 6, 4, 6);
        megaSniffPanel.add(packetsReceivedField, c);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(megaSniffPanel);
        setSize(650, 600);
        setLocation(100, 100);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void WindowClosing(WindowEvent e) {
                parent.getSocket().disableSocket();
                dispose();
            }
        });
    }

    public void setAntennas(boolean leftVertical, boolean rightVertical) {

        if (!(leftVertical == this.leftAntennaVertical)) {

            if (leftVertical) {
                leftAntenna.setIcon(new ImageIcon(getClass().getResource("vertical.png")));
            } else {
                leftAntenna.setIcon(new ImageIcon(getClass().getResource("horizontal.png")));
            }
        }

        if (!(rightVertical == this.rightAntennaVertical)) {

            if (rightVertical) {
                rightAntenna.setIcon(new ImageIcon(getClass().getResource("vertical.png")));
            } else {
                rightAntenna.setIcon(new ImageIcon(getClass().getResource("horizontal.png")));
            }
        }

        this.leftAntennaVertical = leftVertical;
        this.rightAntennaVertical = rightVertical;
    }

    public void updateTable(LinkedHashMap<String, PacketInfo> entrySet) {

        PacketInfo info;
        int i = 0;

        String[] keySet = entrySet.keySet().toArray(new String[entrySet.size()]);
        Arrays.sort(keySet);

        for (String key : keySet)
        {
            info = entrySet.get(key);
            macTable.getModel().setValueAt(info.MAC, i, 0);
            macTable.getModel().setValueAt(String.valueOf(info.signalStrength), i, 1);

            i++;
            if (i == 50) {
                break;
            }
        }

        for (int j = i + 1; j <= 50; j++) {
            macTable.getModel().setValueAt(" ", j, 0);
            macTable.getModel().setValueAt(" ", j, 1);
        }
    }


    public void updateReceived (final long packetsReceived) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                packetsReceivedField.setText(String.valueOf(packetsReceived));
            }
        });
    }


    public void updateClientStatus (boolean connected) {

        final Color color;
        final String text;

        if (connected) {
            color = Color.GREEN;
            text = "CONNECTED";

        } else {
            color = Color.RED;
            text = "disconnected";
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                interfaceClientField.setText(text);
                interfaceClientField.setForeground(color);
            }
        });
    }

    public void updateTime (final PacketInfo packetInfo) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(packetInfo.timeMillis);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hrs = cal.get(Calendar.HOUR_OF_DAY);
        int mins = cal.get(Calendar.MINUTE);
        int secs = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);

        final String calString = month + "/" + day + "/" + year + " " + hrs + ":" + mins + ":" + secs + "." + ms;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                timestampField.setText(String.valueOf(packetInfo.timeMillis));
                realTimeField.setText(calString);
            }
        });
    }
}
