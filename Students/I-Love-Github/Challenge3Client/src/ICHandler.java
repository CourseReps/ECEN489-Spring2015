import java.awt.*;

public class ICHandler implements Runnable {

    ClientRunnable parent;

    ICHandler(ClientRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        String position;
        Point point;
        long currentTime;

        while(true) {

            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
            }

            point = MouseInfo.getPointerInfo().getLocation();
            position = String.valueOf(point.getX()) + "x" + String.valueOf(point.getY());
            parent.updateUI(position);

            currentTime = System.currentTimeMillis();
            parent.getDB().commitData(currentTime, "Mouse X", String.valueOf(point.getX()));
            parent.getDB().commitData(currentTime, "Mouse Y", String.valueOf(point.getY()));

//            parent.getDB().getID();
            parent.setmyTS(currentTime);
        }
    }
}
