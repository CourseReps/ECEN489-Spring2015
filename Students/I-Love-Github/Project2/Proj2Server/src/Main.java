class Main {

    public static void main(String args[]) {

        ServerRunnable client = new ServerRunnable(); 
		Thread clientThread = new Thread(client);
		clientThread.start();
    }
}