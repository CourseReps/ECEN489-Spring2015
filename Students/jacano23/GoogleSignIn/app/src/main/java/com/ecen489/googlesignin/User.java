package com.ecen489.googlesignin;

import java.io.*;

/**
 * Created by Joshua on 4/29/2015.
 */
public class User {
    private String userName;
    private String password;
    private String sessionId;
    User(){
            userName = "";
            password = "";
            sessionId = "";
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String email ) {
        userName = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String hashPassword) {
        password = hashPassword;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String mySession) {
        sessionId = mySession;
    }

}
