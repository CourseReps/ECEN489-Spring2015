import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class PcapParser implements Runnable {

    MainRunnable parent;

    PcapParser(MainRunnable parent) {
        this.parent = parent;
    }

    public void run() {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            LinkedHashMap<String, PacketInfo> entrySet = new LinkedHashMap<String, PacketInfo>();

            PacketInfo packetInfo = null;

            long packetsReceived = 0;
            String line;
            StringTokenizer tokenizer;
            String thisTime;
            String thisTimeNano;
            String lastTime = "NULL";
            String thisMac;
            String sigStrength;

            while (true) {

                line = reader.readLine();

                if (line != null) {

                    packetsReceived++;
                    parent.getUI().updateReceived(packetsReceived);

                    tokenizer = new StringTokenizer(line, ",");
                    thisTime = tokenizer.nextToken();
                    thisTimeNano = tokenizer.nextToken();

                    if (!lastTime.equals(thisTime)) {
                        parent.getUI().updateTable(entrySet);
                        entrySet.clear();
                    }

                    thisMac = tokenizer.nextToken();
                    thisMac = thisMac.replaceAll("\\s+", "");
                    sigStrength = tokenizer.nextToken();

                    if (entrySet.containsKey(thisMac)) {
                        continue;
                    } else {
                        packetInfo = new PacketInfo(thisTime, thisTimeNano, thisMac, sigStrength);
                        entrySet.put(thisMac, packetInfo);
                        parent.getSqlDatabase().writeLine(packetInfo);
                        parent.getUI().updateTime(packetInfo);
                    }

                    lastTime = thisTime;
                }
            }
        } catch (Exception e) {
            System.out.print(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
