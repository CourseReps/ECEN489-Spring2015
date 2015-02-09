public class RunnableThread implements Runnable {

    // This class uses the isRunning field to end the loop
    private boolean isRunning;
    // Reference to the ThreadManager so it can tell it that it's ending
    private ThreadManager parent;

    // Constructor
    RunnableThread(ThreadManager parent) {
        this.parent = parent;
    }

    // run() must be used in any class that implements Runnable
    @Override
    public void run() {

        isRunning = true;
        long value = 0;

        // loop while we're running
        while (isRunning) {
            try {

                // Perform a simple math operation to keep the processor working
                value++;

                //Then take a nap for 1ms
                Thread.sleep(1);

            } catch (InterruptedException e) {
                // We won't get any interrupts at this level
            }
        }

        // THREAD SAFETY //
        // This command tells the parent thread that it's ending.  This should be the LAST OPERATION of the run()
        // function.  This ensures that we don't lose track of any locked or blocked threads.
        parent.threadEnding(this);
    }

    // boolean assignment operations are atomic
    public void stopRunning() {
        this.isRunning = false;
    }
}
