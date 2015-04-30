import java.util.ArrayList;

/**
 * Created by Ian on 4/27/2015.
 */
public class UserWithCheckIns {
    public ArrayList<CheckInData> checkIns = new ArrayList<CheckInData>();
    public String username;

    UserWithCheckIns(String n, ArrayList<CheckInData> cis) {
        checkIns = cis;
        username = n;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (CheckInData s : checkIns)
            arrayS += s.toString() + " ";

        return "Username: " + username + ", CheckIns: " + arrayS;
    }
}
