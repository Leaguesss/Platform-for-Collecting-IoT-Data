package comp3888.group5.sensordatacollector;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    Button sensorConfigButton;
    Button syncSettingButton;
    Button autoManualButton;
    Button fingerPrintButton;
    Button changePasswordButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        sensorConfigButton = (Button) findViewById(R.id.sensorConfigButton);
        syncSettingButton = (Button) findViewById(R.id.syncSettingButton);
        autoManualButton = (Button) findViewById(R.id.autoManualButton);
        fingerPrintButton = (Button) findViewById(R.id.fingerprintButton);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

        sensorConfigButton.setOnClickListener(this);
        syncSettingButton.setOnClickListener(this);
        autoManualButton.setOnClickListener(this);
        fingerPrintButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sensorConfigButton) {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
        else if(v.getId() == R.id.syncSettingButton) {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
        else if(v.getId() == R.id.autoManualButton) {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
        else if(v.getId() == R.id.fingerprintButton) {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
        else if(v.getId() == R.id.changePasswordButton) {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
    }
}
