package com.company;
import java.util.*;
import java.text.*;
public class Main {

    public static void main(String[] args) {
	    Date dateAndTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        System.out.println("Current Date: " + ft.format(dateAndTime));
        //Properties p = new Properties(System.getProperties());
        //System.setProperties(p);
        System.getProperties().list(System.out);
    }
}
