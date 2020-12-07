package comp3888.group5.sensordatacollector.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import comp3888.group5.sensordatacollector.AppDatabase;
import comp3888.group5.sensordatacollector.firebase.FUserDao;
import comp3888.group5.sensordatacollector.model.SensorRecord;

public class SensorRecordTimerTask extends TimerTask implements SensorEventListener {
    SensorRecord sensorRecord;
    Sensor sensor;
    SensorManager sensorManager;
    public SensorRecordTimerTask(Sensor sensor, SensorManager sensorManager){
        this.sensor = sensor;
        this.sensorManager = sensorManager;
    }

    @Override
    public void run() {
        try {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            sensorManager.unregisterListener(this);
            if(sensorRecord != null){
                Log.i("Record Timer", "New record for " + sensor.getName()+": " + sensorRecord.getSensorData());
                AppDatabase.getInstance(null)
                        .sensorRecordDao()
                        .insertRecord(sensorRecord);
                // TODO remove later
                //FUserDao.appendSensorData(FirebaseAuth.getInstance().getCurrentUser(), sensorRecord);
                sensorRecord = null;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(sensorRecord == null){
            sensorRecord = new SensorRecord();
            sensorRecord.setSensorType(event.sensor.getName());
            List<Double> sensorData = new ArrayList<>();
            for (int i = 0; i < event.values.length; i++) {
                sensorData.add((double)event.values[i]);
            }
            sensorRecord.setSensorData(sensorData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
