package comp3888.group5.sensordatacollector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.sdsmdg.tastytoast.TastyToast;

import comp3888.group5.sensordatacollector.util.ServiceUtils;


public class ResearchInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton showInfoBtn;
    private MaterialButton notInterestedBtn;
    private MaterialButton joinBtn;
    private MaterialTextView moreInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_info);
        showInfoBtn = findViewById(R.id.show_info_btn);
        notInterestedBtn = findViewById(R.id.not_interested_btn);
        joinBtn = findViewById(R.id.join_research_btn);
        moreInfoTextView = findViewById(R.id.more_info_textview);
        showInfoBtn.setOnClickListener(this);
        notInterestedBtn.setOnClickListener(this);
        joinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_info_btn:
                if(moreInfoTextView.getVisibility() == View.GONE){
                    moreInfoTextView.setVisibility(View.VISIBLE);
                    showInfoBtn.setText("Collapse");
                }else if(moreInfoTextView.getVisibility() == View.VISIBLE){
                    moreInfoTextView.setVisibility(View.GONE);
                    showInfoBtn.setText("More Information");
                }
                break;
            case R.id.not_interested_btn:
                finish();
                break;
            case R.id.join_research_btn:
                Log.i("Research INFO","in join research btn");
                SharedPreferences sharedPreferences = getApplicationContext().
                        getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt(""+Sensor.TYPE_ACCELEROMETER, 1)
                        .putInt(""+Sensor.TYPE_GYROSCOPE, 10)
                        .apply();
                ServiceUtils.serviceRestart(getApplicationContext(), SensorListenService.class);
                TastyToast.makeText(getApplicationContext(), "Adjusted to new configuration successfully.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                finish();
                break;
        }
    }
}