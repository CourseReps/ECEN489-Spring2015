public class RunnableThread implements Runnable {

    // No constructor -- we don't need to initialize anything
    RunnableThread() {}

    // run() must be used in any class that implements Runnable
    @Override
    public void run() {

        long value = 0;

        // Infinite loop
        while (true) {
            try {

                // Perform a simple math operation to keep the processor working
                value++;

                //Then take a nap for 1ms
                Thread.sleep(1);

            } catch (InterruptedException e) {
                // We won't get any interrupts at this level
            }
        }
    }
}
