import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

// SERVER PANEL Class
// This class only builds and provides interactivity with the user interface
// Any functions that do not talk to or come from the UI are handled in other classes
class ServerPanel extends JPanel {

    // Reference to the stop and go buttons -- used for one method, could be refactored out in the future
    public static final int STOP_BUTTON = 0;
    public static final int GO_BUTTON = 1;

    // Reference to self for internal classes
    protected final ServerPanel panel;

    // UI components
    protected final JButton goButton;
    protected final JButton stopButton;
    protected final JLabel serverStatusField;
    protected final JSeparator horizontalBar;
    private final JLabel serverStatusLabel;
    private final JTextArea statusBox;
    private final JScrollPane statusBoxScroller;

    // ServerRunnable and its thread
    private Thread serverThread = null;
    private ServerRunnable serverRunnable = null;


    // CONSTRUCTOR
    // Builds the window components and populates the window
    public ServerPanel() {

        panel = this;

        // Server status label and field
        serverStatusLabel = new JLabel("Server Status:");
        serverStatusField = new JLabel("OFFLINE");
        serverStatusField.setForeground(Color.RED);

        // GO and STOP buttons (stop disabled by default...since the server starts turned off)
        goButton = new JButton("Start Server");
        goButton.addActionListener(new ButtonGO());
        goButton.setPreferredSize(new Dimension(105, 24));
        stopButton = new JButton("Stop Server");
        stopButton.addActionListener(new ButtonSTOP());
        stopButton.setPreferredSize(new Dimension(105, 24));
        stopButton.setEnabled(false);

        // Separator Bars
        horizontalBar = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar.setPreferredSize(new Dimension(400, 4));

        // Server status box is actually a large scrollable text area -- used so the server operator can look back on what has happened recently
        statusBox = new JTextArea("Ready to start...\n\n");
        statusBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusBox.setEditable(false);
        statusBox.setBackground(Color.WHITE);
        statusBoxScroller = new JScrollPane(statusBox);
        statusBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statusBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusBoxScroller.setPreferredSize(new Dimension(450, 400));
        statusBoxScroller.setAutoscrolls(true);


        // BUILD THE WINDOW
        // This JPanel will consist of two subpanels
        this.setLayout(new GridBagLayout());
        JPanel subPanelTop = new JPanel(new GridBagLayout());
        JPanel subPanelCenter = new JPanel(new GridBagLayout());
        JPanel subPanelBottom = new JPanel(new GridBagLayout());


        // BUILD TOP SUBPANEL -- ROW 1
        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);
        c.gridy = 1;
        c.weightx = 0.1;

        // Start server button
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 6;
        c.weightx = 0.1;
        subPanelTop.add(goButton, c);


