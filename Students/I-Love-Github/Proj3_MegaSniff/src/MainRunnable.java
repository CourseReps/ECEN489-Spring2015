public class MainRunnable implements Runnable {

    private static int cycleSeconds = 5;

    public static int MODE_USER_INPUT = 0;
    public static int MODE_CYCLE = 1;
    private int operatingMode;

    InterfaceFrame userInterface;

    SerialComm serialComm;
    SocketConnection socketConnection;
    SqlDatabase sqlDatabase;
    PcapParser parser;

    MainRunnable() {

        userInterface = new InterfaceFrame("User Display", this);

        serialComm = new SerialComm();
        serialComm.connect("/dev/ttyACM0");

        socketConnection = new SocketConnection(this);
        Thread clientThread = new Thread(socketConnection);
        clientThread.start();

        sqlDatabase = new SqlDatabase(this);

        parser = new PcapParser(this);
        Thread parserThread = new Thread(parser);
        parserThread.start();

        operatingMode = MODE_CYCLE;
        (new Thread(this)).start();
    }


    public SqlDatabase getSqlDatabase() {
        return sqlDatabase;
    }

    public SerialComm getSerialComm() {
        return serialComm;
    }

    public InterfaceFrame getUI() {
        return userInterface;
    }

    public void setOperatingMode(int operatingMode) {
        this.operatingMode = operatingMode;
    }

    public SocketConnection getSocket() {
        return socketConnection;
    }


    // Loop for input cycling
    public void run() {

        int count = 0;
        try {
            while (true) {

                if (operatingMode == MODE_CYCLE) {

                    count++;
                    if (count == 4) {
                        count = 0;
                    }

                    socketConnection.simulateInput(count);
                    Thread.sleep(cycleSeconds * 1000);

                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            System.out.print(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
