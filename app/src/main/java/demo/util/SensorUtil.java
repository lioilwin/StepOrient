package demo.util;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

/**
 * Created by 19219 on 2017/3/10.
 */
public class SensorUtil {
    private static final String TAG = "SensorUtil";

    /**
     * 打印所有传感器
     */
    public static void printAll(Context context){
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            Log.d(TAG, "AllSensor----: "+sensor.getName());
        }
    }
}
