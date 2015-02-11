/**
 * Created by RhoadsWylde on 2/6/2015.
 */
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/*
 * ButtonDemo.java requires the following files:
 *   images/right.gif
 *   images/middle.gif
 *   images/left.gif
 */
public class Buttons extends JPanel
        implements ActionListener {
    protected JButton b1, b2, b3 , b4;

    public Buttons() {

        b1 = new JButton("Yes");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        b1.setMnemonic(KeyEvent.VK_D);
        b1.setActionCommand("disable No");

        b2 = new JButton("No");
        b2.setVerticalTextPosition(AbstractButton.BOTTOM);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        b2.setMnemonic(KeyEvent.VK_M);
        b2.setActionCommand("disable Yes");

        b3 = new JButton("Send Vote");
        b3.setMnemonic(KeyEvent.VK_E);
        b3.setActionCommand("disable both");
        b3.setEnabled(false);

        b4 = new JButton("Reset");
        b4.setVerticalTextPosition(AbstractButton.CENTER);
        b4.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        b4.setMnemonic(KeyEvent.VK_D);
        b4.setActionCommand("reset");

        //Listen for actions on buttons 1 and 3.
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        b1.setToolTipText("Click this button to vote yes.");
        b2.setToolTipText("This middle button to vote no.");
        b3.setToolTipText("Click this button to send votes.");
        b4.setToolTipText("Click this button to reset.");

        //Add Components to this container, using the default FlowLayout.
        add(b1);
        add(b2);
        add(b3);
        add(b4);
    }

        public void actionPerformed (ActionEvent e){

        if ("disable Yes".equals(e.getActionCommand())) {
            b2.setEnabled(false);
            b1.setEnabled(true);
            b3.setEnabled(true);
        } else if ("disable No".equals(e.getActionCommand())) {
            b2.setEnabled(true);
            b1.setEnabled(false);
            b3.setEnabled(true);
        } else if ("disable both".equals(e.getActionCommand())) {
            b2.setEnabled(false);
            b1.setEnabled(false);
            b3.setEnabled(true);
        } else if ("reset".equals(e.getActionCommand())) {
            b2.setEnabled(true);
            b1.setEnabled(true);
            b3.setEnabled(false);
        }
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("Vote");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Buttons newContentPane = new Buttons();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
