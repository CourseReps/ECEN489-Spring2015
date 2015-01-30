/**
 * Created by RhoadsWylde on 1/29/2015.
 */
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class Main {

    public static void main(String[] args) {

        //The following will display the system date
        DateFormat Date = new SimpleDateFormat("EEEEEEEEEEEE, yyyy/MM/dd"); //This line specifies the date format
        Date dateobj = new Date();
        System.out.println("The date is: " + Date.format(dateobj));         //Prints out the date

        //The following will display the system time
        DateFormat  Time= new SimpleDateFormat("HH:mm:ss");                 //This line specifies the time format
        Date Timeobj = new Date();
        System.out.printf("The time is: " + Time.format(Timeobj));          //Prints out the time

        //The following uses System
        System.out.printf("\nYour Java Path is: " + System.getProperty("java.class.path")); //Prints out Java file paths
        System.out.printf("\nYour Java Vendor is: " + System.getProperty("java.vendor")); //Prints out Java vendor
        System.out.printf("\nYour Java Version is: " + System.getProperty("java.version")); //Prints out the version of Java
        System.out.printf("\nYour Operating System Name is: " + System.getProperty("os.name")); //Prints out OS Name
        System.out.printf("\nYour Operating System architecture is: " + System.getProperty("os.arch")); //Prints out OS Arch
        System.out.printf("\nYour Operating System Version is: " + System.getProperty("os.version")); //Prints out OS version
        System.out.printf("\nYour User Home Directory is: " + System.getProperty("user.home")); //Prints out the user's directory
        System.out.printf("\nYour User Name is: " + System.getProperty("user.name")); //Prints out user's name

    }
}