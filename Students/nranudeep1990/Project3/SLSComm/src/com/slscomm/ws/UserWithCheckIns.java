package com.slscomm.ws;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by Ian on 4/27/2015.
 */
public class UserWithCheckIns {
	
	final static Logger logger = Logger.getLogger(UserWithCheckIns.class);
	
    public ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
    public String username;

    UserWithCheckIns(String n, ArrayList<CheckIn> cis) {
        checkIns = cis;
        username = n;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (CheckIn s : checkIns)
            arrayS += s.toString() + " ";

        return "Username: " + username + ", CheckIns: (" + arrayS + ")";
    }
}
