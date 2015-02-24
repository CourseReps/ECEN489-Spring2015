import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/*
 * Created by mandel on 2/23/15.
 */
public class MacParser {
    public static void main(String args[]){
 
   try{
        File csvData = new File("/Users/mandel/Downloads/snifflog_1.csv"); //use your own filename
        CSVParser parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.RFC4180);
       //Hashmap for efficiency. 
       // 10000 initial values affects efficiency, needs to be adjusted to expectedNumber/.75
       HashMap<String,HashSet<String>> recordMap = new HashMap<String, HashSet<String>>(10000);

        for (CSVRecord csvRecord : parser) {
            String sourceMac = csvRecord.get(2);
            String timeStamp = csvRecord.get(0);
            String destMac = csvRecord.get(3);
            
            if (!sourceMac.equals("null") && !destMac.equals("ff:ff:ff:ff:ff:ff")){//igonores nulls and broadcasts
                if(recordMap.containsKey(sourceMac)){ //if mac address exists Ө(1)
                    recordMap.get(sourceMac).add(timeStamp);//adds to the hashset. Ө(1)
                }
                else{
                    recordMap.put(sourceMac,new HashSet<String>()); //if doesn't exist, create new. Search in Ө(1)
                    recordMap.get(sourceMac).add(timeStamp); //add Ө(1)
                }
            }
            
        }
       System.out.println(recordMap.size());
               
    }
    catch(Exception e){
        e.printStackTrace();
        
    }
}
}