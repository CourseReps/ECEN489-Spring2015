import java.util.ArrayList;

/**
 * Created by Ian on 2/15/2015.
 */
public interface SensorCollection {

    ArrayList<SensorData> data = new ArrayList<SensorData>();

    void loadDataToDB(ArrayList<SensorData> data);

}
