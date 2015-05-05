package com.slscomm;

import java.util.ArrayList;

/**
 * Created by Richa on 4/27/2015.
 */
public class LocWithCheckIns {
    public ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
    public String name;

    LocWithCheckIns(String n, ArrayList<CheckIn> cis) {
        checkIns = cis;
        name = n;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (CheckIn s : checkIns)
            arrayS += s.toString() + " ";

        return "Location name: " + name + ", CheckIns: (" + arrayS + ")";
    }
}
