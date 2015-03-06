import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;


public class Main {

    public static void main(String[] args) throws Exception {

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:prombox.db");
        Statement stat = conn.createStatement();

        //Remove Table DATA  if Exists
        stat.executeUpdate("drop table if exists DATA;");

        //Create Table DATA
        stat.executeUpdate("create table DATA (TIME, MAC);");
        System.out.println("DATA Table Created");

        //----------------
        //Begin table for PBID
        //---------------
        //Remove Table if Exists
        stat.executeUpdate("drop table if exists PBID;");

        //Create Table
        stat.executeUpdate("create table PBID (ID);");

        //Entering in the PBID Information
        PreparedStatement pbid = conn.prepareStatement("insert into PBID values (?);");
        pbid.setInt(1, 4);
        pbid.addBatch();
        pbid.executeBatch();
        pbid.close();

        System.out.println("PBID Table Completed");

        //--------------------
        //End of Table for PBID
        //--------------------


        //Initializing
        File f;
        File[] files;
        ArrayList<File> fileList;
        long checkCounter;
        int totalLines = 0;
        HashSet<String> entrySet = new HashSet<String>();
        String lastTime = "null";
        StringTokenizer tokenizer;
        String thisTime;




        //Read in a CSV File
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
            //Making sure that there are enough csv files to work properly
            if (checkCounter >= 2) {

                f = fileList.get(0);
                System.out.print("Working with file: " + f.getName() + "\n");
                try {
                    //Start reading the file
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String line = br.readLine();

                    //BEGIN Parsing and Filtering
                    while (line != null) {

                        //Tokenizer
                        tokenizer = new StringTokenizer(line, ",");
                        thisTime = tokenizer.nextToken();
                        // rs = stat.executeQuery("SELECT * FROM DATA;");

                        String[] values = line.split(",");    //Comma separator

                        //Changing the time from a string into an integer
                        long timeint = Integer.parseInt(values[0]);

                        String insert = values[2];


                        //Printing Values
                        //System.out.println("Timeint: " + timeint);
                        //System.out.println("Values[Time]: " + values[0]);
                        //System.out.println("MAC: " + values[2]);

                        //Refer to Database
                        //ResultSet rs = stat.executeQuery("SELECT * FROM DATA ");
                        //String lastTime = rs.getString("TIME");

                        //Calling Latest String -
                        String currentTime = values[0];

                        //If time changes
                        if (!lastTime.equals(currentTime)) {
                            entrySet = new HashSet<String>();
                            Iterator<String> iterator = entrySet.iterator();
                            //System.out.println("I'm aware my time is different");

                        //Dumping into Database
                            while (iterator.hasNext()) {
                                System.out.println("Made to While Loop");
                                String newMac = iterator.next();
                                String sql = "INSERT INTO DATA (TIME, MAC) " + "VALUES (" + values[0] + ", '" + newMac + "');";
                                stat.executeUpdate(sql);
                            }
                        }

                        //Store into HashSet if not null
                        if (!values[2].toLowerCase().contains("ff:ff:ff:ff:ff") && !values[2].contains("null")) {
                            lastTime = currentTime;
                            while (lastTime.equals(currentTime) || line != null){
                                values = line.split(",");
                                entrySet.add(values[2]);
                                entrySet.add(values[2].replaceAll("\\s+", ""));
                                //System.out.println("MAC: " + values[2]);
                                line = br.readLine();
                                insert = values[2];
                                currentTime = values[0];
                            }
                            //System.out.println("MAC Found");
                            //entrySet.add(values[2]);
                            //entrySet.add(values[2].replaceAll("\\s+", ""));
                            line = br.readLine();
                        }
                        //lastTime=currentTime;
                        line = br.readLine();
                    }
                    f.setWritable(true);
                    f.delete();

                    br.close();

                } catch (Exception e) {
                    System.out.print("FileParser: " + e.getMessage() + "\n");
                }
            }
            else {
                    try {
                        System.out.println("Not enough files -- pausing for 30 seconds \n");
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        System.out.println("FileParser2: " + e.getMessage());
                    }
                }

            conn.close();
            }
        //System.out.println("Finished!");
        }
}

