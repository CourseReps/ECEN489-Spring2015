import java.util.Vector;

/**
 * Created by Ian on 2/15/2015.
 */
public abstract class SensorData {

    private String name;
    private int version;
    private boolean wakeUpSensor;
    private int sensorType;
    private String vendor;

    abstract Vector<String> getData();

    String getName() {
        return name;
    }

    int getVersion() {
        return version;
    }

    boolean getWakeUpSensor() {
        return wakeUpSensor;
    }

    int getSensorType() {
        return sensorType;
    }

    String getVendor() {
        return vendor;
    }
}
