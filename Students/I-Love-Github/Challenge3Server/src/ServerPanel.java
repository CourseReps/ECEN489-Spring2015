import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

class ServerPanel extends JPanel {

    private final JTextArea statusBox;
    private final ServerPanel panel;
    private ServerRunnable serverRunnable;
    private Thread clientThread = null;

    // CONSTRUCTOR
    public ServerPanel() {

        panel = this;

        // TOP OF WINDOW (ROW 1) ELEMENTS
        statusBox = new JTextArea();
        statusBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusBox.setEditable(false);
        statusBox.setLineWrap(true);
        statusBox.setBackground(Color.WHITE);

        final JScrollPane statusBoxScroller = new JScrollPane(statusBox);
        statusBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statusBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statusBoxScroller.setAutoscrolls(true);

        this.setLayout(new BorderLayout());
        this.add(statusBoxScroller, BorderLayout.CENTER);

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
}