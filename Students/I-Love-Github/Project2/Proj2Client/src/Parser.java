import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class Parser implements Runnable {

    Master master;

    Parser (Master master) {
        this.master = master;
    }

    public void run() {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            LinkedHashMap<String, String> entrySet = new LinkedHashMap<String, String>();
            String line;
            StringTokenizer tokenizer;
            String thisTime;
            String lastTime = "NULL";
            String thisMac;
            String sigStrength;
            int lineCount = 0;
            int totalLines = 0;
            long lastMillis = 0;
            long thisMillis = 0;

            while (true) {

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

                        master.getCommToServer().sendList(entrySet);

                        entrySet = new LinkedHashMap<String, String>();
                        lineCount = 0;
                    }

                    thisMac = tokenizer.nextToken();
                    if (!thisMac.toLowerCase().contains("ff:ff:ff:ff:ff") && !thisMac.contains("null")) {
                        thisMac = thisMac.replaceAll("\\s+", "");
                        sigStrength = tokenizer.nextToken();
                        entrySet.put(thisMac, sigStrength);
                    }

                    lastTime = thisTime;
                    line = reader.readLine();
                    lineCount++;
                    totalLines++;
                }

                reader.close();

            }
        } catch (Exception e) {

        }
    }
}
