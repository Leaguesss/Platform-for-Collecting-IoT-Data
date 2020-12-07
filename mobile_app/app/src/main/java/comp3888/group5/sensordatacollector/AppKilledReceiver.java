package comp3888.group5.sensordatacollector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import comp3888.group5.sensordatacollector.util.ServiceUtils;

public class AppKilledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("Broadcast Listened", "Service tried to stop");
        ServiceUtils.serviceRestart(context, SensorListenService.class);
    }
}
