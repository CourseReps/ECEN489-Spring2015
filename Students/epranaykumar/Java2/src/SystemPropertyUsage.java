import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class SystemPropertyUsage {

    public static void main(String[] args) {
	// write your code here

        //getting current date and time using Date class
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date currentDate = new Date();
        System.out.println(df.format(currentDate));

        System.out.println("java class path : "+System.getProperty ("java.class.path"));
        System.out.println("JRE Version : "+System.getProperty ("java.version"));
        System.out.println("JRE Vendor : "+System.getProperty ("java.vendor"));
        System.out.println("Operating System Architecture : "+System.getProperty ("os.arch"));
        System.out.println(" Operating System Name: "+System.getProperty ("os.name"));
        System.out.println(" Operating System Version: "+System.getProperty ("os.version"));
        System.out.println(" User Home Directory: "+System.getProperty ("user.home"));
        System.out.println(" User Account Name: "+System.getProperty ("user.name"));


    }
}


/*Properties p = System.getProperties();
   Enumeration keys = p.keys();
   while (keys.hasMoreElements()) {
        String key = (String)keys.nextElement();
        String value = (String)p.get(key);
        System.out.println(key + ": " + value);
        }

        */