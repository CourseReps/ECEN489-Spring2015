package com.company;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
	// Code to print Date and Time
        System.out.println("Date and Time:");
        Date date = new Date();
        System.out.println(date.toString());
     // Printing System Properties
        System.out.println("System Properties:");
        System.out.println("Java Class path: "+ System.getProperty("java.class.path"));
        System.out.println("JRE vendor name: "+ System.getProperty("java.vendor"));
        System.out.println("OS architecture: "+ System.getProperty("os.arch"));
        System.out.println("OS name: "+ System.getProperty("os.name"));
        System.out.println("OS version: "+ System.getProperty("os.version"));
        System.out.println("User home directory: "+ System.getProperty("user.home"));
        System.out.println("User account name: "+ System.getProperty("uer.name"));
    }
}
