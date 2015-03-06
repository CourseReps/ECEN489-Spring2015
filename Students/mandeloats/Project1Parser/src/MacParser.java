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
    public void parseFile(File fileToParse){

        Connection c = null;
        Statement stmt = null;
        HashMap<String,HashSet<String>> recordMap = new HashMap<String, HashSet<String>>(5000);//Hashmap for efficiency.
        HashMap<String, String> macMap = new HashMap<String, String>((int)(25909/.75));// 10000 initial values affects efficiency, needs to be adjusted to expectedNumber/.75
        
   try{

       Class.forName("org.sqlite.JDBC");
       c = DriverManager.getConnection("jdbc:sqlite:prombox.db");
       System.out.println("Opened database successfully");
       
       //Build map of mac address companies
       File csvData = new File("macmap.txt"); //use your own filename
       CSVParser parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.TDF.withCommentMarker('#').withIgnoreEmptyLines(true).withIgnoreSurroundingSpaces(true).withQuoteMode(QuoteMode.MINIMAL));
       for (CSVRecord csvRecord : parser) {
           if(csvRecord.get(0).equals("")) continue;
           if(csvRecord.get(1).equals("")) continue;
           
           macMap.put(csvRecord.get(0).toLowerCase(), csvRecord.get(1));

       }
       
      //build map of data
       csvData = fileToParse; //use your own filename
        parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.RFC4180);

        for (CSVRecord csvRecord : parser) {
            String sourceMac = csvRecord.get(2);
            String timeStamp = csvRecord.get(0);
            String destMac = csvRecord.get(3);
            
            if(sourceMac.equals("null")) continue;//ignores nulls
            if(destMac.equals("ff:ff:ff:ff:ff:ff")) continue;//igonores broadcasts

            String sourceMacIdentity = sourceMac.substring(0,8);
            String destMacIdentity = destMac.substring(0,8);
            
            if(macMap.containsKey(sourceMacIdentity)) {
                if (recordMap.containsKey(sourceMac)) { //if mac address exists Ө(1)
                    recordMap.get(sourceMac).add(timeStamp);//adds to the hashset. Ө(1)
                } else {
                    recordMap.put(sourceMac, new HashSet<String>()); //if doesn't exist, create new. Search in Ө(1)
                    recordMap.get(sourceMac).add(timeStamp); //add Ө(1)
                }
            }
            
            if(macMap.containsKey(destMacIdentity)) {
                if (recordMap.containsKey(destMac)) { //if mac address exists Ө(1)
                    recordMap.get(destMac).add(timeStamp);//adds to the hashset. Ө(1)
                } else {
                    recordMap.put(destMac, new HashSet<String>()); //if doesn't exist, create new. Search in Ө(1)
                    recordMap.get(destMac).add(timeStamp); //add Ө(1)
                }
            }
            
        }
       System.out.println(recordMap.size());

       //build databas from hashmaps

            stmt = c.createStatement();
          /*  String sql = "CREATE TABLE IF NOT EXISTS MACINFO " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " MACADDRESS           TEXT    NOT NULL," +
                    " TIME     TEXT    NOT NULL," +
                    " COMPANY   TEXT    NOT NULL)";
                    */
       
       String sql = "CREATE TABLE IF NOT EXISTS DATA " +
               "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
               " TIME   LONG    NOT NULL," +
               " MAC    TEXT    NOT NULL)";
               
            stmt.executeUpdate(sql);
       sql = "CREATE TABLE IF NOT EXISTS PBID " + "(ID INTEGER PRIMARY KEY)";
       stmt.executeUpdate(sql);
       sql = "INSERT OR IGNORE INTO PBID (ID) VALUES (1);";
       stmt.executeUpdate(sql);

            for (String macAddress : recordMap.keySet()) {
                String macIdentity = macAddress.substring(0,8);
                String company = macMap.get(macIdentity);
                for(String timestamp : recordMap.get(macAddress)){
                    long time = Long.parseLong(timestamp);
                    sql = "INSERT INTO DATA (TIME,MAC) "+ "VALUES ("+time+", '"+macAddress+"');";
                    stmt.executeUpdate(sql);
                }

            }
            
            stmt.close();
            c.close();  
       System.out.println("Closed database successfully");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}