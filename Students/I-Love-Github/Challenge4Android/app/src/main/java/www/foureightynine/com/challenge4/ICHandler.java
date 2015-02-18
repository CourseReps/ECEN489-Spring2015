package www.foureightynine.com.challenge4;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;

public class ICHandler implements Runnable {

    ClientRunnable parent;

    ICHandler(ClientRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        String position;
        long currentTime;

        while(true) {

            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
            }

            Context context = parent.getActivity().getApplicationContext();
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            int linkSpeed = wifiManager.getConnectionInfo().getRssi();

//            TelephonyManager telephonyManager = (TelephonyManager)parent.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//            CellInfoLte cellinfolte = (CellInfoLte)telephonyManager.getAllCellInfo().get(0);
//            CellSignalStrengthLte cellSignalStrengthLte = cellinfolte.getCellSignalStrength();
//            int cellStrength = cellSignalStrengthLte.getDbm();

            currentTime = System.currentTimeMillis();
            parent.getDB().commitData(currentTime, "Wifi Speed", String.valueOf(linkSpeed));
            parent.newMessage("Wifi data point collected: " + String.valueOf(linkSpeed));
//            parent.getDB().commitData(currentTime, "Mobile Signal", String.valueOf(cellStrength));

            parent.setmyTS(currentTime);
        }
    }
}
