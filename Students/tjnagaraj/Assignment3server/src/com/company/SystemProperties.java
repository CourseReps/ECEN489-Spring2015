package com.company;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by NAGARAJ on 2/4/2015.
 */
public class SystemProperties implements Serializable {
        Date date = new Date();
        String javaClassPath=System.getProperty("java.class.path");
        String jreVendorName=System.getProperty("java.vendor");
        String osArchitecture= System.getProperty("os.arch");
        String osName= System.getProperty("os.name");
        String osVersion=System.getProperty("os.version");
        String userHomeDir= System.getProperty("user.home");
        String userAccountname=  System.getProperty("uer.name");

    public void printSystemProperties(){

        //System.out.println("System Properties:");
        System.out.println("Date: " + this.date.toString() );
        System.out.println("Java Class path: "+ this.javaClassPath);
        System.out.println("JRE vendor name: "+ this.jreVendorName);
        System.out.println("OS architecture: "+ this.osArchitecture);
        System.out.println("OS name: "+ this.osName);
        System.out.println("OS version: "+ this.osVersion );
        System.out.println("User home directory: "+ this.userHomeDir);
        System.out.println("User account name: "+ this.userAccountname);
    }
}
