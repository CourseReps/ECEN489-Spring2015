package www.foureightynine.com.challenge4;

import android.app.Activity;

class ClientRunnable implements Runnable {

    private RootActivity parent;
    private DBHandler dbHandler;
    private ICHandler icHandler;
    private CommHandler commHandler;

    private long serverTS;
    private long clientTS;

    ClientRunnable(RootActivity parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        dbHandler = new DBHandler(this);
//        dbHandler = new DBHandler(parent.getApplicationContext(), this);
        icHandler = new ICHandler(this);
        commHandler = new CommHandler(this);

//        Thread dbThread = new Thread(dbHandler);
        Thread icThread = new Thread(icHandler);
        Thread commThread = new Thread(commHandler);

//        dbThread.start();
        icThread.start();
        commThread.start();
    }

    public void updateUI(String message) {
        parent.newMessage(message);
    }

    public DBHandler getDB() {
        return dbHandler;

    }

    public void newMessage(String message) {
        parent.newMessage(message);
    }

    public void setServerTS(long ts) {
        this.serverTS = ts;
    }

    public long getServerTS() {
        return serverTS;
    }

    public void setmyTS(long ts) {
        this.clientTS = ts;
    }

    public long getMyTS() {
        return clientTS;
    }

    public Activity getActivity() {
        return parent;
    }

    public CommHandler getCommHandler() { return commHandler; }
}