package comp3888.group5.sensordatacollector.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StringListConverter {
    static Gson gson = new Gson();
    @TypeConverter
    public static List<Double> stringToList(String data){
        if(data == null) return Collections.emptyList();
        Type type = new TypeToken<List<Double>>() {
        }.getType();

        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String listToString(List<Double> list){
        return gson.toJson(list);
    }
}
