package com.slscomm.ws;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by Ian on 4/20/2015.
 */
public class UserWithFriends {
	
	final static Logger logger = Logger.getLogger(UserWithFriends.class);
	
    public ArrayList<String> friends = new ArrayList<String>();
    public String username;

    UserWithFriends(String n, ArrayList<String> fs) {
        friends = fs;
        username = n;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (String s : friends)
            arrayS += s + " ";

        return "Username: " + username + ", Friends: " + arrayS;
    }
}
