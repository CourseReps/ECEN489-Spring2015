import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommHandler implements Runnable {

    ClientRunnable parent;

    private static String host = "127.0.0.1";
    private static int port = 5555;

    private Socket socket;
    private PrintWriter xmit;
    private BufferedReader recv;

    CommHandler(ClientRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        // Client initialization
        parent.updateUI("Looking for connection to master...");
        parent.updateUI("Connection established!");
        boolean handshake = true;
        String stringIn;

        while (true) {
            // SERVER CONNECTION LOOP AND CLIENT THREAD GENERATION
            try {
                if (socket == null || socket.isClosed()) {
                    socket = new Socket(host, port);
                }

                xmit = new PrintWriter(socket.getOutputStream(), true);
                recv = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                parent.updateUI("Connection established!");

                if (handshake) {
                    xmit.println("test");
                }

                while (socket.isConnected()) {
                    stringIn = recv.readLine();

                    if (stringIn != null && stringIn.length() > 0) {
                        processString(stringIn);
                    }
                }


            } catch (IOException e) {

            } finally {
                try {

                    if (!(socket == null || !socket.isClosed())) {
                        xmit.flush();
                        xmit.close();
                        recv.close();
                        socket.close();
                    }
                } catch (IOException e) {

                }
            }

            try {

                Thread.sleep(4000);

                parent.updateUI("Updating master with current db...");
                parent.updateUI("Master updated!");

            } catch (InterruptedException e) {
                parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    private void processString(String string) {
        // nothing yet
    }
}
