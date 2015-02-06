package com.company;
import java.io.PrintWriter;
import java.util.*;
import java.text.*;

import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        String serverAddress ="192.168.1.104";
        //String serverAddress ="0.0.0.0"; // put address here
        int port = 4445;
        Date dateAndTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        try{
            Socket connection = new Socket( serverAddress, port);
            PrintWriter printWriter = new PrintWriter(connection.getOutputStream(),true);
            printWriter.println("Current Date: " + ft.format(dateAndTime));
            printWriter.println("Java Class Path: " + System.getProperty("java.class.path"));
            printWriter.println("JRE Vendor Name: " + System.getProperty("java.vendor"));
            printWriter.println("JRE Version Number: "+ System.getProperty("java.version"));
            printWriter.println("Operating System Architecture: " + System.getProperty("os.arch"));
            printWriter.println("Operating System Name: " + System.getProperty("os.name"));
            printWriter.println("Operating System Version: " + System.getProperty("os.version"));
            printWriter.println("User Home Directory: " + System.getProperty("user.home"));
            printWriter.println("User Account Name: " + System.getProperty("user.name"));
            connection.close();
        }catch(Exception e){
            System.out.println("exception " + e);
            System.exit(1);

        }
    }
}
