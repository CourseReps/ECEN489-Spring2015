package com.nagaraj.challenge5android;

import java.io.Serializable;

/**
 * Created by NAGARAJ on 2/15/2015.
 */
public class ClientInfoPacket implements Serializable {
    private String clientName;
    private Boolean mode;

     public void setClientName(String clientName){
         this.clientName=clientName;
     }
    public void setMode (Boolean mode){
        this.mode=mode;
    }
    public String getClientName(){
        return clientName;
    }
    public Boolean getMode(){
        return mode;
    }

}
