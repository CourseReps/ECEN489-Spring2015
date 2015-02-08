class ClientRunnable implements Runnable {

    private ClientPanel parent;
    private DBHandler dbHandler;
    private ICHandler icHandler;
    private CommHandler commHandler;


    ClientRunnable(ClientPanel parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        dbHandler = new DBHandler(this);
        icHandler = new ICHandler(this);
        commHandler = new CommHandler(this);

        Thread dbThread = new Thread(dbHandler);
        Thread icThread = new Thread(icHandler);
        Thread commThread = new Thread(commHandler);

        dbThread.start();
        icThread.start();
        commThread.start();
    }

    public void updateUI(String message) {
        parent.updateUI(message);
    }

    public DBHandler getDB() {
        return dbHandler;

    }
}