package comp3888.group5.sensordatacollector.ui.dashboard;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.os.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import comp3888.group5.sensordatacollector.AppDatabase;
import comp3888.group5.sensordatacollector.R;
import comp3888.group5.sensordatacollector.ResearchInfoActivity;
import comp3888.group5.sensordatacollector.firebase.FUserDao;
import comp3888.group5.sensordatacollector.model.SensorRecord;
import comp3888.group5.sensordatacollector.ui.widgets.BasicDialog;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    View root;
    private TextView sensor_info;
    private TextView sensor_reward_unit;
    private TextView total_record;
    private ProgressBar progressBar;
    private Button upload_btn;
    private Handler mHandler = new Handler();
    private TextView study_information;
//    private LinearLayout study_information;
//    private int rustLevel = 10;    /* battery level */

    private int count;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
        upload_btn = (Button) root.findViewById(R.id.upload_btn);


        sensor_info = (TextView) root.findViewById(R.id.sensor);
        sensor_reward_unit = (TextView) root.findViewById(R.id.sensor_reward_unit);
        total_record = (TextView) root.findViewById(R.id.total_record_tv);

        root.findViewById(R.id.study).setOnClickListener(this);
        root.findViewById(R.id.active_sensor).setOnClickListener(this);
        sensor_info.setOnClickListener(this);
        root.findViewById(R.id.reward_block).setOnClickListener(this);
        sensor_reward_unit.setOnClickListener(this);
        root.findViewById(R.id.upload_btn).setOnClickListener(this);

        getSensor(sensor_info, sensor_reward_unit);

        study_information = (TextView) root.findViewById(R.id.study_information);


        // TODO DELETE later, this is for experiment only
        Intent resultIntent = new Intent(getContext(), ResearchInfoActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notif=
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sensor Data Collection";
            String description = "Channel for notify new research opportunities for Sensor Data Collection";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("8080", name, importance);
            channel.setDescription(description);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notif.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "8080");
        }else{
            builder = new NotificationCompat.Builder(getActivity().getApplicationContext());
        }

        Notification notify = builder
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Mobile Data Collector")
                .setContentText("New Research opportunity")
                .setSmallIcon(R.drawable.icon)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        //notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
        readCountFromDb();
        return root;
    }

    private void readCountFromDb(){
        try {
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    count = AppDatabase.getInstance(getContext()).sensorRecordDao().getCount();
                    Log.i("Dashboard Fragment", "async task performed");
                    total_record.setText("Number of Record in local database: "+count);
                    return null;
                }
            }.execute().get();

        }
        catch (Exception e){
            Log.i("Dashboard fragment", e.getMessage());
        }
    }

    public void getSensor(TextView sensor_info, TextView sensor_reward_unit){
        Map<String, ?> sensorConfig = getActivity().getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_key) ,Context.MODE_PRIVATE)
                .getAll();

        StringBuilder mapAsString = new StringBuilder();
        StringBuilder rewardAsString = new StringBuilder();
        getActivity();
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        for (String key : sensorConfig.keySet()) {
            final int sensorType = Integer.parseInt(key);
            final Integer freq = (Integer) sensorConfig.get(key);
            if (freq == null) break;
            if(freq != -1){
                mapAsString.append(sensorManager.getDefaultSensor(sensorType).getName())
                        .append(" has ");
                rewardAsString.append(sensorManager.getDefaultSensor(sensorType).getName())
                        .append(" gets ");
                switch (freq){
                    case 1:
                        mapAsString.append("Low upload frequency.\n\n");
                        rewardAsString.append("1 point per hour.\n\n");
                        break;
                    case 10:
                        mapAsString.append("Median upload frequency.\n\n");
                        rewardAsString.append("10 points per hour.\n\n");
                        break;
                    case 60:
                        mapAsString.append("High upload frequency.\n\n");
                        rewardAsString.append("60 points per hour.\n\n");
                        break;
                }
            }
        }

        if(mapAsString.length() > 2 && rewardAsString.length() > 2){
            mapAsString.delete(mapAsString.length()-1,mapAsString.length());
            rewardAsString.delete(rewardAsString.length()-1,rewardAsString.length());
        } else{
            mapAsString.append("No sensor is activated");
            rewardAsString.append("No reward point detail available");
        }

        sensor_info.setText(mapAsString.toString());
        sensor_reward_unit.setText(rewardAsString.toString());
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.popup_sensor, null);
        ScrollView scrollView = (ScrollView) layout.getChildAt(1);
        TextView detail_textView = (TextView) scrollView.getChildAt(0);
        switch (v.getId()){
            case R.id.study:
                study_information.setText("Motion study collection process (Percentage of persons): 90%\n\n Location study collection process (Percentage of persons): 69%\n\n Direction study collection process (Percentage of persons): 53%");
                detail_textView.setText(study_information.getText());
                BasicDialog basicDialog0 = new BasicDialog(R.layout.popup_sensor,layout);
                basicDialog0.show(getFragmentManager(), "Study info dialog");
                break;
            case R.id.active_sensor:
            case R.id.sensor:
                detail_textView.setText(sensor_info.getText());
                BasicDialog basicDialog = new BasicDialog(R.layout.popup_sensor,layout);
                basicDialog.show(getFragmentManager(), "Sensor info dialog");
                break;
            case R.id.reward_block:
            case R.id.sensor_reward_unit:
                ((TextView)layout.getChildAt(0)).setText(getString(R.string.reward_points));
                detail_textView.setText(sensor_reward_unit.getText());
                BasicDialog basicDialog2 = new BasicDialog(R.layout.popup_sensor,layout);
                basicDialog2.show(getFragmentManager(), "Sensor info dialog");
                break;
            case R.id.upload_btn:
                // Todo: risky operation

                BatteryManager batteryManager = (BatteryManager) getActivity().getSystemService(Context.BATTERY_SERVICE);
                int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                System.out.println(battery);
                if(battery >= 15) {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                        upload_btn.setVisibility(View.GONE);
                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (SensorRecord sensorRecord : AppDatabase.getInstance(getContext()).sensorRecordDao().getAllRecords()) {
                                FUserDao.appendSensorData(FirebaseAuth.getInstance().getCurrentUser(), sensorRecord);
                            }
                            AppDatabase.getInstance(getContext()).sensorRecordDao().deleteAll();
                        }
                    }).start();
                    total_record.setText("Number of Record in local database: 0");

                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            upload_btn.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                    break;
                }
                break;
        }
    }
}
