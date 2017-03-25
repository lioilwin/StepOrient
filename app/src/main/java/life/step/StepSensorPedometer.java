package life.step;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * 计步传感器
 */
public class StepSensorPedometer extends StepSensorBase {
    private final String TAG = "StepSensorPedometer";
    private int lastStep = -1;
    private int liveStep = 0;
    private int increment = 0;
    private int sensorMode = 0; // 计步传感器类型

    public StepSensorPedometer(Context context, StepCallBack stepCallBack) {
        super(context, stepCallBack);
    }

    @Override
    protected void registerStepListener() {
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_GAME)) {
            isAvailable = true;
            sensorMode = 0;
            Log.i(TAG, "计步传感器Detector可用！");
        } else if (sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_GAME)) {
            isAvailable = true;
            sensorMode = 1;
            Log.i(TAG, "计步传感器Counter可用！");
        } else {
            isAvailable = false;
            Log.i(TAG, "计步传感器不可用！");
        }
    }

    @Override
    public void unregisterStep() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        liveStep = (int) event.values[0];
        if (sensorMode == 0) {
            Log.i(TAG, "Detector步数："+liveStep);
            StepSensorBase.CURRENT_SETP += liveStep;
        } else if (sensorMode == 1) {
            Log.i(TAG, "Counter步数："+liveStep);
            StepSensorBase.CURRENT_SETP = liveStep;
        }
        stepCallBack.Step(StepSensorBase.CURRENT_SETP);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}