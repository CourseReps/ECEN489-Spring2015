import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ClientPanel extends JPanel {

    protected final JTextField timeField;
    protected final JTextField nanoField;
    protected final JTextField idField;
    protected final ClientPanel panel;

    private ClientRunnable clientRunnable;

    private Thread clientThread = null;

    // CONSTRUCTOR
    public ClientPanel() {

        panel = this;

        // TOP OF WINDOW (ROW 1) ELEMENTS
        final JLabel timeLabel = new JLabel("null");
        timeField = new JTextField("null");
        timeField.setPreferredSize(new Dimension(80, 20));
        timeField.setEditable(true);
        timeField.setBackground(Color.WHITE);
        timeField.setColumns(12);

        final JLabel nanoLabel = new JLabel("Numbers Generated:");
        nanoField = new JTextField();
        nanoField.setPreferredSize(new Dimension(80, 20));
        nanoField.setEditable(true);
        nanoField.setBackground(Color.WHITE);
        nanoField.setColumns(12);

        final JLabel idLabel = new JLabel("Repeats:");
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(80, 20));
        idField.setEditable(true);
        idField.setBackground(Color.WHITE);
        idField.setColumns(12);

        final JButton recalcButton = new JButton("null");
//        recalcButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                calcID();
//            }
//        });

        // BUILD TOP SUBPANEL
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets.set(4, 6, 4, 6);

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        this.add(timeLabel, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        this.add(timeField, c);

        c.gridy = 2;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        this.add(nanoLabel, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        this.add(nanoField, c);

        c.gridy = 3;
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        this.add(idLabel, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.WEST;
        this.add(idField, c);

        c.gridy = 4;
        c.gridx = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        this.add(recalcButton, c);

        clientRunnable = new ClientRunnable(this);
        Thread runThread = new Thread(clientRunnable);
        runThread.start();
    }

    public void updateUI(long total, long repeats) {

        final String repeatString = String.valueOf(repeats);
        final String totalString = String.valueOf(total);

        Runnable runnable = new Runnable() {
            public void run() {
                panel.idField.setText(repeatString);
                panel.nanoField.setText(totalString);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}