import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class FileParser implements Runnable {

    private ServerRunnable parent;
    private boolean isRunning;

    FileParser(ServerRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        // Give the database a few seconds to get started before we begin parsing files
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            parent.newMessage("FileParser: " + e.getMessage());
        }

        isRunning = true;
        long fileCounter = 0;
        long checkCounter;
        File f = null;
        BufferedReader reader;
        String line;
        StringTokenizer tokenizer;
        String thisTime;
        String lastTime = "0";
        boolean firstTime = true;
        String thisMac;
        ArrayList<String> macAddresses = new ArrayList<String>();
        ArrayList<String> macToDBList;
        long sortCounter;
        String sortMac;
        Iterator<String> iterator;
        Iterator<String> sortIterator;
        File[] files;

        while (isRunning) {

            checkCounter = 0;
            files = new File(".").listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().contains("snifflog_") && file.getName().endsWith(".csv")) {
                    checkCounter++;
                }
            }

            if (checkCounter >= 2) {

                while (f == null || !f.exists()) {
                    f = new File("./sniffLog_" + String.valueOf(fileCounter) + ".csv");
                    if (!f.exists()) {
                        fileCounter++;
                    }
                }

                parent.newMessage("Working with file: " + f.getName());

                try {

                    reader = new BufferedReader(new FileReader(f));
                    line = reader.readLine();

                    while (line != null) {
                        tokenizer = new StringTokenizer(line, ",");
                        thisTime = tokenizer.nextToken();

                        // Handle the array list and stick them in the DB when one second of records has been parsed
                        if (!lastTime.equals(thisTime) && !firstTime) {

                            // Sift garbage data -- throw out any MAC we don't see more than once
                            iterator = macAddresses.iterator();
                            macToDBList = new ArrayList<String>();
                            while (iterator.hasNext()) {
                                sortCounter = 0;
                                thisMac = iterator.next();

                                // Count the number of occurences of thisMac in the macAddresses ArrayList
                                // If there is more than one occurence, we stick it into the to-write ArrayList
                                if (!macToDBList.contains(thisMac)) {
                                    sortIterator = macAddresses.iterator();
                                    while (sortIterator.hasNext()) {
                                        sortMac = sortIterator.next();
                                        if (sortMac.contains(thisMac)) {
                                            sortCounter++;
                                        }
                                    }

                                    if (sortCounter > 1) {
                                        macToDBList.add(thisMac);
                                    }
                                }
                            }

                            // Write cleaned list of MAC addresses to the database
                            iterator = macToDBList.iterator();
                            while (iterator.hasNext()) {
                                thisMac = iterator.next();
                                parent.getDB().dataEntry(lastTime, thisMac);
                            }
                            parent.newMessage("Wrote to database " + macToDBList.size()
                                    + " MACS for timestamp: " + lastTime);

                            macAddresses = new ArrayList<String>();
                        }

                        tokenizer.nextToken(); // Dispose of microsecond timestamp

                        thisMac = tokenizer.nextToken();
                        if (!thisMac.toLowerCase().contains("ff:ff:ff:ff:ff") && !thisMac.contains("null")) {
                            macAddresses.add(thisMac);
                        }

                        thisMac = tokenizer.nextToken();
                        if (!thisMac.toLowerCase().contains("ff:ff:ff:ff:ff") && !thisMac.contains("null")) {
                            macAddresses.add(thisMac);
                        }

                        if (firstTime) {
                            firstTime = false;
                        }

                        lastTime = thisTime;
                        line = reader.readLine();
                    }

                    reader.close();
                    f.delete();

                } catch (Exception e) {
                    parent.newMessage("FileParser: " + e.getMessage());

                }

            } else {
                try {
                    parent.newMessage("Not enough files -- pausing for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    parent.newMessage("FileParser: " + e.getMessage());
                }
            }
        }
    }
}
