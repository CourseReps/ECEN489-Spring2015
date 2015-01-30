package com.ECEN489;
import java.util.Date;

/*
    Create a simple Java application that displays information about the computer it is executed on.
    In particular, print date and time. Use System.getProperty to display the following attributes:
    Java class path, JRE vendor name, JRE version number, Operating system architecture,
    Operating system name, Operating system version, User home directory, and User account name.
    Some of the attribute may return null.
*/

public class Main {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println("Date and Time: " + date.toString());

	    System.out.println("Java Class Path: " + System.getProperty("java.class.path"));
        System.out.println("JRE Vendor Name: " + System.getProperty("java.vendor"));
        System.out.println("JRE Version Number: "+ System.getProperty("java.version"));
        System.out.println("Operating System Architecture: " + System.getProperty("os.arch"));
        System.out.println("Operating System Name: " + System.getProperty("os.name"));
        System.out.println("Operating System Version: " + System.getProperty("os.version"));
        System.out.println("User Home Directory: " + System.getProperty("user.home"));
        System.out.println("User Account Name: " + System.getProperty("user.name"));
    }
}
