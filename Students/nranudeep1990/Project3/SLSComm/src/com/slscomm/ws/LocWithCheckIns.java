package com.slscomm.ws;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by Richa on 4/27/2015.
 */
public class LocWithCheckIns {
	
	final static Logger logger = Logger.getLogger(LocWithCheckIns.class);
	
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
