package comp3888.group5.sensordatacollector;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import comp3888.group5.sensordatacollector.model.SensorRecord;
import comp3888.group5.sensordatacollector.util.SensorRecordTimerTask;


public class SensorListenService extends Service implements SensorEventListener {
    private int counter = 0;
    private AppDatabase appDatabase;
    private SensorManager sensorManager;
    private ThreadPoolExecutor threadPoolExecutor;
    private List<Timer> timers;

    public SensorListenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager)getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        Log.i("Sensor Service", ""+Build.VERSION.SDK_INT +"\t"+Build.VERSION_CODES.O);
        String NOTIFICATION_CHANNEL_ID = "comp3888.group5.sensordatacollector";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SensorService", "getting database");
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        timers = new ArrayList<>();
        // Getting sensor configuration from sharedPreferences
        Map<String, ?> sensorConfig = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_key) ,Context.MODE_PRIVATE)
                .getAll();
        if (sensorConfig != null && sensorConfig.size() > 0) {
                for(Map.Entry<String, ?> configEntry : sensorConfig.entrySet()){
                   try {
                       collectSensorInfo(configEntry);
                   } catch (NumberFormatException nfe){
                       Log.d("SensorListenService", "Invalid SensorConfig" + configEntry);
                   }
                }
//                startTimer();
                return START_STICKY;
        }
        Log.i("Stopself","Stopself");
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SensorListenService", "onDestroy");
        for (Timer timer : timers) {
            timer.cancel();
        }
    }

    private void collectSensorInfo(Map.Entry<String, ?> configEntry){
        // TODO: change sampling rate based on config
        final Sensor sensor = sensorManager.getDefaultSensor(Integer.parseInt(configEntry.getKey()));
        Integer freq = (Integer)configEntry.getValue();
        Timer timer = new Timer();
        timer.schedule(new SensorRecordTimerTask(sensor, sensorManager), 50, 60000/freq);
        timers.add(timer);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        final SensorRecord sensorRecord = new SensorRecord();
        sensorRecord.setSensorType(event.sensor.getName());
        //Log.i("Data Collect Service", event.sensor.getName()+": "+ event.values[0]);
        List<Double> sensorData = new ArrayList<>();
        for (int i = 0; i < event.values.length; i++) {
            sensorData.add((double)event.values[i]);
        }
        sensorRecord.setSensorData(sensorData);
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                appDatabase.sensorRecordDao().insertRecord(sensorRecord);
                //Log.i("sub thread", "sensorinfo get");
            }
        });
}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
