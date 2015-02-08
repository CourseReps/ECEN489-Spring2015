import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ClientPanel extends JPanel {

    private final JTextField localIPField;
    private final JTextField portField;
    private final ClientPanel panel;
    private final JButton connectButton;
    private final JLabel statusLabel;

    private final JButton[] vote = new JButton[4];

    private ClientRunnable clientRunnable = null;
    private Thread clientThread = null;
    private final JSeparator horizontalBar;
    private final JSeparator horizontalBar2;
    private final JLabel portLabel;
    private final JLabel localIPLabel;

    // CONSTRUCTOR
    public ClientPanel() {

        panel = this;

        for (int x = 0; x <= 3; x++) {
            vote[x] = new JButton();
            setVoteAction(x);
        }
        clearButtons();

        localIPLabel = new JLabel("Server IP:");

        localIPField = new JTextField("localhost");
        localIPField.setPreferredSize(new Dimension(80, 20));
        localIPField.setEditable(true);
        localIPField.setBackground(Color.WHITE);
        localIPField.setColumns(12);

        portLabel = new JLabel("Port:");

        portField = new JTextField(String.valueOf(ClientRunnable.DEFAULT_PORT));
        portField.setPreferredSize(new Dimension(80, 20));
        portField.setEditable(true);
        portField.setBackground(Color.WHITE);
        portField.setColumns(5);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ButtonConnect());
        connectButton.setPreferredSize(new Dimension(100, 30));
        connectButton.setMargin(new Insets(0, 0, 0, 0));

        statusLabel = new JLabel("Not connected");

        horizontalBar = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar.setPreferredSize(new Dimension(400, 4));

        horizontalBar2 = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar2.setPreferredSize(new Dimension(400, 4));

        buildPanel();
    }


    // PRIVATE METHODS //

    private void buildPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);

        c.gridy = 2;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        this.add(localIPLabel, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        this.add(localIPField, c);

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

        c.gridx = 6;
        c.anchor = GridBagConstraints.EAST;
        this.add(connectButton, c);

        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 3;
        c.fill = GridBagConstraints.VERTICAL;
        this.add(horizontalBar, c);

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

        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 8;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        this.add(horizontalBar2, c);

        c.insets.set(0, 6, 24, 6);
        c.gridy = 9;
        this.add(statusLabel, c);
    }

    private void buttonsOff() {
        for (int x = 0; x <= 3; x++) {
            vote[x].setEnabled(false);
        }
    }

    public void clearButtons() {
        for (int x = 0; x <= 3; x++) {
            vote[x].setEnabled(false);
            vote[x].setText("choice " + (x + 1));
        }
    }


    // INTERNAL CLASSES (Button ActionListeners) //

    class ButtonConnect implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (connectButton.getText().equals("Connect")) {
                connectButtonManip("Connecting", false);

                if (clientRunnable == null) {
                    clientRunnable = new ClientRunnable(
                            panel, localIPField.getText(), Integer.valueOf(portField.getText()), "noname"
                    );
                }

                if (clientRunnable.isRunning) {
                    clientRunnable.disconnect();
                    clientThread.interrupt();
                }

                clientThread = new Thread(clientRunnable);
                clientThread.start();

            } else if (connectButton.getText().equals("Disconnect")) {

                disconnectClient();
            }
        }
    }

    private void setVoteAction(final int whichButton) {

        final int whichOne = whichButton;

        vote[whichOne].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientRunnable.sendString("/sel" + whichOne);
                buttonsOff();
                newMessage("You chose option " + whichOne);
            }
        });
    }


    // PUBLIC METHODS //

    public void disconnectClient() {
        connectButtonManip("Disconnecting", false);

        if (clientThread == null) {
            clientRunnable = new ClientRunnable(
                    panel, localIPField.getText(), Integer.valueOf(portField.getText()), "noname"
            );
            clientThread = new Thread(clientRunnable);
        }

        if (clientRunnable.isRunning) {
            clientRunnable.disconnect();
            clientThread.interrupt();

        }
    }

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


    synchronized public void newMessage(String message) {
        final String text = message;

        Runnable runnable = new Runnable() {
            public void run() {
                statusLabel.setText(text);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

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