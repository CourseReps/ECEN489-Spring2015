package com.slscomm.ws;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by Ian on 4/20/2015.
 */
public class Location {
	
	final static Logger logger = Logger.getLogger(Location.class);
	
    public ArrayList<String> friends = new ArrayList<String>();
    public String name;

    Location(String n, ArrayList<String> fs) {
        friends = fs;
        name = n;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (String s : friends)
            arrayS += s + " ";

        return "Location name: " + name + ", Friends: " + arrayS;
    }
}
