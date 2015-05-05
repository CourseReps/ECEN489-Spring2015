package com.ecen489.slidermenu;

import java.util.ArrayList;

/**
 * Created by Ian on 4/20/2015.
 */
public class UserWithLocations {
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
