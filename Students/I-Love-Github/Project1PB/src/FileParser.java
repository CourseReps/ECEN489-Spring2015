import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class FileParser implements Runnable {

    private ServerRunnable parent;
    private boolean isRunning;

    FileParser(ServerRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            parent.newMessage("FileParser: " + e.getMessage());
        }

        isRunning = true;
        long fileCounter = 0;
        long checkCounter = 0;
        File f = null;
        BufferedReader reader;
        String line;
        StringTokenizer tokenizer;
        String thisTime;
        String lastTime = "0";
        boolean firstTime = true;
        String thisMac;
        ArrayList<String> macAddresses = new ArrayList<String>();
        Iterator<String> iterator;
        File[] files;

        while (isRunning) {

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

                try {

//                        f = new File(path.toURI());
                    reader = new BufferedReader(new FileReader(f));

                    line = reader.readLine();

                    while (line != null) {
                        tokenizer = new StringTokenizer(line, ",");

                        thisTime = tokenizer.nextToken();
                        if (!lastTime.equals(thisTime) && !firstTime) {
                            iterator = macAddresses.iterator();
                            while (iterator.hasNext()) {
                                thisMac = iterator.next();
                                parent.getDB().dataEntry(thisTime, thisMac);
                            }

                            macAddresses = new ArrayList<String>();
                        }

                        tokenizer.nextToken(); // Dispose of microsecond timestamp
                        thisMac = tokenizer.nextToken();
                        if (!macAddresses.contains(thisMac)) {
                            macAddresses.add(thisMac);
                        }
                        tokenizer.nextToken(); // Destination MAC also doesn't give us any useful information
//                        thisMac = tokenizer.nextToken();
//                        if (!macAddresses.contains(thisMac)) {
//                            macAddresses.add(thisMac);
//                        }

                        if (firstTime) {
                            firstTime = false;
                        }

                        lastTime = thisTime;
                        line = reader.readLine();
                    }

                    f.delete();

                } catch (Exception e) {
                    parent.newMessage("FileParser: " + e.getMessage());

                }

            } else {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    parent.newMessage("FileParser: " + e.getMessage());
                }
            }
        }
    }
}
