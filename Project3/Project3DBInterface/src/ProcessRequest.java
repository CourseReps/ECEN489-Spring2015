/**
 * Created by Ian on 4/15/2015.
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class ProcessRequest {

    String process(String request) {
        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonResponse = new JSONObject();

        jsonRequest = JSONWrapping.stringToJSON(request);

        jsonResponse = JSONWrapping.serverUnwrapping(jsonRequest);

        //System.out.println("Received timestamp: " + jsonObject.get("timestamp").toString());

        return jsonResponse.toString();
    }

}
