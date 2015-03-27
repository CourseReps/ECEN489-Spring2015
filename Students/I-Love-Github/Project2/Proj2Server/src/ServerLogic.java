import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerLogic implements Runnable {

    ServerRunnable parent;

    ServerLogic(ServerRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        long myTime;
        long clientTime;
        ClientConnection thisClient;
        Iterator clientSocketIterator;
        ArrayList<HashMap<String, String>> compareMap;

        Iterator<String> keyIterator;
        HashMap<String, String> overlapMap;
        String thisKey;

        String jsonString;

        try {

            Thread.sleep(100);

            if (parent.ping) {

                myTime = System.currentTimeMillis();

                compareMap = new ArrayList<HashMap<String, String>>();
                clientSocketIterator = parent.getClientSockets().iterator();

                while (clientSocketIterator.hasNext()) {
                    thisClient = (ClientConnection) clientSocketIterator.next();
                    clientTime = thisClient.timestamp;
                    if (clientTime > (myTime - 500)) {
                        compareMap.add(thisClient.myMacs);
                    }
                }

                keyIterator = compareMap.get(0).keySet().iterator();
                overlapMap = new HashMap<String, String>();

                while (keyIterator.hasNext()) {

                    thisKey = keyIterator.next();

                    for (int i = 1; i < compareMap.size(); i++) {

                        if (compareMap.get(i).containsKey(thisKey)) {
                            overlapMap.put(thisKey, compareMap.get(i).get(thisKey));
                        }
                    }
                }

                if (overlapMap.size() > 0) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("command", "take_picture");
                    jsonObject.put("data", String.valueOf(myTime));
                    jsonString = jsonObject.toJSONString();

                    clientSocketIterator = parent.getClientSockets().iterator();
                    while (clientSocketIterator.hasNext()) {
                        thisClient = (ClientConnection) clientSocketIterator.next();
                        thisClient.xmitMessage(jsonString);
                    }

                }

                parent.resetPing();
            }

        } catch (Exception e) {

        }
    }
}
