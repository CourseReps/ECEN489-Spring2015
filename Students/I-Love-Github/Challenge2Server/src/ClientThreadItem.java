public class ClientThreadItem {

    private ClientConnection clientConnection;
    private Thread clientThread;

    ClientThreadItem(ClientConnection clientConnection, Thread thread) {
        this.clientConnection = clientConnection;
        this.clientThread = thread;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public Thread getClientThread() {
        return clientThread;
    }

    public void setClientThread(Thread clientThread) {
        this.clientThread = clientThread;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }
}
