import java.util.Properties;
import java.util.Date;

/**
 * Created by kwilk_000 on 1/29/2015.
 */


public class Main {
    public static void main(String[] args){

        Date date = new Date();
        System.out.println("It is currently " + date);
        System.out.println("Java Class Path = " + System.getProperty("java.classpath"));
        System.out.println("JRE Vendor Name = " + System.getProperty("java.vendor"));
        System.out.println("JRE Version Number = " + System.getProperty("java.version"));
        System.out.println("Operating System Architecture = " + System.getProperty("os.arch"));
        System.out.println("Operating System Name = " + System.getProperty("os.name"));
        System.out.println("Operating System version = " + System.getProperty("os.version"));
        System.out.println("User Home Directory = " + System.getProperty("user.dir"));
        System.out.println("User Account Name = " + System.getProperty("user.name"));
    }
}
