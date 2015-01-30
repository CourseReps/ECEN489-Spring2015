package com.company;
/* Program that displays information about the computer
it is executed on.
 */
import java.util.Calendar;
//import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) {
	//System.getProperties().list(System.out); //Retrieves ALL the information, not using

        /*Prints the date and time*/
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy HH:mm:ss \n"); //Format for date to be displayed
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime())); //Prints Date and Time

        /*Prints out system information*/
        System.out.println("Your User Home Directory is:\t" + System.getProperty("user.home")); //Prints User Home Directory
        System.out.println("Your User Account Name is:\t" + System.getProperty("user.name")); //Prints User's Name
        System.out.println("Your Operating System Name is:\t" + System.getProperty("os.name")); //Prints OS name
        System.out.println("Your Operating System Version is:\t" + System.getProperty("os.version"));//Prints OS version
        System.out.println("Your Operating System Architecture is:\t" + System.getProperty("os.arch")); //Prints OS arch
        System.out.println("Your Java Class Path is:\t" + System.getProperty("java.class.path")); //Prints Java path
        System.out.println("Your JRE Vendor name is:\t" + System.getProperty("java.vendor")); //Prints JRE Vendor name
        System.out.println("Your JRE Version number is:\t" + System.getProperty("java.version")); //Prints JRE version
    }
}
