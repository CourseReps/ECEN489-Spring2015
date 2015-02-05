package com.chatclient;

import com.anudeep.ClientInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class SimpleClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("SimpleClient");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);


    public SimpleClient() {
/*
Create GUI for Client chat window
 */
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
/*
ActionListener for chat window dataField
 */
        dataField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                out.println(dataField.getText());
                String response;
                try {
                    response = in.readLine();
                    if (response == null || response.equals("")) {
                        System.exit(0);
                    }
                } catch (IOException ex) {
                    response = "Error: " + ex;
                }
                messageArea.append(response + "\n");
                dataField.selectAll();
            }
        });
    }

    /**
     * Implements the connection logic by prompting the end user for
     * the server's IP address, connecting, setting up streams, and
     * consuming the welcome messages from the server.  The Capitalizer
     * protocol says that the server sends three lines of text to the
     * client immediately after establishing a connection.
     */
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to the Simple Chat Program",
                JOptionPane.QUESTION_MESSAGE);

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9000);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream  objectInput = new ObjectInputStream(socket.getInputStream());

        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
            messageArea.append(in.readLine() + "\n");
        }

        ClientInfo ci = new ClientInfo();
        ci.setClientNumber(0);
        ci.setCountry(System.getProperty("user.country"));
        ci.setOsArch(System.getProperty("os.arch"));
        ci.setOsName(System.getProperty("os.name"));
        ci.setUserHome(System.getProperty("user.home"));
        ci.setUserName(System.getProperty("user.name"));
        ci.setJavaVersion(System.getProperty("java.version"));

        objectOut.writeObject(ci);

    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        SimpleClient client = new SimpleClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }
}

