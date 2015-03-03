import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

public class FileParser {

    static public void main(String[] args) {

        DBHandler db = new DBHandler();
        db.run();

        long checkCounter;
        File f;
        BufferedReader reader;
        String line;
        StringTokenizer tokenizer;
        String thisTime;
        String lastTime = "NULL";
        String thisMac;
        File[] files;
        ArrayList<File> fileList;
        HashSet<String> entrySet = new HashSet<String>();
        int lineCount = 0;
        int totalLines = 0;
        long lastMillis;
        long thisMillis;

        while (true) {

            fileList = new ArrayList<File>();
            System.out.print("Checking local directory");
            checkCounter = 0;
            files = new File(".").listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().contains("snifflog_") && file.getName().endsWith(".csv")) {
                    checkCounter++;
                    System.out.print("Checking " + file.getName() + "\n");
                    fileList.add(file);
                }
            }

            if (checkCounter >= 2) {

                thisMillis = System.currentTimeMillis();
                totalLines = 0;
                f = fileList.get(0);
                System.out.print("Working with file: " + f.getName() + "\n");

                try {

                    reader = new BufferedReader(new FileReader(f));
                    line = reader.readLine();

                    while (line != null) {
                        tokenizer = new StringTokenizer(line, ",");
                        thisTime = tokenizer.nextToken();

                        // Handle the hashmap and stick them in the DB when one second of records has been parsed
                        if (!lastTime.equals(thisTime)) {
                            System.out.print("TS: " + lastTime + "\tUnique MACs:" + entrySet.size() + "\n");
                            System.out.print(lineCount + " lines read\tFile total: " + totalLines + " \n");

                            lastMillis = thisMillis;
                            thisMillis = System.currentTimeMillis();
                            System.out.print((thisMillis - lastMillis) + " ms elapsed\n");
                            db.dataEntry(lastTime, entrySet);

                            entrySet = new HashSet<String>();
                            lineCount = 0;
                        }

                        tokenizer.nextToken(); // Dispose of microsecond timestamp

                        thisMac = tokenizer.nextToken();
                        if (!thisMac.toLowerCase().contains("ff:ff:ff:ff:ff") && !thisMac.contains("null")) {
//                            entrySet.add(thisMac);
                            entrySet.add(thisMac.replaceAll("\\s+", ""));
                        }

                        thisMac = tokenizer.nextToken();
                        if (!thisMac.toLowerCase().contains("ff:ff:ff:ff:ff") && !thisMac.contains("null")) {
//                            entrySet.add(thisMac);
                            entrySet.add(thisMac.replaceAll("\\s+", ""));
                        }

                        lastTime = thisTime;
                        line = reader.readLine();
                        lineCount++;
                        totalLines++;
                    }

                    reader.close();
                    f.setWritable(true);
                    f.delete();

                } catch (Exception e) {
                    System.out.print("FileParser: " + e.getMessage() + "\n");

                }

            } else {
                try {
                    System.out.print("Not enough files -- pausing for 30 seconds\n");
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    System.out.print("FileParser: " + e.getMessage());
                }
            }
        }
    }
}
