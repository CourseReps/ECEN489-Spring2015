import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

class ServerPanel extends JPanel {

    private final JTextArea statusBox;
    private final ServerPanel panel;
    private ServerRunnable serverRunnable;
    private Thread clientThread = null;
    private final JLabel[] clientLabel = new JLabel[5];
    private final JLabel[] clientTimeLabel = new JLabel[5];

    // CONSTRUCTOR
    public ServerPanel() {

        panel = this;

        final JLabel[] clientTitle = new JLabel[5];
        final JLabel[] clientTimeTitle = new JLabel[5];

        for (int x = 0; x <= 4; x++) {
            clientTitle[x] = new JLabel("Client " + (x+1) + ":");
            clientLabel[x] = new JLabel("Not Connected");
            clientTimeTitle[x] = new JLabel("Updated:");
            clientTimeLabel[x] = new JLabel("N/A");
        }

        // TOP OF WINDOW (ROW 1) ELEMENTS
        statusBox = new JTextArea();
        statusBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusBox.setEditable(false);
        statusBox.setLineWrap(true);
        statusBox.setBackground(Color.WHITE);

        final JScrollPane statusBoxScroller = new JScrollPane(statusBox);
        statusBoxScroller.setPreferredSize(new Dimension(400, 150));
        statusBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statusBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusBoxScroller.setAutoscrolls(true);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(6, 6, 6, 6);

        for (int x = 0; x <= 4; x++) {

            c.anchor = GridBagConstraints.EAST;
            c.gridy = x;
            c.gridx = 0;
            this.add(clientTitle[x], c);
            c.gridx = 1;
            this.add(clientLabel[x], c);
            c.gridx = 2;
            this.add(clientTimeTitle[x], c);
            c.gridx = 3;
            this.add(clientTimeLabel[x], c);
        }

        c.gridy = 6;
        c.gridx = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 4;
        this.add(statusBoxScroller, c);

        serverRunnable = new ServerRunnable(this);
        clientThread = new Thread(serverRunnable);
        clientThread.start();
    }

    public void newMessage(String message) {

        final String text = message;

        Runnable runnable = new Runnable() {
            public void run() {
                statusBox.append(text + '\n');
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    public void setClientStatus(final int client, final boolean connected) {

        Runnable runnable = new Runnable() {
            public void run() {

                if (connected) {
                    clientLabel[client].setText("Connected");
                    clientLabel[client].setForeground(Color.GREEN);

                } else {
                    clientLabel[client].setText("Disconnected");
                    clientLabel[client].setForeground(Color.BLACK);
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    public void setClientTime(final int client, final String timeStamp) {

        Runnable runnable = new Runnable() {
            public void run() {

                clientTimeLabel[client].setText(timeStamp);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}
