public class Master {

    CommToServer commToServer;

    Master() {

        commToServer = new CommToServer(this);
        Thread commThread = new Thread(commToServer);
        commThread.start();

        Parser parser = new Parser(this);
        Thread parseThread = new Thread(parser);
        parseThread.start();

    }

    public CommToServer getCommToServer() {
        return commToServer;
    }
}
