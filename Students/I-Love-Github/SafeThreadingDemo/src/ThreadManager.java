import java.util.Iterator;
import java.util.Vector;

public class ThreadManager implements Runnable {

    UserInterface parent;

    // isRunning allows the user interface to tell the ThreadManager to stop doing its thing
    private boolean isRunning;

    // THREAD SAFETY //
    // We will create some Vectors (lists) to keep track of all of our threads
    private Vector<Thread> threadList;
    private Vector<RunnableThread> runnableList;

    // Constructor creates a connection to the object that created it, so we can interact with it
    // In this case, the parent is the user interface.  We need this reference so we can manipulate the interface
    ThreadManager(UserInterface parent) {
        this.parent = parent;
    }

    // run() must be used in any class that implements Runnable
    @Override
    public void run() {

        isRunning = true;
        long numberOfThreads = 0;
        int index = 0;

        // Initialize the Vectors
        threadList = new Vector<Thread>();
        runnableList = new Vector<RunnableThread>();

        parent.flipButtons(true);

        // Will stop if the GUI turns off isRunning
        while (isRunning) {
            try {

                // Create a new RunnableThread and start it
                RunnableThread runnable = new RunnableThread(this);
                Thread thread = new Thread(runnable);
                thread.start();

                // THREAD SAFETY //
                // Add the runnable and thread objects to a list so you can reference them
                runnableList.add(runnable);
                index = runnableList.indexOf(runnable);
                threadList.add(index, thread);
                // The INDEX value ensures that the thread and runnable are in the SAME place in both lists, that way
                // we don't have to waste computing time hunting them down...if that's even possible

                // To avoid an extra line of code for index, you can combine the commands:
                // threadList.add(runnableList.indexOf(runnable), thread);

                // Update the GUI with the number of threads that are running
                numberOfThreads = threadList.size();
                parent.updateUI(numberOfThreads);

                // Sleep for 10ms before creating the next thread
                Thread.sleep(10);

            } catch (InterruptedException e) {
                // We won't get any interrupts at this level
            }
        }

        // Declare some blank variables for the cleanup loop
        Vector<RunnableThread> mirrorList;
        Iterator<RunnableThread> iterator;
        RunnableThread thisRunnableThread;

        // Second loop to enforce CLEANUP
        while (threadList.size() > 0) {
            try {

                // THREAD SAFETY //
                // Even though vectors are SYNCHRONIZED, we're going to have threads pulling themselves out of runnableList
                // as we iterate through all the threads.  That will cause us to throw exceptions, so we need to create a copy
                // of this list that we don't have to worry about it
                mirrorList = new Vector<RunnableThread>();
                mirrorList.addAll(runnableList);

                // THREAD SAFETY //
                // When it comes to lists, using the = operator works like a pointer in C, it just points the variable at the
                // other list.  UNCOMMENT THE LINE BELOW to see what happens if you don't make a copy of the thread
                // mirrorList = runnableList;

                // Now we create an iterator so we can send the STOP command to all of our running threads
                iterator = mirrorList.iterator();
                while (iterator.hasNext()) {
                    thisRunnableThread = iterator.next();
                    thisRunnableThread.stopRunning();
                }

                // Take a nap (a longer one to give the threads time to die)
                Thread.sleep(1);

                // Update the GUI with the number of threads that are running
                numberOfThreads = threadList.size();
                parent.updateUI(numberOfThreads);

            } catch (InterruptedException e) {
                // We won't get any interrupts at this level
            }
        }

        // Let the next level up know that we're done
        parent.doneRunning();
    }


    // PUBLIC METHODS //

    // THREAD SAFETY //
    // This is redundant because vectors are naturally thread-safe, but synchronizing a function is one way of
    // ensuring that other data types are executed one at a time
    synchronized public void threadEnding(RunnableThread thisRunnable) {

    // COMMENT THE LINE ABOVE UNCOMMENT THE LINE BELOW to see what happens when you don't synchronize this function
    // You throw exceptions because you're removing things from the vector at the SAME TIME that you're building your
    // vector in the list above.  You end up trying to look outside of your Vector, and Java freaks out
    // public void threadEnding(RunnableThread thisRunnable) {

        // Because we put the thread and runnable in the same place in both lists, we only need to find the index
        // of the runnable passed to this method
        int index = runnableList.indexOf(thisRunnable);

        // Because the function is synchronized, we can just change runnableList all we want without worrying about
        // thread-safety
        runnableList.remove(index);

        // An alternative to declaring an entire function as synchronized (really costly for larger functions), you
        // can just put a synchronized block around the data you're working with
        // This is the MORE CORRECT way to synchronize data types

        synchronized (threadList) {
            threadList.remove(index);
        }
    }

    // boolean operations are atomic and technically don't require synchronization
    public void stopRunning() {
        this.isRunning = false;
    }
}
