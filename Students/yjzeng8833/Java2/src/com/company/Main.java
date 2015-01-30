package com.company;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    public static void main(String[] args)
            throws Exception {
                Date date=new Date();
                System.out.println(date); //Date should print both Date & Time
                System.out.println("Class Path: " + System.getProperty("java.class.path")); //Find Class Path
                System.out.println("JRE Vendor Name: " + System.getProperty("java.vendor")); //Find JRE Vendor Name
                System.out.println("JRE Version: " + System.getProperty("java.version")); //Find JRE Version
                System.out.println("OS Architecture: " + System.getProperty("os.arch")); //Find OS Architecture
                System.out.println("OS Name: " + System.getProperty("os.name")); //Find OS Name
                System.out.println("OS Version: " + System.getProperty("os.version")); //Find OS Version
                System.out.println("User Home Directory: " + System.getProperty("user.home")); //Find User Home Directory
                System.out.println("User Name: " + System.getProperty("user.name")); //Find User Name

            }
    }

