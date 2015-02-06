package com.anudeep;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tungala on 2/6/2015.
 */
public class ClientInfo implements Serializable {

    private Date clientDate;
    private String javaClassPath;
    private String jreVendor;
    private String jreVersion;
    private String osArch;
    private String osName;
    private String osVersion;
    private String userHome;
    private String userName;

    public Date getClientDate() {
        return clientDate;
    }

    public void setClientDate(Date clientDate) {
        this.clientDate = clientDate;
    }

    public String getJavaClassPath() {
        return javaClassPath;
    }

    public void setJavaClassPath(String javaClassPath) {
        this.javaClassPath = javaClassPath;
    }

    public String getJreVendor() {
        return jreVendor;
    }

    public void setJreVendor(String jreVendor) {
        this.jreVendor = jreVendor;
    }

    public String getJreVersion() {
        return jreVersion;
    }

    public void setJreVersion(String jreVersion) {
        this.jreVersion = jreVersion;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getUserHome() {
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
