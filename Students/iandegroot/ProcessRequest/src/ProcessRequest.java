/**
 * Created by Ian on 4/15/2015.
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class ProcessRequest implements Runnable {

    String request;

    ProcessRequest(String rqst) {
        request = rqst;
    }

    //Takes a string in JSON format and outputs a JSON object
    @SuppressWarnings("unchecked")
    static JSONObject stringToJSON(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = new JSONObject();

        try {
            json = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void run() {
        JSONObject jsonObject = new JSONObject();

        jsonObject = stringToJSON(request);

        System.out.println("Received timestamp: " + jsonObject.get("timestamp").toString());

        clientResponse = jsonObject;
    }

}
