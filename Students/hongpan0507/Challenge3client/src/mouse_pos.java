/**
 * Created by hpan on 2/1/15.
 */
import java.awt.*;
import java.io.Serializable;

public class mouse_pos implements Serializable {
    public int x_loc = 0;
    public int y_loc = 0;
    transient PointerInfo mouse_ptr = MouseInfo.getPointerInfo();
    transient Point mouse_loc = mouse_ptr.getLocation();

    //constructor
    public mouse_pos () {

    }

    public void get_loc () {
        x_loc = (int) mouse_loc.getX();
        y_loc = (int) mouse_loc.getY();
    }
}
