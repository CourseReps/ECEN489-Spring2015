import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

class ServerPanel extends JPanel {

    public static final int STOP_BUTTON = 0;
    public static final int GO_BUTTON = 1;

    protected final ServerPanel panel;
    protected final JButton goButton;
    protected final JButton stopButton;
    protected final JTextArea statusBox;
    protected final JLabel serverStatusField;
    protected final JTextField portField;

    private Thread serverThread = null;
    private ServerRunnable serverRunnable = null;

    public ServerPanel() {

        panel = this;

        String s = "Not Detected";
        try {
            s = InetAddress.getLocalHost().getHostAddress();
        } catch(UnknownHostException e) {
            // nothing here
        }

        final JLabel serverStatusLabel = new JLabel("Server Status:");
        serverStatusField = new JLabel("OFFLINE");
        serverStatusField.setForeground(Color.RED);

        final JLabel localIPLabel = new JLabel("Local IP:");
        final JTextField localIPField = new JTextField(s);
        localIPField.setPreferredSize(new Dimension(120, 20));
        localIPField.setEditable(false);
        localIPField.setBackground(Color.WHITE);

        final JLabel portLabel = new JLabel("Port:");
        portField = new JTextField(String.valueOf(ServerRunnable.DEFAULT_PORT));
        portField.setPreferredSize(new Dimension(60, 20));
        portField.setEditable(true);
        portField.setBackground(Color.WHITE);

        goButton = new JButton("Start Server");
        goButton.addActionListener(new ButtonGO());
        goButton.setPreferredSize(new Dimension(105, 24));

        stopButton = new JButton("Stop Server");
        stopButton.addActionListener(new ButtonSTOP());
        stopButton.setPreferredSize(new Dimension(105, 24));
        stopButton.setEnabled(false);

        statusBox = new JTextArea("Ready to start...\n\n");
        statusBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusBox.setEditable(false);
        statusBox.setBackground(Color.WHITE);

        final JScrollPane statusBoxScroller = new JScrollPane(statusBox);
        statusBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statusBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusBoxScroller.setPreferredSize(new Dimension(450, 300));
        statusBoxScroller.setAutoscrolls(true);


        // BUILD THE WINDOW: This JPanel will consist of two subpanels
        this.setLayout(new BorderLayout());
        JPanel subPanelTop = new JPanel(new GridBagLayout());
        JPanel subPanelBottom = new JPanel(new BorderLayout());


        // BUILD TOP SUBPANEL -- ROW 1
        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);
        c.gridy = 1;
        c.weightx = 0.1;

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(localIPLabel, c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        subPanelTop.add(localIPField, c);

        c.gridx = 3;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(portLabel, c);
        c.gridx = 4;
        c.anchor = GridBagConstraints.WEST;
        subPanelTop.add(portField, c);

        c.gridx = 5;
        c.weightx = .5;
        subPanelTop.add(new JLabel(), c);

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 6;
        c.weightx = 0.1;
        subPanelTop.add(goButton, c);


        // BUILD TOP SUBPANEL -- ROW 2
        c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);
        c.gridy = 2;
        c.weightx = 0.1;

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(serverStatusLabel, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        subPanelTop.add(serverStatusField, c);

        c.gridx = 3;
        c.weightx = 1;
        c.gridwidth = 3;
        subPanelTop.add(new JLabel(), c);

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 6;
        c.weightx = 0.1;
        c.gridwidth = 1;
        subPanelTop.add(stopButton, c);


        // BUILD CENTRAL SUBPANEL
        c.anchor = GridBagConstraints.CENTER;
        c.insets.top = 12;
        subPanelBottom.add(statusBoxScroller, BorderLayout.CENTER);


        // CONSTRUCT THE MAIN PANEL FROM THE SUBPANELS
        this.add(subPanelTop, BorderLayout.PAGE_START);
        this.add(subPanelBottom, BorderLayout.CENTER);
    }

    class ButtonGO implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            goButton.setEnabled(false);

            if (serverRunnable == null) {
                serverRunnable = new ServerRunnable(panel, Integer.valueOf(portField.getText()));
            }

            if (serverRunnable.isRunning) {
                serverRunnable.notifyStopPressed();
                serverThread.interrupt();
            }

            serverThread = new Thread(serverRunnable);
            serverThread.start();
        }
    }

    class ButtonSTOP implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            emergencyShutdown();
        }
    }

    public void emergencyShutdown() {
        stopButton.setEnabled(false);

        if (serverThread == null) {
            serverRunnable = new ServerRunnable(panel, Integer.valueOf(portField.getText()));
            serverThread = new Thread(serverRunnable);
        }

        if (serverRunnable.isRunning) {
            serverRunnable.notifyStopPressed();
            serverThread.interrupt();
        }
    }

    synchronized public void setStatus(Color color, String message) {
        serverStatusField.setText(message);
        serverStatusField.setForeground(color);
    }

    synchronized public void updateStatusBox(String line) {

        final String text = line;

        Runnable runnable = new Runnable() {
            public void run() {
                statusBox.append(text + "\n");
                statusBox.setCaretPosition(statusBox.getDocument().getLength());
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    synchronized public void setButtonEnabled(int button, boolean enabled) {
        final int but = button;
        final boolean en = enabled;

        Runnable runnable = new Runnable() {
            public void run() {
                if (but == STOP_BUTTON) {
                    stopButton.setEnabled(en);
                } else if (but == GO_BUTTON) {
                    goButton.setEnabled(en);
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    public Thread getServerThread() {
        synchronized (serverThread) {
            return serverThread;
        }
    }
}