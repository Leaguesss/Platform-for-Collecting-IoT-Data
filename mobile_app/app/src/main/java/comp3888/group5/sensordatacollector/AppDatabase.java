package comp3888.group5.sensordatacollector;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import comp3888.group5.sensordatacollector.dao.SensorRecordDao;
import comp3888.group5.sensordatacollector.dao.UserDao;
import comp3888.group5.sensordatacollector.model.SensorRecord;
import comp3888.group5.sensordatacollector.model.User;

@Database(entities = {User.class, SensorRecord.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "appDatabase.db";
    private static volatile AppDatabase instance;
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    public AppDatabase() {};

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME).build();
    }

    public abstract UserDao userDao();
    public abstract SensorRecordDao sensorRecordDao();
}
