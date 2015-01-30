import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

class ClientPanel extends JPanel {

    public static final int STOP_BUTTON = 0;
    public static final int GO_BUTTON = 1;

    protected final JTextField localIPField;
    protected final JTextField portField;
    protected final JTextField nameField;
    protected final ClientPanel panel;
    protected final JButton connectButton;
    protected final JButton sendButton;
    protected final JTextArea chatTextBox;
    protected final JTextArea chatEntry;
    protected final JList<Vector> userList;

    protected Vector<String> userListVector;

    private ClientRunnable clientRunnable = null;
    private Thread clientThread = null;

    // CONSTRUCTOR
    public ClientPanel() {

        panel = this;

        // TOP OF WINDOW (ROW 1) ELEMENTS
        final JLabel nameLabel = new JLabel("Username:");
        nameField = new JTextField("John Doe");
        nameField.setPreferredSize(new Dimension(80, 20));
        nameField.setEditable(true);
        nameField.setBackground(Color.WHITE);
        nameField.setColumns(12);

        // TOP OF WINDOW (ROW 1) ELEMENTS
        final JLabel localIPLabel = new JLabel("Server IP:");
        localIPField = new JTextField("localhost");
        localIPField.setPreferredSize(new Dimension(80, 20));
        localIPField.setEditable(true);
        localIPField.setBackground(Color.WHITE);
        localIPField.setColumns(12);

        final JLabel portLabel = new JLabel("Port:");
        portField = new JTextField(String.valueOf(ClientRunnable.DEFAULT_PORT));
        portField.setPreferredSize(new Dimension(80, 20));
        portField.setEditable(true);
        portField.setBackground(Color.WHITE);
        portField.setColumns(5);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ButtonConnect());
        connectButton.setPreferredSize(new Dimension(100, 30));
        connectButton.setMargin(new Insets(0,0,0,0));


        // TOP OF WINDOW (ROW 2) ELEMENTS
        chatTextBox = new JTextArea();
        chatTextBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        chatTextBox.setEditable(false);
        chatTextBox.setLineWrap(true);
        chatTextBox.setBackground(Color.WHITE);

        final JScrollPane chatBoxScroller = new JScrollPane(chatTextBox);
        chatBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatBoxScroller.setAutoscrolls(true);

        userList = new JList<Vector>();
        userListVector = new Vector<String>();
        userList.setListData((Vector) userListVector);
        userList.setFixedCellWidth(100);

        final JScrollPane userListScroller = new JScrollPane(userList);
        userListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        userListScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        userListScroller.setAutoscrolls(false);


        // BOTTOM OF WINDOW ELEMENTS
        chatEntry = new JTextArea();
        chatEntry.setLineWrap(true);
        chatEntry.setEditable(true);
        chatEntry.setBackground(Color.WHITE);
        // Enter key handler for text entry box (can't use simple KeyListener because we have to handle the enter key
        // separately from the message)
        // get our maps for binding from the chatEnterArea JTextArea
        InputMap inputMap = chatEntry.getInputMap(WHEN_FOCUSED);
        ActionMap actionMap = chatEntry.getActionMap();
        // the key stroke we want to capture
        KeyStroke enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        // tell input map that we are handling the enter key
        inputMap.put(enterStroke, enterStroke.toString());
        // tell action map just how we want to handle the enter key
        actionMap.put(enterStroke.toString(), new EnterSent());

        final JScrollPane textEntryScroller = new JScrollPane(chatEntry);
        textEntryScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        textEntryScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textEntryScroller.setAutoscrolls(true);

        // Send button
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(70, 40));
        sendButton.addActionListener(new ButtonSend());

        // BUILD THE WINDOW: This JPanel will consist of three subpanels
        this.setLayout(new BorderLayout());
        JPanel subPanelTop = new JPanel(new GridBagLayout());
        JPanel subPanelCenter = new JPanel(new BorderLayout());
        JPanel subPanelBottom = new JPanel(new BorderLayout());


        // BUILD TOP SUBPANEL
        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(nameLabel, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        subPanelTop.add(nameField, c);

        c.gridy = 2;
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
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        subPanelTop.add(new JLabel(), c);

        c.gridx = 6;
        c.anchor = GridBagConstraints.EAST;
        subPanelTop.add(connectButton, c);


        // BUILD CENTRAL SUBPANEL
        subPanelCenter.add(chatBoxScroller, BorderLayout.CENTER);
        subPanelCenter.add(userListScroller, BorderLayout.EAST);


        // BUILD BOTTOM SUBPANEL
        subPanelBottom.add(textEntryScroller, BorderLayout.CENTER);
        subPanelBottom.add(sendButton, BorderLayout.EAST);


        // CONSTRUCT THE MAIN PANEL FROM THE SUBPANELS
        this.add(subPanelTop, BorderLayout.PAGE_START);
        this.add(subPanelCenter, BorderLayout.CENTER);
        this.add(subPanelBottom, BorderLayout.PAGE_END);
    }


    // INTERNAL CLASSES //
    class ButtonConnect implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (connectButton.getText().equals("Connect")) {
                connectButtonManip("Connecting", false);

                if (clientRunnable == null) {
                    clientRunnable = new ClientRunnable(
                            panel, localIPField.getText(), Integer.valueOf(portField.getText()), nameField.getText()
                    );
                }

                if (clientRunnable.isRunning) {
                    clientRunnable.disconnect();
                    clientThread.interrupt();
                }

                clientRunnable.setUserName(nameField.getText());
                clientThread = new Thread(clientRunnable);
                clientThread.start();

            } else if (connectButton.getText().equals("Disconnect")) {

                connectButtonManip("Disconnecting", false);

                if (clientThread == null) {
                    clientRunnable = new ClientRunnable(
                            panel, localIPField.getText(), Integer.valueOf(portField.getText()), nameField.getText()
                    );
                    clientThread = new Thread(clientRunnable);
                }

                if (clientRunnable.isRunning) {
                    clientRunnable.disconnect();
                    clientThread.interrupt();

                }
            }
        }
    }

    class ButtonSend implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            String stringOut = chatEntry.getText();
            chatEntry.setText("");
            chatEntry.setCaretPosition(0);
            chatEntry.moveCaretPosition(0);

            if (clientRunnable != null && clientRunnable.isConnected) {
                clientRunnable.sendString(stringOut);
            }
        }
    }

    class EnterSent extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
        }
    }

    synchronized public void changeNameBox(String name) {
        nameField.setText(name);
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

    synchronized public void newMessage(String message) {
        final String text = message;

        Runnable runnable = new Runnable() {
            public void run() {
                chatTextBox.append(text + "\n");
                chatTextBox.setCaretPosition(chatTextBox.getDocument().getLength());
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    synchronized public void clearUserList() {
        userListVector.clear();

        Runnable runnable = new Runnable() {
            public void run() {
                userList.updateUI();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    synchronized public void userManip(String userName, boolean add) {
        if (add) {
            userListVector.add(userName);
        } else {
            userListVector.remove(userListVector.indexOf(userName));
        }

        Runnable runnable = new Runnable() {
            public void run() {
                userList.updateUI();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    public Thread getClientThread() {
        synchronized (clientThread) {
            return clientThread;
        }
    }
}