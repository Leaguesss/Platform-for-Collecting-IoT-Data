package comp3888.group5.sensordatacollector.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


public class ServiceUtils {
    public static void serviceRestart(Context context,  Class<?> service){
        context.stopService(new Intent(context, service));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context.getApplicationContext(), service));
        } else {
            context.startService(new Intent(context, service));
        }
    }
}
