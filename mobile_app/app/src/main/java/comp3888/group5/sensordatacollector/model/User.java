package comp3888.group5.sensordatacollector.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Map;

import comp3888.group5.sensordatacollector.util.StringMapConverter;

@Entity(tableName = "user")
public class User {

    @PrimaryKey
    @ColumnInfo(name="uid")
    public int uid;

    @ColumnInfo(name="user_name")
    public String userName;

    @ColumnInfo(name="email")
    public String email;

    @ColumnInfo(name="phone_number")
    public String phoneNumber;

    @ColumnInfo(name="detail_info")
    @TypeConverters(StringMapConverter.class)
    public Map<String, String> detailInfo;

    @ColumnInfo(name="sensor_config")
    @TypeConverters(StringMapConverter.class)
    public Map<String, String> sensorConfig;

    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Map<String, String> getDetailInfo() {
        return detailInfo;
    }

    public Map<String, String> getSensorConfig() {
        return sensorConfig;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDetailInfo(Map<String, String> detailInfo) {
        this.detailInfo = detailInfo;
    }

    public void setSensorConfig(Map<String, String> sensorConfig) {
        this.sensorConfig = sensorConfig;
    }
}
