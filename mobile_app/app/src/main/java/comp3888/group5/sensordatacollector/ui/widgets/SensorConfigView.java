package comp3888.group5.sensordatacollector.ui.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.sdsmdg.tastytoast.TastyToast;


import java.util.List;

import comp3888.group5.sensordatacollector.R;
import comp3888.group5.sensordatacollector.SensorListenService;
import comp3888.group5.sensordatacollector.util.ServiceUtils;

public class SensorConfigView extends ConstraintLayout {
    private TextView sensorNameView;
    private TextView frequencyView;

    private SwitchMaterial checkBox;
    private MaterialButtonToggleGroup toggleGroup;


    private Sensor sensor;
    private int frequency;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SensorConfigView(Context context) {
        super(context);
    }

    public SensorConfigView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sensor_config_component, this, true);
        sensorNameView = (TextView) getChildAt(0);
        frequencyView = (TextView) getChildAt(1);
        checkBox = (SwitchMaterial) getChildAt(2);
        toggleGroup = (MaterialButtonToggleGroup) getChildAt(3);

        sharedPreferences = context
                .getSharedPreferences(getContext().getString(R.string.preference_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(sensor == null) {
                    Toast.makeText(context,"Error: sensor info initialiesd yet.", Toast.LENGTH_LONG);
                    return;
                }
                if(!isChecked){
                    editor.remove(""+sensor.getType())
                            .apply();
                     ServiceUtils.serviceRestart(context, SensorListenService.class);
                }else if (frequency > 0){
                    editor.putInt(""+sensor.getType(),  frequency)
                            .apply();
                    ServiceUtils.serviceRestart(context, SensorListenService.class);
                }
            }
        });
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked){Button btn =((View)group).findViewById(checkedId);
                final String btnText = btn.getText().toString();
                if(btnText.equals(context.getString(R.string.low_freq))){
                    // low freq
                    frequency = 1;
                    TastyToast.makeText(context.getApplicationContext(), "The new configuration will cause 10% loss on reward point", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
                }else if(btnText.equals(context.getString(R.string.mid_freq))){
                    // mid
                    frequency = 10;
                    TastyToast.makeText(context.getApplicationContext(), "The new configuration will result to 10% more reward point", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
                } else if(btnText.equals(context.getString(R.string.high_freq))){
                    frequency = 60;
                    TastyToast.makeText(context.getApplicationContext(), "The new configuration will result to 20% more reward point", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);

                }
                if(sensor != null && checkBox.isChecked()){
                    editor.putInt("" + sensor.getType(), frequency).apply();
                    ServiceUtils.serviceRestart(context, SensorListenService.class);
                }}
            }
        });

    }

    public SensorConfigView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStatus(Sensor sensor){
        this.sensor = sensor;
        sensorNameView.setText(sensor.getName());
        frequency = sharedPreferences
                .getInt(""+sensor.getType(), -1);
        // TODO load active button based on different config for different sensor, hard coded for now
        if(frequency >=0) {
            checkBox.setChecked(true);
            List<Integer> checkedButtonIds = toggleGroup.getCheckedButtonIds();
            MaterialButton btn = null;
            switch (frequency){
                case 1:
                    btn = (MaterialButton) toggleGroup.getChildAt(0);
                    break;
                case 10:
                    btn = (MaterialButton) toggleGroup.getChildAt(1);
                    break;
                case 60:
                    btn = (MaterialButton) toggleGroup.getChildAt(2);
                    break;
            }
            btn.setChecked(true);
        }
    }



}
