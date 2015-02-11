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
    protected final JButton voteOn;
    protected final JButton voteOff;
    protected final JLabel serverStatusField;
    protected final JTextField portField;
    private final JLabel serverStatusLabel;
    private final JLabel localIPLabel;
    private final JTextField localIPField;
    private final JLabel portLabel;
    private final JSeparator horizontalBar;
    private final JSeparator horizontalBar2;
    private final JSeparator horizontalBar3;
    private final JTextArea statusBox;
    private final JScrollPane statusBoxScroller;

    // Vote textboxes and "enable" checkboxes
    private final JCheckBox[] optionButton = new JCheckBox[4];
    private final JTextField[] optionText = new JTextField[4];

    // ServerRunnable and its thread
    private Thread serverThread = null;
    private ServerRunnable serverRunnable = null;


    // CONSTRUCTOR
    // Builds the window components and populates the window
    public ServerPanel() {

        panel = this;

        // Gets the local IP address for the purpose of populating the IP text field
        String s = "Not Detected";
        try {
            s = InetAddress.getLocalHost().getHostAddress();
        } catch(UnknownHostException e) {
            // nothing here
        }

        // Initialize the "option enable" textboxes and checkboxes
        for (int x = 0; x <= 3; x++) {
            optionButton[x] = new JCheckBox();
            optionText[x] = new JTextField();
            optionText[x].setColumns(40);
            optionText[x].setPreferredSize(new Dimension(120,30));
        }

        // The first two options are enabled by default (expected to be the most common number of voting options)
        optionButton[0].setSelected(true);
        optionButton[1].setSelected(true);

        // Server status label and field
        serverStatusLabel = new JLabel("Server Status:");
        serverStatusField = new JLabel("OFFLINE");
        serverStatusField.setForeground(Color.RED);

        // Server IP label and field
        localIPLabel = new JLabel("Local IP:");
        localIPField = new JTextField(s);
        localIPField.setPreferredSize(new Dimension(120, 20));
        localIPField.setEditable(false);
        localIPField.setBackground(Color.WHITE);

        // Server port label and field
        portLabel = new JLabel("Port:");
        portField = new JTextField(String.valueOf(ServerRunnable.DEFAULT_PORT));
        portField.setPreferredSize(new Dimension(60, 20));
        portField.setEditable(true);
        portField.setBackground(Color.WHITE);

        // GO and STOP buttons (stop disabled by default...since the server starts turned off)
        goButton = new JButton("Start Server");
        goButton.addActionListener(new ButtonGO());
        goButton.setPreferredSize(new Dimension(105, 24));
        stopButton = new JButton("Stop Server");
        stopButton.addActionListener(new ButtonSTOP());
        stopButton.setPreferredSize(new Dimension(105, 24));
        stopButton.setEnabled(false);

        // START and STOP VOTE buttons (disabled until the server is turned on)
        voteOn = new JButton("Start Vote");
        voteOn.addActionListener(new ButtonStartVote());
        voteOn.setPreferredSize(new Dimension(105, 24));
        voteOn.setEnabled(false);
        voteOff = new JButton("Stop Vote");
        voteOff.addActionListener(new ButtonStopVote());
        voteOff.setPreferredSize(new Dimension(105, 24));
        voteOff.setEnabled(false);

        // Separator Bars
        horizontalBar = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar.setPreferredSize(new Dimension(400, 4));
        horizontalBar2 = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar2.setPreferredSize(new Dimension(400, 4));
        horizontalBar3 = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar3.setPreferredSize(new Dimension(400, 4));

        // Server status box is actually a large scrollable text area -- used so the server operator can look back on what has happened recently
        statusBox = new JTextArea("Ready to start...\n\n");
        statusBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusBox.setEditable(false);
        statusBox.setBackground(Color.WHITE);
        statusBoxScroller = new JScrollPane(statusBox);
        statusBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statusBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusBoxScroller.setPreferredSize(new Dimension(450, 80));
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

        // IP info
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(localIPLabel, c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        subPanelTop.add(localIPField, c);

        // Port info
        c.gridx = 3;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(portLabel, c);
        c.gridx = 4;
        c.anchor = GridBagConstraints.WEST;
        subPanelTop.add(portField, c);

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
        subPanelTop.add(horizontalBar2, c);

        // CENTER SUBPANEL CONTAINS THE VOTING OPTION TEXT FIELDS AND CHECKBOXES

        // Option 1
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 1;
        c.gridx = 1;
        c.weightx = .9;
        subPanelCenter.add(optionText[0], c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.weightx = .1;
        subPanelCenter.add(optionButton[0], c);

        // Option 2
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = .9;
        subPanelCenter.add(optionText[1], c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.weightx = .1;
        subPanelCenter.add(optionButton[1], c);

        // Option 3
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 3;
        c.gridx = 1;
        c.weightx = .9;
        subPanelCenter.add(optionText[2], c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.weightx = .1;
        subPanelCenter.add(optionButton[2], c);

        // You get the idea....option 4
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 4;
        c.gridx = 1;
        c.weightx = 1;
        subPanelCenter.add(optionText[3], c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.weightx = .1;
        subPanelCenter.add(optionButton[3], c);

        // Second separator bar
        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 5;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        subPanelCenter.add(horizontalBar, c);

        // BOTTOM SUBPANEL

        // START VOTE button
        c = new GridBagConstraints();
        c.insets.set(4, 24, 4, 24);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        subPanelBottom.add(voteOn, c);

        // STOP VOTE button
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 2;
        subPanelBottom.add(voteOff, c);

        // Bottom separator bar
        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 5;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        c.gridwidth = 2;
        subPanelBottom.add(horizontalBar3, c);

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
                serverRunnable = new ServerRunnable(panel, Integer.valueOf(portField.getText()));
            }

            // Then check to see if serverRunnable is already running.  If it is, we should kill it so we can restart it
            if (serverRunnable.isRunning) {
                serverRunnable.notifyStopPressed();
                serverThread.interrupt();
            }

            // Create a new thread and start it!
            serverThread = new Thread(serverRunnable);
            serverThread.start();

            // Now that the server is running, enable the Start Vote button
            setButton(voteOn, true);
            setButton(voteOff, false);
        }
    }

    // This ActionListener adds the STOP SERVER functionality.
    class ButtonSTOP implements ActionListener
    {
        public void actionPerformed(ActionEvent ae) {
            // Kill all of the vote buttons and run emergencyShutdown to kill the server
            setButton(voteOn, false);
            setButton(voteOff, false);
            emergencyShutdown(); // Despite the dramatic name, this method performs an orderly shutdown
        }
    }

    // This ActionListener adds the START VOTE functionality.
    class ButtonStartVote implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            // Switch the active buttons -- we shouldn't be able to start multiple votes
            setButton(voteOn, false);
            setButton(voteOff, true);


            int numQuestions = 0;
            boolean[] thisSelected = new boolean[4];
            String[] thisString = new String[4];

            for (int x = 0; x <= 3; x++) {

                thisSelected[x] = optionButton[x].isSelected(); // Check each checkbox to see if it is checked
                if (thisSelected[x]) {

                    thisString[x] = optionText[x].getText(); // If it is checked, we grab the text from the appropriate textbox
                    numQuestions++; // Also, keep track of how many active questions we have
                }
            }

            // Finally, pass the information on to the ServerRunnable and run its startVote method to enable client voting
            serverRunnable.setQuestions(thisSelected, thisString, numQuestions);
            serverRunnable.startVote();
        }
    }

    // This class enables STOP VOTE button functionality
    class ButtonStopVote implements ActionListener
    {
        public void actionPerformed(ActionEvent ae) {
            // Swap buttons so the user can start another vote
            setButton(voteOn, true);
            setButton(voteOff, false);

            // Run the stopVote method to disable voting and notify clients of the result
            serverRunnable.stopVote();
        }
    }

    // PUBLIC METHODS

    // Despite the dramatic name, this method performs an orderly shutdown
    public void emergencyShutdown() {

        // If a vote is in progress, kill it
        if (serverRunnable.isVoting()) {
            serverRunnable.stopVote();
        }

        // Turn off the Stop Server button (if it's already on)
        Runnable runnable = new Runnable() {
            public void run() {
                stopButton.setEnabled(false);
            }
        };
        SwingUtilities.invokeLater(runnable);

        // If the server doesn't have a thread, create one
        if (serverThread == null) {
            serverRunnable = new ServerRunnable(panel, Integer.valueOf(portField.getText()));
            serverThread = new Thread(serverRunnable);
        }

        // If that thread is running, kill it
        if (serverRunnable.isRunning) {
            serverRunnable.notifyStopPressed();
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

    // This returns the serverThread -- used by the ServerPanel class for self-interruption by child threads
    public Thread getServerThread() {
        synchronized (serverThread) {
            return serverThread;
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