class ServerRunnable implements Runnable {

    private ServerPanel parent;
    private DBHandler dbHandler;
    private FileParser fileParser;
    private CommHandler commHandler;
    public boolean isRunning;

    // This class just acts as a central place to start and manage the different threads that run as a part of this
    //      database management program.  It allows for expansion as the program becomes more complicated
    ServerRunnable(ServerPanel parent) {
        this.parent = parent;
        this.isRunning = false;
    }

    @Override
    public void run() {

        fileParser = new FileParser(this);
        dbHandler = new DBHandler(this);
//        commHandler = new CommHandler(this);

        Thread fileThread = new Thread(fileParser);
        Thread dbThread = new Thread(dbHandler);
//        Thread commThread = new Thread(commHandler);

        fileThread.start();
        dbThread.start();
//        commThread.start();

        isRunning = true;

        while (isRunning) {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                newMessage("Preparing to shut down server...");
            }
        }

        // Close down running threads
    }

    public DBHandler getDB() {
        return dbHandler;
    }

    public void newMessage(String message) {
        parent.updateStatusBox(message);
    }

    public ServerPanel getUI() { return parent; }

    public void notifyStopPressed(Thread myThread) {
        isRunning = false;
        myThread.notify();
    }
}