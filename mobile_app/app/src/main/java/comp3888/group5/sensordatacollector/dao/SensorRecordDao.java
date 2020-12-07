package comp3888.group5.sensordatacollector.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import comp3888.group5.sensordatacollector.model.SensorRecord;

@Dao
public interface SensorRecordDao {
    @Query("SELECT * FROM sensor_record")
    List<SensorRecord> getAllRecords();

    @Insert
    void insertRecord(SensorRecord sensorRecord);

    @Query("DELETE FROM sensor_record")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM sensor_record")
    int getCount();
}
