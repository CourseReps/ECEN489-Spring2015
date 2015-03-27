package com.anudeep;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main {

    public static void main(String[] args) {

        try {
            String serverAddress = "10.202.126.70";

            // Make connection and initialize streams
            Socket socket = new Socket(serverAddress, 9000);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            JSONObject obj = new JSONObject();
            obj.put("command","connect");
            obj.put("deviceName", "FaceRec3");

            out.println(obj.toString());
            out.flush();

            String line = in.readLine();

            JSONObject readObj = (JSONObject) JSONValue.parse(line);
            //String command = readObj.get("command").toString();
            String timeStamp = readObj.get("timestamp").toString();

            System.out.println(timeStamp);

            String[] cmd = {"/Users/tungalanagaraghavendraanudeep/Library/Developer/Xcode/DerivedData/Opencv-ensheosmysofxrdkkoapzvfqbaet/Build/Products/Debug/opencv",timeStamp};
            Runtime.getRuntime().exec(cmd);
            TimeUnit.SECONDS.sleep(15);
            int numfiles = new File("/Users/tungalanagaraghavendraanudeep/Documents/faceRecClient").listFiles().length;
            if(numfiles==1) {
                obj.clear();
                obj = new JSONObject();
                obj.put("command","filename");
                obj.put("filename","FaceRec3_1_"+timeStamp+".png");
                trasferFile("FaceRec3_1_" + timeStamp + ".png");
                out.println(obj.toString());
                out.flush();
                new File("/Users/tungalanagaraghavendraanudeep/Documents/faceRecClient/FaceRec3_1_"+timeStamp+".png").delete();
            }
            else {
                obj.clear();
                obj = new JSONObject();
                obj.put("filename","NULL");
                out.println(obj.toString());
                out.flush();
                FileUtils.cleanDirectory(new File("/Users/tungalanagaraghavendraanudeep/Documents/faceRecClient"));
            }

            socket.close();




        }
        catch(Exception e) {
            e.printStackTrace();
        }






    }

    public static void trasferFile(String filename) {

        String server = "10.202.126.70";
        int port = 1234;
        String user = "test";
        String pass = "test";

        FTPClient ftpClient = new FTPClient();

        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


            File firstLocalFile = new File("/Users/tungalanagaraghavendraanudeep/Documents/faceRecClient/"+filename);

            String firstRemoteFile = filename;
            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }



        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
