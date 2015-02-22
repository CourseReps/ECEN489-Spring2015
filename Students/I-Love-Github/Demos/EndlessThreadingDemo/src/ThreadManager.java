public class ThreadManager implements Runnable {

    UserInterface parent;

    // Constructor creates a connection to the object that created it, so we can interact with it
    // In this case, the parent is the user interface.  We need this reference so we can manipulate the interface
    ThreadManager(UserInterface parent) {
        this.parent = parent;
    }

    // run() must be used in any class that implements Runnable
    @Override
    public void run() {

        parent.flipButtons();

        long numberOfThreads = 0;

        // Endless loop
        while (true) {
            try {

                // Create a new RunnableThread and start it
                RunnableThread runThread = new RunnableThread();
                Thread thread = new Thread(runThread);
                thread.start();

                // Update the GUI with the number of threads that have been created
                numberOfThreads++;
                parent.updateUI(numberOfThreads);

                // Sleep for 10ms before creating the next thread
                Thread.sleep(10);

            } catch (InterruptedException e) {
                // We won't get any interrupts at this level
            }
        }
    }
}
