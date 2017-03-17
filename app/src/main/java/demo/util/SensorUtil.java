package demo.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;
import java.util.Stack;

/**
 * 传感器工具类
 */
public class SensorUtil {
    private static final String TAG = "SensorUtil";
    private static final SensorUtil sensorUtil = new SensorUtil(); // 单例常量

    public static final int SENSE = 10; // 灵敏度
    private int initialOrient = -1; // 初始方向
    private boolean isRotating = false; // 是否正在转动

    private int lastDOrient = 0; // 上次方向与初始方向差值
    private Stack<Integer> dOrientStack = new Stack<>(); // 方向差值缓存栈
    private SensorManager sensorManager;

    private SensorUtil() {
    }

    /**
     * 单例获取
     */
    public static SensorUtil getInstance() {
        return sensorUtil;
    }

    /**
     * 获取传感器管理类的实例
     *
     * @param context
     * @return
     */
    public SensorManager getSensorManager(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        return sensorManager;
    }

    /**
     * 打印所有可用传感器
     */
    public void printAllSensor(Context context) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            Log.d(TAG, "所有可用传感器----: " + sensor.getName());
        }
    }

    /**
     * 获取手机转动结束后的方向
     *
     * @param orient 手机实时方向
     * @return 返回转动结束后的正确方向
     */
    public int getCorrectOrient(int orient) {
        int correctOrient = 0; // 正确方向
        if (initialOrient == -1) {
            // 初始化转动
            initialOrient = correctOrient = orient;
            Log.i(TAG, "Orient: 初始化方向：" + correctOrient);
        }

        int currentDOrient = Math.abs(orient - initialOrient); // 当前方向与初始方向差值
        if (!isRotating) {
            // 检测是否开始转动
            lastDOrient = currentDOrient;
            if (lastDOrient >= SENSE) {
                // 开始转动
                isRotating = true;
            }
        } else {
            // 检测是否停止转动
            Log.i(TAG, "Orient: 正在转动，当前方向：" + orient);

            if (currentDOrient <= lastDOrient) {
                // 至少SENSE次出现当前方向反向或不变, 循环判断SENSE次方向差距与当前差距是否都小于SENSE
                int size = dOrientStack.size();
                if (size >= SENSE) {
                    for (int i = 0; i < size; i++) {
                        if (Math.abs(currentDOrient - dOrientStack.pop()) >= SENSE) {
                            isRotating = true;
                            break;
                        }
                        isRotating = false;
                    }
                }

                if (!isRotating) {
                    dOrientStack.clear();
                    initialOrient = -1;
                    correctOrient = orient;
                    Log.i(TAG, "Orient: 停止转动------，正确方向：" + correctOrient);
                } else {
                    dOrientStack.push(currentDOrient);
                }

            } else {
                lastDOrient = currentDOrient;
            }
        }
        return correctOrient;
    }
}