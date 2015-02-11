import java.util.ArrayList;

class ClientRunnable implements Runnable {

    private ClientPanel parent;
    private HighQualityRandom random;
    private ArrayList<Long> longList;

    ClientRunnable(ClientPanel parent) {
        this.parent = parent;
        random = new HighQualityRandom();
        longList = new ArrayList<Long>();
    }

    @Override
    public void run() {

        long numberCounter = 0;
        long multipleCounter = 0;

        while (true) {
            long id = Math.abs(random.nextLong());
            numberCounter++;

            if (longList.contains(Long.valueOf(id))) {
                multipleCounter++;
            }

            longList.add(id);
            parent.updateUI(numberCounter, multipleCounter);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}
        }
    }
}