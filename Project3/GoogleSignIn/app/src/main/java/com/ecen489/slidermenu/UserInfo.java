package com.ecen489.slidermenu;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Joshua on 4/29/2015.
 */
public class UserInfo implements Serializable {
    private static String name = null;
    private static String userName =null;
    private static String password =null;
    private static String sessionId =null;
    private static ArrayList<String> friendsList = null;
    private static String imageUrl = null;
    UserInfo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFriendsList(){
        return friendsList;
    }

    public void setFriendsList(ArrayList<String> friendsList){
        this.friendsList = friendsList;
    }

    public String getImageUrl(){
        return imageUrl;
    }
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
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
