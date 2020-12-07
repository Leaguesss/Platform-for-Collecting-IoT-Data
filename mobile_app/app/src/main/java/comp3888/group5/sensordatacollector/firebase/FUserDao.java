package comp3888.group5.sensordatacollector.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import comp3888.group5.sensordatacollector.model.SensorRecord;

public class FUserDao {
    public static void createNewUser(FirebaseUser u){
        FUser user = new FUser(u.getEmail(), u.getUid(), u.getMetadata().getCreationTimestamp());
        FirebaseDatabase.getInstance().getReference("users").child(u.getUid()).setValue(user);
    }

    public static void appendSensorData(FirebaseUser u, SensorRecord sensorRecord){
        DatabaseReference sensor = FirebaseDatabase.getInstance().getReference("users")
                .child(u.getUid()).child("sensor").push();

        sensor.setValue(sensorRecord);

    }
}
