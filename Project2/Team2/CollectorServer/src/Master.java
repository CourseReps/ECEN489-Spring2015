/**
 * Created by joshuacano on 3/26/15.
 */
public class Master {

    // Create Threads
    Thread serverThread;
    Thread openCvThread;
    Thread sensorThread;

    // Create Client objects
    CommSvr server;
    CommSensor sensor;
    CommOCV facialRecog;

    Master() {

        server = new CommSvr(this);
        serverThread = new Thread(server);
        serverThread.start();

        sensor = new CommSensor(this);
        sensorThread = new Thread(sensor);
        sensorThread.start();

        facialRecog = new CommOCV(this);
        openCvThread = new Thread(facialRecog);
        openCvThread.start();

    }

    public CommSvr getCommToServer() {
        return server;
    }
    public CommSensor getCommToSensor() {
        return sensor;
    }
    public CommOCV getCommToFacialRecognition() {
        return facialRecog;
    }


}