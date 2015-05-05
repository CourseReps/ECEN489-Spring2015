package com.slscomm.ws;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by Ian on 4/20/2015.
 */
public class UserWithLocations {
	
	final static Logger logger = Logger.getLogger(UserWithLocations.class);
	
    public ArrayList<String> locations = new ArrayList<String>();
    public String username;

    UserWithLocations(String n, ArrayList<String> ls) {
        locations = ls;
        username = n;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (String s : locations)
            arrayS += s + " ";

        return "Username: " + username + ", Locations: " + arrayS;
    }
}
