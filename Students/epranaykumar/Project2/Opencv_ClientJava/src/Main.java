
import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.net.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main {

    public static void main(String[] args) throws IOException
    {
        // write your code here
        // ProcessBuilder proc = new ProcessBuilder("<your_exe>", "exe_args");
        // proc.start();
        try{
            Scanner cmd_input = new Scanner(System.in);

            //System.out.println("Please enter the IP address of Server.");
            //String ip = cmd_input.next();
            String ip = "10.202.115.90";
            int portNumber = 9000;
            Socket clientSocket = new Socket(ip, portNumber);
            System.out.println("connected to Server.");

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter wr_to_server = new PrintWriter(clientSocket.getOutputStream(), true);

            String server = "10.202.115.246";
            int port = 9019;
            String user = "test";
            String pass = "test";


            String deviceid = "4";

            System.out.println("send the connected command to JSON object");
            JSONObject sendDeviceProp = new JSONObject();
            sendDeviceProp.put("command", "connect");
            sendDeviceProp.put("deviceName", deviceid); //new Integer(4)

            wr_to_server.println(sendDeviceProp.toString());
            wr_to_server.flush();




            while(true) {

                String line = input.readLine();
                JSONObject obj = (JSONObject) JSONValue.parse(line);

                String timestamp = obj.get("timestamp").toString();
                System.out.println(" received timestamp : " + timestamp);


                FTPClient ftpClient = new FTPClient();
                try {

                    String directory = "P:/Masters Courses/Mobile Sensing/course_repository/Project2/Project2_FaceDetection/FaceDetection/croppedimages/";
                    File folder = new File(directory);

                    File[] listOfFiles = folder.listFiles();
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            boolean success =  (new File(directory+file.getName())).delete();
                            if (success) {
                                System.out.println("deleted file :"+ file.getName());
                            }
                        }
                    }



                    Runtime r = Runtime.getRuntime();
                    Process p2 = r.exec("P:\\Masters Courses\\Mobile Sensing\\course_repository\\Project2\\Project2_FaceDetection\\FaceDetection\\Debug\\FaceDetection.exe"); //absolute or relative path


                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();

                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);



                    int count=0;
                    while(count==0){
                        count = folder.list().length;
                    }

                    if (count == 1) {
                        File cropped_image = new File(directory + "th0.jpg");

                        String server_image = deviceid+"_"+timestamp+".jpg";
                        InputStream inputStream = new FileInputStream(cropped_image);

                        System.out.println("Start uploading first file");
                        boolean done = ftpClient.storeFile(server_image, inputStream);
                        inputStream.close();
                        if (done) {
                            System.out.println("The first file is uploaded successfully.");
                        }



                        JSONObject sendImageName = new JSONObject();

                        sendImageName.put("command", "filename");
                        sendImageName.put("filename", deviceid + "_" + timestamp); //new Integer(4)

                        wr_to_server.println(sendImageName.toString());
                        wr_to_server.flush();

                    } else {

                        JSONObject sendImageName = new JSONObject();
                        sendImageName.put("command", "filename");
                        sendImageName.put("filename", "NULL"); //new Integer(4)

                        wr_to_server.println(sendImageName.toString());
                        wr_to_server.flush();
                    }

                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }

                    //int exitVal = p2.exitValue();

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
                /*finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                        clientSocket.close();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }*/
            }
        }

        catch (Exception e) {

            System.out.println("Error handling : " + e);
        }

    }
}


/*

// to count files in a folder

File folder = new File("/Users/you/folder/");

int count = folder.list().length //list is better than the listFiles


 */



/*

// to read filenames from a folder

File folder = new File("/Users/you/folder/");
File[] listOfFiles = folder.listFiles();

for (File file : listOfFiles) {
        if (file.isFile()) {
        System.out.println(file.getName());
        }
        }

 */

/*

// to delete filename from a folder

  boolean success = (new File("filename")).delete();

         if (success) {
            System.out.println("The file has
            been successfully deleted");
         }

                   //  or  //

  File file = new File("c:\\logfile20100131.log");

    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}


 */