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
        ArrayList<Boolean> waitingForImage;

        Iterator<String> keyIterator;
        HashMap<String, String> overlapMap;
        String thisKey;

        String jsonString;

        System.out.print("started Server logic\n");

        while (true) {

            try {

                Thread.sleep(100);

                if (parent.ping) {
                    System.out.print("Parent ping!!!\n");

                    myTime = System.currentTimeMillis();

                    compareMap = new ArrayList<HashMap<String, String>>();
                    waitingForImage = new ArrayList<Boolean>();
                    clientSocketIterator = parent.getClientSockets().iterator();

                    while (clientSocketIterator.hasNext()) {
                        thisClient = (ClientConnection) clientSocketIterator.next();
                        clientTime = thisClient.timestamp;
                        if (clientTime > (myTime - 500)) {
                            compareMap.add(thisClient.myMacs);
                        }
                    }


//                    System.out.print("Comparemap has " + compareMap.size() + " elements\n");

                    keyIterator = compareMap.get(0).keySet().iterator();
                    overlapMap = new HashMap<String, String>();

                    if (compareMap != null && compareMap.size() > 0) {
                        while (keyIterator.hasNext()) {

                            thisKey = keyIterator.next();
//                            System.out.print("thiskey: " + thisKey + "\n");


                            for (int i = 0; i < compareMap.size(); i++) {
//                        for (int i = 1; i < compareMap.size(); i++) {

                                if (compareMap.get(i).containsKey(thisKey)) {
                                    overlapMap.put(thisKey, compareMap.get(i).get(thisKey));
                                }
                            }
                        }

//                        System.out.print("Overlapmap size: " + overlapMap.size() + "\n");
                        if (overlapMap.size() > 0) {

                            System.out.print("Sending openCV command\n");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("command", "take_picture");
                            jsonObject.put("timestamp", String.valueOf(myTime));
                            jsonString = jsonObject.toJSONString();

                            clientSocketIterator = parent.getClientSockets().iterator();
                            while (clientSocketIterator.hasNext()) {
                                thisClient = (ClientConnection) clientSocketIterator.next();
                                thisClient.xmitMessage(jsonString);
                            }
                        }
                    }

                    parent.resetPing();

                    System.out.print("Parent ping reset\n");
                }

            } catch (Exception e) {

                parent.resetPing();
            }
        }
    }
}