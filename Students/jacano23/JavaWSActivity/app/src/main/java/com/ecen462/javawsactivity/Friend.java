package com.ecen462.javawsactivity;

import java.util.ArrayList;

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