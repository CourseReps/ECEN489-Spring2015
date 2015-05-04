import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mandel on 3/1/15.
 */
public class Main {

    public static void main(String args[]) {

        int fileCount = 0;
        File currentFile = null;
        int minCount = 1;
        HashMap<String,String> newLoginMap = new HashMap<String, String>();
        HashMap<String,String> oldLoginMap = new HashMap<String, String>();
        MacParser macParser = new MacParser();
        FileBuffer fileBuffer = new FileBuffer();
        //MacCheckIn checkIn = MacCheckIn();

        HashMap<String, String> whiteList = new HashMap<String, String>();
        //Build map of mac address to users from textfile
        
        try {
            File csvData = new File("macWhitelist.txt"); //use your own filename
            CSVParser parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withCommentMarker('#').withIgnoreEmptyLines(true).withIgnoreSurroundingSpaces(true).withQuoteMode(QuoteMode.MINIMAL));
            for (CSVRecord csvRecord : parser) {
                if (csvRecord.get(0).equals("")) continue;
                if (csvRecord.get(1).equals("")) continue;
                
                whiteList.put(csvRecord.get(0).toLowerCase(), csvRecord.get(1));

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        
        while (true) {

        fileBuffer.updateBuffer();

            if (fileBuffer.size() > minCount) {
                currentFile = fileBuffer.getNextFile();
                if (currentFile.isFile()){
                    newLoginMap = macParser.parseFile(whiteList,currentFile);
                    fileBuffer.popFile();
                }

            } else {
                try {
                    newLoginMap.clear();
                    System.out.println("Thread sleeping for 30 sec");
                    Thread.sleep(30000); //sleep for 30 sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

           // newLoginMap.put("30:f7:c5:51:85:76", "123456789");
            if(!newLoginMap.isEmpty()){
                for(String mac : newLoginMap.keySet()){
                    String newTimestamp = newLoginMap.get(mac);
                    String username = whiteList.get(mac);
                    if (oldLoginMap.containsKey(mac)){
                        String oldTimestamp = oldLoginMap.get(mac);
                        int nTs = Integer.parseInt(newTimestamp);
                        int oTs = Integer.parseInt(oldTimestamp);
                        if(nTs-oTs > 60){
                            try {
                                CheckInClient.checkIn(username, "EIC", "password", nTs);
                            }
                            catch (IOException e){
                                System.out.println("Failed to Connect to Server");
                            }
                        }
                    }
                    else {
                        try {
                            CheckInClient.checkIn(username, "EIC", "password", Integer.parseInt(newTimestamp));
                        }
                        catch (IOException e){
                            System.out.println("Failed to Connect to Server");
                        }
                    }
                 
                }
            }
            
            oldLoginMap = newLoginMap;
            newLoginMap.clear();
            
        }

    }
}
