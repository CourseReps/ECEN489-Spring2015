/**
 * Created by Benito on 1/29/2015.
 */
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class Main {
    public static void main(String[] args){

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date date = new Date();
        System.out.println("This is the current date and time:" +dateFormat.format(date));

        System.out.println("This is your Java class path:" + System.getProperty("java.class.path","No value was found"));
        System.out.println("This is your Java vendor name:" + System.getProperty("java.vendor","No value was found"));
        System.out.println("This is your Java version number:" + System.getProperty("java.version","No value was found"));
        System.out.println("This is your Operating System Architechture:" + System.getProperty("os.arch","No value was found"));
        System.out.println("This is your Operating System Name:" + System.getProperty("os.name","No value was found"));
        System.out.println("This is your Operating System Version:" + System.getProperty("os.version","No value was found"));
        System.out.println("This is your User home directory:" + System.getProperty("user.home","No value was found"));
        System.out.println("This is your User account name:" + System.getProperty("user.name","No value was found"));

    }
}
