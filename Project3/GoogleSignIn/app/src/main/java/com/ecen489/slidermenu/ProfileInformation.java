package com.ecen489.slidermenu;

import java.io.Serializable;

/**
 * Created by Joshua on 5/3/2015.
 */
public class ProfileInformation implements Serializable {
    private String name;
    private String email;
    private String imageUrl;

    public String getName() {
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getImageUrl(){
        return imageUrl;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
