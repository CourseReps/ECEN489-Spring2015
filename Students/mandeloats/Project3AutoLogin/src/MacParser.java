import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import java.nio.charset.StandardCharsets;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.sql.*;
/*
 * Created by mandel on 2/23/15.
 */
public class MacParser {
    public HashMap<String,String> parseFile(HashMap<String, String> whiteList, File fileToParse){

        HashMap<String,String> recordMap = new HashMap<String,String>();//Hashmap for efficiency.


   try {

       //build map of data
       File csvData = fileToParse; //use your own filename
       CSVParser parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.RFC4180);

       for (CSVRecord csvRecord : parser) {
           String sourceMac = csvRecord.get(2);
           String timeStamp = csvRecord.get(0);
           String destMac = csvRecord.get(3);

           if (sourceMac.equals("null")) continue;//ignores nulls
           if (destMac.equals("ff:ff:ff:ff:ff:ff")) continue;//igonores broadcasts
           if(whiteList.containsValue(sourceMac)){
               recordMap.put(sourceMac,timeStamp);
           }
           else if(whiteList.containsValue(destMac)){
               recordMap.put(destMac,timeStamp);
           }

       }
    
   }
        catch(Exception e){
            e.printStackTrace();
        }
        return recordMap;
    }
}