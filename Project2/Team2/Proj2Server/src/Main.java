class Main {

    public static void main(String args[]) {

        ServerRunnable server = new ServerRunnable();
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}