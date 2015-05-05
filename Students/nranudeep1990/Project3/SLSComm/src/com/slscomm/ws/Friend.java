package com.slscomm.ws;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by Ian on 4/20/2015.
 */
public class Friend {
	
	final static Logger logger = Logger.getLogger(Friend.class);
	
    public ArrayList<String> locations = new ArrayList<String>();
    public String username;

    Friend(String name, ArrayList<String> locs) {
        locations = locs;
        username = name;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (String s : locations)
            arrayS += s + " ";

        return "Username: " + username + ", Locations: " + arrayS;
    }
}
