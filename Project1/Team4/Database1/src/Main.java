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
        Connection conn = DriverManager.getConnection("jdbc:sqlite:PB4.db");
        Statement stat = conn.createStatement();

        //Remove Table DATA  if Exists
        stat.executeUpdate("drop table if exists DATA;");

        //Create Table DATA
        stat.executeUpdate("create table DATA (ID, TIME, MAC);");
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
        String line;
        BufferedReader br;
        File f;
        File[] files;
        ArrayList<File> fileList;
        long checkCounter;
        int totalLines = 0;
        HashSet<String> entrySet = new HashSet<String>();
        String lastTime = "null";
        int counter = 1;

        //Read in a CSV File
        while (true) {

            fileList = new ArrayList<File>();
            System.out.print("Checking local directory");
            checkCounter = 0;
            files = new File(System.getProperty("user.dir")).listFiles();
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
                    br = new BufferedReader(new FileReader(f));
                    line = br.readLine();

                    //BEGIN Parsing and Filtering
                    while (line !=null) {

                        //Split the CSV line into an array of strings
                        String[] values = line.split(",");    //Comma separator

                        //Defining MAC address into easier to call string
                        String insert = values[2];

                        //Calling Latest String -
                        String currentTime = values[0];

                        //If time changes
                        if (!lastTime.equals(currentTime)) {
                            entrySet = new HashSet<String>();
                            Iterator<String> iterator = entrySet.iterator();
                            lastTime = currentTime;

                            //Debug Testing: See if entering time if statement correctly
                            //System.out.println("I'm aware my time is different");
                        }

                        //Store into HashSet if not null
                        if (!values[2].toLowerCase().contains("ff:ff:ff:ff:ff") && !values[2].contains("null")) {
                            //lastTime = currentTime;
                            //Start collecting all MAC addresses at this time
                            while (lastTime.equals(currentTime) && line != null) {
                                values = line.split(",");
                                insert = values[2];
                                while (insert.toLowerCase().contains("ff:ff:ff:ff:ff") || insert.contains("null")) {
                                    if (line != null) {
                                        line = br.readLine();
                                        if (line == null) {
                                            break;
                                        }
                                        values = line.split(",");
                                        insert = values[2];
                                    }
                                }
                                entrySet.add(insert);
                                entrySet.add(insert.replaceAll("\\s+", ""));
                                //System.out.println("MAC: " + values[2]);
                                currentTime = values[0];
                                if (line != null) {
                                    line = br.readLine();
                                }
                                //line = br.readLine();
                            }
                            if (line != null) {
                                line = br.readLine();
                            }
                        }
                        line = br.readLine();
                        if (!lastTime.equals(currentTime)) {
                            //entrySet = new HashSet<String>();
                            Iterator<String> iterator = entrySet.iterator();
                            //System.out.println("I'm aware my time is different");

                            //Dumping into Database
                            while (iterator.hasNext()) {
                                // System.out.println("Made to While Loop at " + currentTime);
                                String newMac = iterator.next();
                                String sql = "INSERT INTO DATA (ID, TIME, MAC) " + "VALUES (" + counter +", '" + lastTime + "', '" + newMac + "');";
                                stat.executeUpdate(sql);
                                counter++;
                            }
                        }
                    }

                    //Close and Shut down Reader
                    br.close();

                    System.out.println("File Deleted" + f);

                    //Deleting .csv after reading
                    f.setWritable(true);
                    f.delete();

                    //Debug Testing if file actually deleted
//                    boolean check = f.delete();
//
//                    if(check)
//                        System.out.println("Deleted");
//                    else
//                        System.out.println("Didn't delete");


                } catch (Exception e) {
                    System.out.print("FileParser: " + e.getMessage() + "\n");

                }
            }

            //If there are not enough CSV files to filter, wait 30 seconds
            else {
                try {
                    System.out.println("Not enough files -- pausing for 30 seconds \n");
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    System.out.println("FileParser2: " + e.getMessage());
                }
            }
        }
    }
}

