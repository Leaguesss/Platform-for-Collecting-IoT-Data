package comp3888.group5.sensordatacollector.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import comp3888.group5.sensordatacollector.R;
import comp3888.group5.sensordatacollector.ui.widgets.BasicDialog;

public class SettingsFragment extends Fragment {


    private ImageButton info_button;
    ListView listView;
    SwitchMaterial autoSwitch;

    private SensorManager sensorManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


//    TextView DashboardTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        listView = root.findViewById(R.id.listViewID);
        autoSwitch = root.findViewById(R.id.auto_switch);

//        DashboardTextView = root.findViewById(R.id.sensor);

        sharedPreferences =  getActivity().getApplicationContext()
                .getSharedPreferences(getContext().getString(R.string.other_setting_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        SensorAdapter sensorAdapter = new SensorAdapter(getContext(), sensorList);
        listView.setAdapter(sensorAdapter);

        // linking button from xml

        info_button =  root.findViewById(R.id.infobutton);
        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    autoSwitch.setText("Enabled");
                    editor.putBoolean("auto", true)
                    .apply();
                    // TODO logic to actually make change to setting
                }
                else {
                    autoSwitch.setText("Disabled");
                    editor.putBoolean("auto", false)
                    .apply();
                    // TODO logic to actually make change to setting
                }
            }
        });

        autoSwitch.setChecked(sharedPreferences.getBoolean("auto", false));
        return root;
    }
    public void openDialog()
    {
        BasicDialog basicDialog = new BasicDialog(R.layout.table_layout);
        basicDialog.show(getFragmentManager(), "example dialog");
    }

}