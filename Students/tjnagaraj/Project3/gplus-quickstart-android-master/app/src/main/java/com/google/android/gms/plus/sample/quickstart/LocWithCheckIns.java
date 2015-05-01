package com.google.android.gms.plus.sample.quickstart;

import java.util.ArrayList;

/**
 * Created by Richa on 4/27/2015.
 */
public class LocWithCheckIns {
    public ArrayList<CheckInData> checkIns = new ArrayList<CheckInData>();
    public String name;

    LocWithCheckIns(String n, ArrayList<CheckInData> cis) {
        checkIns = cis;
        name = n;
    }

    @Override
    public String toString() {
        String arrayS = "";

        for (CheckInData s : checkIns)
            arrayS += s.toString() + " ";

        return "Location name: " + name + ", CheckIns: " + arrayS;
    }
}
