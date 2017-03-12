package demo.orient;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * 方向传感器
 */

public class OrientSensor implements SensorEventListener {
    private static final String TAG = "OrientSensor";
    private SensorManager sensorManager;
    private OrientCallBack orientCallBack;
    private Context context;

    public OrientSensor(Context context, OrientCallBack orientCallBack) {
        this.context = context;
        this.orientCallBack = orientCallBack;
    }


    /**
     * 注册方向监听器
     *
     * @return 是否支持
     */
    public Boolean registerOrient() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        boolean isAvailable = sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        if (isAvailable) {
            Log.i(TAG, "方向传感器可用！");
        } else {
            Log.i(TAG, "方向传感器不可用！");
        }
        return isAvailable;
    }

    /**
     * 注销方向监听器
     */
    public void unregisterOrient() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        orientCallBack.Orient(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