        // BUILD TOP SUBPANEL -- ROW 2
        c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);
        c.gridy = 2;
        c.weightx = 0.1;

        // Server status
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(serverStatusLabel, c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        subPanelTop.add(serverStatusField, c);

        // Stop server button
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 6;
        c.weightx = 0.1;
        c.gridwidth = 1;
        subPanelTop.add(stopButton, c);

        // Top Separator Bar
        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 3;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        subPanelTop.add(horizontalBar, c);

        // BOTTOM SUBPANEL

        // Status box along the bottom of the window
        c = new GridBagConstraints();
        c.insets.set(12, 24, 24, 24);
        c.gridy = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridwidth = 2;
        subPanelBottom.add(statusBoxScroller, c);

        // CONSTRUCT THE MAIN PANEL FROM THE SUBPANELS
        c = new GridBagConstraints();
        c.gridy = 1;
        this.add(subPanelTop, c);
        c.gridy = 2;
        this.add(subPanelCenter, c);
        c.gridy = 3;
        this.add(subPanelBottom, c);
    }


    // INTERNAL CLASSES (Button ActionListeners) //

    // This ActionListener adds the START SERVER functionality.  It launches a ServerRunnable object
    class ButtonGO implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            // Turn off the start server button so we can't start multiple threads and kill our processor
            goButton.setEnabled(false);

            // Check to see if a serverRunnable already exists -- if it doesn't, create one
            if (serverRunnable == null) {
                serverRunnable = new ServerRunnable(panel);
            }

            // Then check to see if serverRunnable is already running.  If it is, we should kill it so we can restart it
            if (serverRunnable.isRunning) {
                serverRunnable.notifyStopPressed(serverThread);
                serverThread.interrupt();
            }

            // Create a new thread and start it!
            serverThread = new Thread(serverRunnable);
            serverThread.start();
        }
    }

    // This ActionListener adds the STOP SERVER functionality.
    class ButtonSTOP implements ActionListener
    {
        public void actionPerformed(ActionEvent ae) {
            emergencyShutdown(); // Despite the dramatic name, this method performs an orderly shutdown
        }
    }


    // PUBLIC METHODS

    // Despite the dramatic name, this method performs an orderly shutdown
    public void emergencyShutdown() {

        // Turn off the Stop Server button (if it's already on)
        Runnable runnable = new Runnable() {
            public void run() {
                stopButton.setEnabled(false);
            }
        };
        SwingUtilities.invokeLater(runnable);

        // If the server doesn't have a thread, create one
        if (serverThread == null) {
            serverRunnable = new ServerRunnable(panel);
            serverThread = new Thread(serverRunnable);
        }

        // If that thread is running, kill it
        if (serverRunnable.isRunning) {
            serverRunnable.notifyStopPressed(serverThread);
            serverThread.interrupt();
        }
    }

    // This method just sets the Server Status label
    synchronized public void setStatus(Color color, String message) {
        serverStatusField.setText(message);
        serverStatusField.setForeground(color);
    }

    // This method just updates the info box at the bottom of the screen
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

    // This method enables or disables the START and STOP SERVER buttons -- it could be refactored into something less complicated
    synchronized public void setButtonEnabled(int button, boolean enabled) {
        final int but = button;
        final boolean en = enabled;

        if (but == STOP_BUTTON) {
            setButton(stopButton, en);
        } else if (but == GO_BUTTON) {
            setButton(goButton, en);
        }
    }

    // This method provides a way to enable/disable the voting buttons
    public void setButton(final JButton button, final boolean on) {

        Runnable runnable = new Runnable() {
            public void run() {
                button.setEnabled(on);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}

//import javax.swing.*;
//import java.awt.*;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//class ServerPanel extends JPanel {
//
//    private final JTextArea statusBox;
//    private final ServerPanel panel;
//    private ServerRunnable serverRunnable;
//    private Thread clientThread = null;
//    private final JLabel[] clientLabel = new JLabel[5];
//    private final JLabel[] clientTimeLabel = new JLabel[5];
//
//    // CONSTRUCTOR
//    public ServerPanel() {
//
//        panel = this;
//
//        final JLabel[] clientTitle = new JLabel[5];
//        final JLabel[] clientTimeTitle = new JLabel[5];
//
//        for (int x = 0; x <= 4; x++) {
//            clientTitle[x] = new JLabel("Client " + (x+1) + ":");
//            clientLabel[x] = new JLabel("Not Connected");
//            clientTimeTitle[x] = new JLabel("Updated:");
//            clientTimeLabel[x] = new JLabel("N/A");
//        }
//
//        // TOP OF WINDOW (ROW 1) ELEMENTS
//        statusBox = new JTextArea();
//        statusBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//        statusBox.setEditable(false);
//        statusBox.setLineWrap(true);
//        statusBox.setBackground(Color.WHITE);
//
//        final JScrollPane statusBoxScroller = new JScrollPane(statusBox);
//        statusBoxScroller.setPreferredSize(new Dimension(400, 150));
//        statusBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        statusBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        statusBoxScroller.setAutoscrolls(true);
//
//        this.setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//
//        c.insets = new Insets(6, 6, 6, 6);
//
//        for (int x = 0; x <= 4; x++) {
//
//            c.anchor = GridBagConstraints.EAST;
//            c.gridy = x;
//            c.gridx = 0;
//            this.add(clientTitle[x], c);
//            c.gridx = 1;
//            this.add(clientLabel[x], c);
//            c.gridx = 2;
//            this.add(clientTimeTitle[x], c);
//            c.gridx = 3;
//            this.add(clientTimeLabel[x], c);
//        }
//
//        c.gridy = 6;
//        c.gridx = 0;
//        c.weightx = 1;
//        c.weighty = 1;
//        c.fill = GridBagConstraints.VERTICAL;
//        c.anchor = GridBagConstraints.CENTER;
//        c.gridwidth = 4;
//        this.add(statusBoxScroller, c);
//
//        serverRunnable = new ServerRunnable(this);
//        clientThread = new Thread(serverRunnable);
//        clientThread.start();
//    }
//
//    public void newMessage(String message) {
//
//        final String text = message;
//
//        Runnable runnable = new Runnable() {
//            public void run() {
//                statusBox.append(text + '\n');
//            }
//        };
//        SwingUtilities.invokeLater(runnable);
//    }
//
//    public void setClientStatus(final int client, final boolean connected) {
//
//        Runnable runnable = new Runnable() {
//            public void run() {
//
//                if (connected) {
//                    clientLabel[client].setText("Connected");
//                    clientLabel[client].setForeground(Color.GREEN);
//
//                } else {
//                    clientLabel[client].setText("Disconnected");
//                    clientLabel[client].setForeground(Color.BLACK);
//                }
//            }
//        };
//        SwingUtilities.invokeLater(runnable);
//    }
//
//    public void setClientTime(final int client, final String timeStamp) {
//
//        Runnable runnable = new Runnable() {
//            public void run() {
//
//                clientTimeLabel[client].setText(timeStamp);
//            }
//        };
//        SwingUtilities.invokeLater(runnable);
//    }
//}
