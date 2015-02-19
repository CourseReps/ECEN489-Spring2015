class ServerRunnable implements Runnable {

    private ServerPanel parent;
    private DBHandler dbHandler;
    private CommHandler commHandler;

    // This class just acts as a central place to start and manage the different threads that run as a part of this
    //      database management program.  It allows for expansion as the program becomes more complicated
    ServerRunnable(ServerPanel parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        dbHandler = new DBHandler(this);
        commHandler = new CommHandler(this);

        Thread dbThread = new Thread(dbHandler);
        Thread commThread = new Thread(commHandler);

        dbThread.start();
        commThread.start();
    }

    public DBHandler getDB() {
        return dbHandler;
    }

    public void newMessage(String message) {
        parent.newMessage(message);
    }

    public ServerPanel getUI() { return parent; }
}