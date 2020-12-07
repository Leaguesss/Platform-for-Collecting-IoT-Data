package comp3888.group5.sensordatacollector.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class StringMapConverter {
    static Gson gson = new Gson();
    @TypeConverter
    public static Map<String, String> stringToMap(String data){
        if(data == null) return Collections.emptyMap();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String mapToString(Map<String, String> map){
        return gson.toJson(map);
    }
}
