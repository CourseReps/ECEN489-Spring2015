/**
 * Created by Ian on 4/20/2015.
 */
public class CheckInData {
    String method, location, username;
    int time;

    CheckInData(String meth, String loc, String name, int t) {
        method = meth;
        location = loc;
        username = name;
        time = t;
    }

    @Override
    public String toString() {
        return "Method: " + method + ", Location: " + location + ", Time: " + time + ", Username: " + username;
    }
}
