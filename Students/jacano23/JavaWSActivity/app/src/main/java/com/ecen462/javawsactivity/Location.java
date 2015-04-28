package com.ecen462.javawsactivity;

import java.util.ArrayList;

public class Location {
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