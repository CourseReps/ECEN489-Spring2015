import java.util.Collections;
import java.util.ArrayList;

public interface Sensors { //interfaces and methods implicitly abstract. No need to declare as abstract
	public Collections output = new ArrayList();
	public void sensorType();
	public void powerUse();
	public void value();
	public void activate();
}

/* 
I've implemented it this way taking in the most generic parts of sensors. A 
sensor needs to turn on, know if it is running continuously or single,
what type of sensor it is, report the output itself, and activate itself.

To add more sensor specific tasks, this can be done using implements like,
	public class Accelerometer implements Sensors{}

Since it is an interface, all methods are public and abstract.
*/