import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class ClientConnection implements Runnable {

    final static public String MESSAGE = "/msg";
    final static public String USERLIST_ADD = "/add";
    final static public String USERLIST_REMOVE = "/rem";
    final static public String USERLIST_CHANGE = "/chg";

    final static public String QUIT = "/quit";
    final static public String JOIN_SERVER = "/join";
    final static public String SET_USERNAME = "/name";
    final static public String GET_USER_LIST = "/list";

    final public int id;

    private String userName;
    private ServerRunnable parent;

    private Socket clientSocket;
    private PrintWriter toClient;

    ClientConnection(ServerRunnable parent, Socket clientSocket, int id) {
        this.clientSocket = clientSocket;
        this.parent = parent;
        this.id = id;
    }

    public void run() {

        boolean handshake = true;

        BufferedReader toServer = null;

        try {
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            toServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String stringIn;

            while (clientSocket.isConnected()) {
                stringIn = toServer.readLine();

                if (stringIn != null && stringIn.length() > 0) {
                    processString(stringIn);
                }
            }
//            parent.parent.updateStatusBox("Stopping client from ClientConnection");

        } catch(IOException e) {
            parent.parent.updateStatusBox(id + " " + e.getMessage());

            //TODO: IO Exceptions should be handled
        } finally {
            try {
                toServer.close();
                toClient.close();

                if (clientSocket != null && clientSocket.isConnected()) {
                    clientSocket.close();
                }

            } catch(IOException e) {
                parent.parent.updateStatusBox(e.getMessage());
                //TODO: Handle this IOE
            }
        }

        parent.clientThreadEnding(this);
    }

    public void closeSocket() {

        try {

            if (clientSocket != null && clientSocket.isConnected()) {
                toClient.write('q');
                toClient.flush();
                clientSocket.close();
                clientSocket.shutdownInput();
            }
        } catch(IOException e) {
            parent.parent.updateStatusBox(id + " " + e.getMessage());
            // TODO: handle IO exception
        }
    }

    private void processString(String string) {

        parent.parent.updateStatusBox(string);

        if (string.startsWith(MESSAGE)) {
            String newString = string.replace(MESSAGE, "");
            parent.broadcastMsg(MESSAGE + userName + "> " + newString);

        } else if (string.startsWith(QUIT)) {
            closeSocket();

        } else if (string.startsWith(SET_USERNAME)) {

            String newString = string.replace(SET_USERNAME, "");

            if (newString.charAt(0) == ' ') {
                newString = newString.substring(1);
            }

            StringTokenizer token = new StringTokenizer(newString, ",");
            String newName = token.nextToken();
            String oldName = null;

            if (token.hasMoreTokens()) {
                oldName = token.nextToken();
            }

            String returnName = parent.checkUserName(newName, oldName);
            this.userName = returnName;

            if (oldName != null) {
                parent.broadcastMsg(USERLIST_CHANGE + returnName + "," + oldName);
            } else {
                xmitMessage(SET_USERNAME + returnName);
            }

        } else if (string.startsWith(JOIN_SERVER)) {
            parent.clientJoined(this);

        } else if (string.startsWith(GET_USER_LIST)) {

            Vector<String> userList = parent.getUserList();
            Iterator nameIterator = userList.iterator();
            String returnString = (String) nameIterator.next();

            while (nameIterator.hasNext()) {
                returnString = returnString + "," + nameIterator.next();
            }

            xmitMessage(GET_USER_LIST + returnString);
        }
    }

    public void xmitMessage(String string) {
        if (userName != null || toClient != null) {
            toClient.println(string);
        }
    }

    public String getUserName() {
        return userName;
    }

    public boolean isConnected() {
        if (clientSocket != null) {
            return clientSocket.isConnected();
        } else {
            return false;
        }
    }
}
