package com.slscomm;

import java.util.ArrayList;

/**
 * Created by Ian on 4/20/2015.
 */
public class Friend {
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
