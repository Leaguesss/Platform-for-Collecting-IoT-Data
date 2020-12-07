package comp3888.group5.sensordatacollector.util;

public class SensorConfigStringUtils {
    public static String progressToFreqText(int progress){
        if(progress == 0) return "Disabled";
        return ""+progress + "times per minutes";
    }

    public static String progressToLevelOfFrequency(int progress){
        if(progress == 0) return "Disabled";
        if(progress < 33) return "Low Frequency";
        if(progress > 66) return "High Frequency";
        return "Medium Frequency";
    }
}
