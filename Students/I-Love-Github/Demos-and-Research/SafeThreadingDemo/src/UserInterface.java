import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// THIS CLASS IS JUST THE GUI INTERFACE, YOU DON'T NEED TO LOOK THROUGH THIS CODE UNLESS YOU WANT TO LEARN SWING GUI //
class UserInterface extends JPanel {

    protected final JTextField numThreadsField;
    protected final UserInterface panel;
    protected final JButton startButton;
    protected final JButton stopButton;

    // Reference to the thread manager so we can turn it off
    private ThreadManager threadManager;

    // CONSTRUCTOR
    public UserInterface() {

        // Reference to this panel for the internal classes at the bottom
        panel = this;

        // Shows the number of threads that have been created
        final JLabel numThreadsLabel = new JLabel("Number of Threads Created:");
        numThreadsField = new JTextField("0");
        numThreadsField.setPreferredSize(new Dimension(80, 20));
        numThreadsField.setEditable(false);
        numThreadsField.setBackground(Color.WHITE);
        numThreadsField.setColumns(12);

        // Start button
        startButton = new JButton("Start");
        startButton.addActionListener(new ButtonStart());
        startButton.setPreferredSize(new Dimension(100, 30));
        startButton.setMargin(new Insets(0, 0, 0, 0));

        // Stop Button
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ButtonStop());
        stopButton.setPreferredSize(new Dimension(100, 30));
        stopButton.setMargin(new Insets(0, 0, 0, 0));
        stopButton.setEnabled(false);


        // BUILD THE PANEL
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);

        // Buttons are on top
        c.gridx = 1;
        c.anchor = GridBagConstraints.CENTER;
        this.add(startButton, c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.CENTER;
        this.add(stopButton, c);

        // Thread display is on bottom
        c.gridy = 2;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        this.add(numThreadsLabel, c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        this.add(numThreadsField, c);
    }


    // INTERNAL CLASSES //

    // This is the listener for the GO button.  It just creates a ThreadManager process and runs it
    class ButtonStart implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {

            threadManager = new ThreadManager(panel);
            new Thread(threadManager).start();;
        }
    }

    // This is the listener for the GO button.  It just creates a ThreadManager process and runs it
    class ButtonStop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {

            threadManager.stopRunning();
        }
    }


    // PUBLIC METHODS

    // These methods use the SwingUtilities GUI manager to ensure thread safety

    // flipButtons just turns the appropriate buttons on or off
    public void flipButtons(boolean running) {

        // FINAL variable required because we're working with an inner class (the new runnable)
        final boolean buttonOn = running;

        Runnable runnable = new Runnable() {
            public void run() {
                startButton.setEnabled(!buttonOn);
                stopButton.setEnabled(buttonOn);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    // updateUI just updates the total thread count with the current number of running threads
    public void updateUI(final long numThreads) {
        Runnable runnable = new Runnable() {
            public void run() {
                numThreadsField.setText(String.valueOf(numThreads));
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    public void doneRunning() {
        flipButtons(false);
    }
}