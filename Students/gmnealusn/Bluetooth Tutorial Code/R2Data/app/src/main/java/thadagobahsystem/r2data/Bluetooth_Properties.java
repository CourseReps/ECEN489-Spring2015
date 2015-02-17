package thadagobahsystem.r2data;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.HashSet;
import java.util.Set;


class WifiScanReceiver extends BroadcastReceiver {
    public void onReceive(Context c, Intent intent) {
    }
}

public class Bluetooth_Properties extends ActionBarActivity {
    Set setA = new HashSet();
    String state, mode =  null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth__properties);


        BluetoothManager BT_Manager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);//gets the Bluetooth service using the BluetoothManager
        BluetoothAdapter BT_Adapter = BT_Manager.getAdapter();  //gets the device's local Bluetooth adapter
            /*This will get some of the Bluetooth parameters*/
        String adapterName = BT_Adapter.getName();
        boolean adapterEnabled = BT_Adapter.isEnabled();
        String adapterAddress = BT_Adapter.getAddress();
        int profile = BT_Adapter.getState();
        int scanMode = BT_Adapter.getScanMode();
        setA = BT_Adapter.getBondedDevices();

        //if statements for name of the state
        if(profile==0){state = "Disconnected";}
        else if (profile==1){state = "Connecting";}
        else if(profile==2){state = "Connected";}
        else if(profile==3){state = "Disconnecting";}
        else if(profile==10){state = "Off";}
        else if(profile==11){state = "Turning On";}
        else if(profile==12){state = "On";}
        else if (profile==13){state = "Turning Off";}
        //if statements for scan modes
        if(scanMode==20){mode = "None";}
        else if (scanMode==21){mode = "Connectable";}
        else if(scanMode==23){mode = "Discoverable and Connectable";}

//Display the device information
        TextView textView = new TextView(this);
        textView.setTextSize(12);
        textView.setText("Bluetooth Adapter Name: " + adapterName +
                "\nBluetooth Adapter Enabled? " + adapterEnabled + "\nBluetooth Adapter Address: "
                + adapterAddress+ "\nBluetooth Adapter State: " +state+ "\nBluetooth Adapter Mode: "
                +mode+ "\nDevices Bonded to this Device: " +setA);

        setContentView(textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth__properties, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

