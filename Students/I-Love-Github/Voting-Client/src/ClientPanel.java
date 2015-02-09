import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// CLIENTPANEL Class
// This class only builds and provides interactivity with the user interface
// Any functions that do not talk to or come from the UI are handled in other classes
class ClientPanel extends JPanel {

    // Reference to self for internal classes
    private final ClientPanel panel;

    // UI components
    private final JTextField localIPField;
    private final JTextField portField;
    private final JButton connectButton;
    private final JLabel statusLabel;
    private final JSeparator horizontalBar;
    private final JSeparator horizontalBar2;
    private final JLabel portLabel;
    private final JLabel localIPLabel;
    private final JButton[] vote = new JButton[4];

    // ClientRunnable and its thread
    private ClientRunnable clientRunnable = null;
    private Thread clientThread = null;

    // CONSTRUCTOR
    // Builds the window components and populates the window
    public ClientPanel() {

        panel = this;

        // Voting buttons
        for (int x = 0; x <= 3; x++) {
            vote[x] = new JButton();
            setVoteAction(x);
        }
        clearButtons();

        // Server IP label and field
        localIPLabel = new JLabel("Server IP:");
        localIPField = new JTextField("localhost");
        localIPField.setPreferredSize(new Dimension(80, 20));
        localIPField.setEditable(true);
        localIPField.setBackground(Color.WHITE);
        localIPField.setColumns(12);

        // Server port label and field
        portLabel = new JLabel("Port:");
        portField = new JTextField(String.valueOf(ClientRunnable.DEFAULT_PORT));
        portField.setPreferredSize(new Dimension(80, 20));
        portField.setEditable(true);
        portField.setBackground(Color.WHITE);
        portField.setColumns(5);

        // Connect button
        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ButtonConnect());
        connectButton.setPreferredSize(new Dimension(100, 30));
        connectButton.setMargin(new Insets(0, 0, 0, 0));

        // Separator bars
        horizontalBar = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar.setPreferredSize(new Dimension(400, 4));
        horizontalBar2 = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar2.setPreferredSize(new Dimension(400, 4));

        // Status label for client
        statusLabel = new JLabel("Not connected");

        // Construct the JPanel
        buildPanel();
    }


    // PRIVATE METHODS //

    private void buildPanel() {

        // We're going to use a GridBagLayout
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);

        // IP Label and field
        c.gridy = 2;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        this.add(localIPLabel, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        this.add(localIPField, c);

        // Port label and field
        c.gridx = 3;
        c.anchor = GridBagConstraints.EAST;
        this.add(portLabel, c);

        c.gridx = 4;
        c.anchor = GridBagConstraints.WEST;
        this.add(portField, c);

        c.gridx = 5;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        this.add(new JLabel(), c);

        // Connect button
        c.gridx = 6;
        c.anchor = GridBagConstraints.EAST;
        this.add(connectButton, c);

        // Top separator bar
        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 3;
        c.fill = GridBagConstraints.VERTICAL;
        this.add(horizontalBar, c);

        // Voting buttons
        c.insets.set(12, 6, 12, 6);
        c.gridy = 4;
        c.weighty = 1;
        this.add(vote[0], c);
        c.gridy = 5;
        this.add(vote[1], c);
        c.gridy = 6;
        this.add(vote[2], c);
        c.gridy = 7;
        this.add(vote[3], c);

        // Bottom separator bar
        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 8;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        this.add(horizontalBar2, c);

        // Finally, the status label
        c.insets.set(0, 6, 24, 6);
        c.gridy = 9;
        this.add(statusLabel, c);
    }

    // INTERNAL METHODS //

    // This method turns off the voting buttons (called when an option is selected)
    private void buttonsOff() {
        for (int x = 0; x <= 3; x++) {
            vote[x].setEnabled(false);
        }
    }

    // This method turns off the voting buttons AND clears the button text
    public void clearButtons() {
        for (int x = 0; x <= 3; x++) {
            vote[x].setEnabled(false);
            vote[x].setText("choice " + (x + 1));
        }
    }


    // INTERNAL CLASSES (Button ActionListeners) //

    // This ActionListener adds the connectButton functionality.  It either connects to or disconnects from the server
    class ButtonConnect implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae) {

            // CONNECT TO SERVER
            if (connectButton.getText().equals("Connect")) {
                connectButtonManip("Connecting", false);

                // See if ClientRunnable already exists, create a new copy if it doesn't
                if (clientRunnable == null) {
                    clientRunnable = new ClientRunnable(
                            panel, localIPField.getText(), Integer.valueOf(portField.getText()), "noname"
                    );
                }

                // If clientRunnable is already running, shut it down (handy for killing stuck processes)
                if (clientRunnable.isRunning) {
                    clientRunnable.disconnect();
                    clientThread.interrupt();
                }

                // Now create a new thread and run it
                clientThread = new Thread(clientRunnable);
                clientThread.start();

            // DISCONNECT FROM SERVER
            } else if (connectButton.getText().equals("Disconnect")) {

                // The disconnection code is also accessed by clientRunnable and RootClass, so it has been refactored
                //      into a public function
                disconnectClient();
            }
        }
    }

    // This is an INTERNAL CLASS hidden inside of a private function
    // It creates an ActionListener for the voting buttons which sends a response to the server
    private void setVoteAction(final int whichButton) {

        vote[whichButton].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                clientRunnable.sendString("/sel" + whichButton);    // Send message to server
                buttonsOff();                                       // Disable buttons
                newMessage("You chose option " + whichButton);      // Status message in client window
            }
        });
    }


    // PUBLIC METHODS //

    // Shuts down the server connection
    public void disconnectClient() {
        connectButtonManip("Disconnecting", false);

        // Make sure the thread exists.  If it doesn't, we should create a new clientRunnable
        if (clientThread == null) {
            clientRunnable = new ClientRunnable(
                    panel, localIPField.getText(), Integer.valueOf(portField.getText()), "noname"
            );
            clientThread = new Thread(clientRunnable);
        }

        // Now, we check to see if clientRunnable is running.  If it is, we tell it to disconnect
        if (clientRunnable.isRunning) {
            clientRunnable.disconnect();
            clientThread.interrupt();

        }
    }

    // This function just turns the connect button on/off and changes the text
    // Used ONLY for the connect/disconnect button
    synchronized public void connectButtonManip(String message, boolean enabled) {
        final String text = message;
        final boolean en = enabled;

        Runnable runnable = new Runnable() {
            public void run() {
                connectButton.setEnabled(en);
                connectButton.setText(text);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    // This method builds a special string just to tell the user how many votes remain, then stick it on statusLabel
    synchronized public void remaining(final int remaining) {

        if (!(statusLabel.getText().startsWith("Voting has started!"))) {
            Runnable runnable = new Runnable() {
                public void run() {
                    statusLabel.setText(statusLabel.getText().substring(0, 18) + ".  Waiting on " +
                            String.valueOf(remaining) + " more votes.");
                }
            };
            SwingUtilities.invokeLater(runnable);
        }
    }

    // This method only changes the string displayed in the status label -- used to provide user feedback
    synchronized public void newMessage(String message) {
        final String text = message;

        Runnable runnable = new Runnable() {
            public void run() {
                statusLabel.setText(text);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    // This method only sets the text of the valid voting choice buttons, then enables them
    synchronized public void setButtonText(final String[] strings, final int numChoices) {

        Runnable runnable = new Runnable() {
            public void run() {
                for (int x = 0; x <= numChoices-1; x++) {
                    vote[x].setEnabled(true);
                    vote[x].setText(strings[x]);
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}