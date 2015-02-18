import java.util.ArrayList;
import java.util.Collections;

public interface SensorInterface {
	
	//These are included in SensorListener though..//Using as an example.
	//public abstract void onAccuracyChanged(int sensor, int accuracy); //Called when accuracy of sensor changed
	//public abstract void onSensorChanged(int sensor, float[] values); //Called when sensor values change
	
	public Collection SensorData = new ArrayList();
	public abstract void SensorName();
	public abstract void SensorEnabled();
	public abstract void SensorValues();
	
}

/*This design was created in this manner so that it's simple and universal to different sensors.
The abstract methods are placed inside the interface named SensorInterface. Then the
methods in the interface can be used in ways that the user pleases.
They can be modified to work with the data of different types sensors. */
