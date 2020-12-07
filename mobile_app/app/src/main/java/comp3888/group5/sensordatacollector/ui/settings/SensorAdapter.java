package comp3888.group5.sensordatacollector.ui.settings;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import comp3888.group5.sensordatacollector.R;
import comp3888.group5.sensordatacollector.ui.widgets.SensorConfigView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SensorAdapter extends ArrayAdapter<Sensor> {
    private static final String TAG = "SensorAdapter";
    private Context mContext;

    public SensorAdapter(Context context, List<Sensor> objects) {
        super(context, R.layout.sensor_config_component , objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.sensor_listview_row, parent, false);

        SensorConfigView configView = (SensorConfigView) convertView.findViewById(R.id.config_row);
        configView.setStatus(getItem(position));

        return convertView;
    }
}
