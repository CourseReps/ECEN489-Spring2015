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

    private final JCheckBox[] optionButton = new JCheckBox[4];
    private final JTextField[] optionText = new JTextField[4];

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


        for (int x = 0; x <= 3; x++) {
            optionButton[x] = new JCheckBox();
            optionText[x] = new JTextField();
            optionText[x].setColumns(40);
            optionText[x].setPreferredSize(new Dimension(120,30));
        }

        optionButton[0].setSelected(true);
        optionButton[1].setSelected(true);


        serverStatusLabel = new JLabel("Server Status:");
        serverStatusField = new JLabel("OFFLINE");
        serverStatusField.setForeground(Color.RED);

        localIPLabel = new JLabel("Local IP:");
        localIPField = new JTextField(s);
        localIPField.setPreferredSize(new Dimension(120, 20));
        localIPField.setEditable(false);
        localIPField.setBackground(Color.WHITE);

        portLabel = new JLabel("Port:");
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

        voteOn = new JButton("Start Vote");
        voteOn.addActionListener(new ButtonStartVote());
        voteOn.setPreferredSize(new Dimension(105, 24));
        voteOn.setEnabled(false);

        voteOff = new JButton("Stop Vote");
        voteOff.addActionListener(new ButtonStopVote());
        voteOff.setPreferredSize(new Dimension(105, 24));
        voteOff.setEnabled(false);

        horizontalBar = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar.setPreferredSize(new Dimension(400, 4));

        horizontalBar2 = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar2.setPreferredSize(new Dimension(400, 4));

        horizontalBar3 = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalBar3.setPreferredSize(new Dimension(400, 4));

        statusBox = new JTextArea("Ready to start...\n\n");
        statusBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusBox.setEditable(false);
        statusBox.setBackground(Color.WHITE);

        statusBoxScroller = new JScrollPane(statusBox);
        statusBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statusBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusBoxScroller.setPreferredSize(new Dimension(450, 80));
        statusBoxScroller.setAutoscrolls(true);


        // BUILD THE WINDOW: This JPanel will consist of two subpanels
        this.setLayout(new GridBagLayout());
        JPanel subPanelTop = new JPanel(new GridBagLayout());
        JPanel subPanelCenter = new JPanel(new GridBagLayout());
        JPanel subPanelBottom = new JPanel(new GridBagLayout());


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

        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 6;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 3;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        subPanelTop.add(horizontalBar2, c);


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

        c.anchor = GridBagConstraints.EAST;
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = .9;
        subPanelCenter.add(optionText[1], c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.weightx = .1;
        subPanelCenter.add(optionButton[1], c);

        c.anchor = GridBagConstraints.EAST;
        c.gridy = 3;
        c.gridx = 1;
        c.weightx = .9;
        subPanelCenter.add(optionText[2], c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.weightx = .1;
        subPanelCenter.add(optionButton[2], c);

        c.anchor = GridBagConstraints.EAST;
        c.gridy = 4;
        c.gridx = 1;
        c.weightx = 1;
        subPanelCenter.add(optionText[3], c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.weightx = .1;
        subPanelCenter.add(optionButton[3], c);

        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 5;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        subPanelCenter.add(horizontalBar, c);


        c = new GridBagConstraints();
        c.insets.set(4, 24, 4, 24);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        subPanelBottom.add(voteOn, c);

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 2;
        subPanelBottom.add(voteOff, c);

        c.insets.set(12, 6, 12, 6);
        c.gridx = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 5;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 0;
        c.gridwidth = 2;
        subPanelBottom.add(horizontalBar3, c);

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
        c.gridx = 1;
        this.add(subPanelTop, c);

        c.gridy = 2;
        c.gridx = 1;
        this.add(subPanelCenter, c);

        c.gridy = 3;
        c.gridx = 1;
        this.add(subPanelBottom, c);
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

            setButton(voteOn, true);
            setButton(voteOff, false);
        }
    }

    class ButtonStartVote implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            setButton(voteOn, false);
            setButton(voteOff, true);

            int numQuestions = 0;
            boolean[] thisSelected = new boolean[4];
            String[] thisString = new String[4];

            for (int x = 0; x <= 3; x++) {

                thisSelected[x] = optionButton[x].isSelected();
                if (thisSelected[x]) {

                    thisString[x] = optionText[x].getText();
                    numQuestions++;
                }
            }

            serverRunnable.setVoting(true);
            serverRunnable.setQuestions(thisSelected, thisString, numQuestions);
            serverRunnable.startVote();
        }
    }

    class ButtonStopVote implements ActionListener
    {
        public void actionPerformed(ActionEvent ae) {
            setButton(voteOn, true);
            setButton(voteOff, false);

            serverRunnable.setVoting(false);
            serverRunnable.stopVote();
        }
    }

    class ButtonSTOP implements ActionListener
    {
        public void actionPerformed(ActionEvent ae) {
            setButton(voteOn, false);
            setButton(voteOff, false);
            emergencyShutdown();
        }
    }

    public void emergencyShutdown() {
        Runnable runnable = new Runnable() {
            public void run() {
                stopButton.setEnabled(false);
            }
        };
        SwingUtilities.invokeLater(runnable);

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

        if (but == STOP_BUTTON) {
            setButton(stopButton, en);
        } else if (but == GO_BUTTON) {
            setButton(goButton, en);
        }
    }

    public Thread getServerThread() {
        synchronized (serverThread) {
            return serverThread;
        }
    }

    public void setButton(final JButton button, final boolean on) {

        Runnable runnable = new Runnable() {
            public void run() {
                button.setEnabled(on);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}