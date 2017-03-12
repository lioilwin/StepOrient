package demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import demo.orient.CompassView;
import demo.orient.OrientCallBack;
import demo.orient.OrientSensor;
import demo.step.StepCallBack;
import demo.step.StepSensorAcceleration;
import demo.step.StepSensorPedometer;
import demo.step.StepSensorBase;
import demo.step.StepSurfaceView;
import demo.util.SensorUtil;

public class MainActivity extends AppCompatActivity implements StepCallBack, OrientCallBack {
    private final String TAG = "MainActivity";
    private TextView stepText;
    private TextView orientText;
    private StepSurfaceView stepSurfaceView;
    private CompassView compassView;

    private StepSensorBase stepSensor; // 计步传感器
    private OrientSensor orientSensor; // 方向传感器

    @Override
    public void Step(int stepNum) {
        //  计步回调
        stepText.setText("步数:" + stepNum);
        stepSurfaceView.autoAddPoint(0, 30);
    }


    @Override
    public void Orient(float orient) {
        // 方向回调
        orientText.setText("方向:" + (int) orient);
        compassView.setOrient(orient);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorUtil.printAll(this); // 打印所有可用传感器

        setContentView(R.layout.activity_main);
        stepText = (TextView) findViewById(R.id.step_text);
        orientText = (TextView) findViewById(R.id.orient_text);
        stepSurfaceView = (StepSurfaceView) findViewById(R.id.step_surfaceView);
        compassView = (CompassView) findViewById(R.id.compass_view);

        // 开启计步监听
        stepSensor = new StepSensorPedometer(this, this);
        if (!stepSensor.registerStep()) {
            stepSensor = new StepSensorAcceleration(this, this);
            if (!stepSensor.registerStep()) {
                Toast.makeText(this, "加速度传感器不可用！", Toast.LENGTH_SHORT).show();
            }
        }

        // 开启方向监听
        orientSensor = new OrientSensor(this, this);
        if (!orientSensor.registerOrient()) {
            Toast.makeText(this, "方向传感器不可用！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销传感器监听
        stepSensor.unregisterStep();
        orientSensor.unregisterOrient();
    }
}
