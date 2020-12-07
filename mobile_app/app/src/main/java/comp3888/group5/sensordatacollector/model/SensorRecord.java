package comp3888.group5.sensordatacollector.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import comp3888.group5.sensordatacollector.util.StringListConverter;
import comp3888.group5.sensordatacollector.util.StringMapConverter;

@Entity(tableName = "sensor_record")
public class SensorRecord {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="rid")
    public int rid;

    @ColumnInfo(name = "sensor_type")
    public String sensorType;

    @ColumnInfo(name = "sensor_data")
    @TypeConverters(StringListConverter.class)
    public List<Double> sensorData;

    @ColumnInfo(name = "time_recorded")
    public String time_recorded = new Date().toString();

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public List<Double> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<Double> sensorData) {
        this.sensorData = sensorData;
    }

    public String getTime_recorded() {
        return time_recorded;
    }

    public void setTime_recorded(String time_recorded) {
        this.time_recorded = time_recorded;
    }
}
