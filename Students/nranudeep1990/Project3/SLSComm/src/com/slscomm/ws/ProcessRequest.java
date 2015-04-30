package com.slscomm.ws;
/**
 
* Created by Ian on 4/15/2015.
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class ProcessRequest {

    String process(String request) {
        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonResonse = new JSONObject();

        jsonRequest = JSONWrapping.stringToJSON(request);

        jsonResonse = JSONWrapping.serverUnwrapping(jsonRequest);

        //System.out.println("Received timestamp: " + jsonObject.get("timestamp").toString());

        return jsonResonse.toString();
    }

}
