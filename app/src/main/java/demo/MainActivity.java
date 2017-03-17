package demo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import demo.orient.CompassView;
import demo.orient.OrientSensor;
import demo.step.StepSensorAcceleration;
import demo.step.StepSensorBase;
import demo.step.StepSurfaceView;
import demo.util.SensorUtil;

public class MainActivity extends AppCompatActivity implements StepSensorBase.StepCallBack, OrientSensor.OrientCallBack {
    public static final int REQUEST_IMG = 1;
    private final String TAG = "MainActivity";

    private TextView stepText;
    private TextView orientText;
    private StepSurfaceView stepSurfaceView;
    private CompassView compassView;
    private ImageView imageView;

    private int stepLen = 50; // 步长
    private StepSensorBase stepSensor; // 计步传感器
    private OrientSensor orientSensor; // 方向传感器

    private int endOrient;

    @Override
    public void Step(int stepNum) {
        //  计步回调
//        stepText.setText("步数:" + stepCount++);
        stepText.append(endOrient + ", ");

        // 步长和方向角度转为圆点坐标
        float x = (float) (stepLen * Math.sin(Math.toRadians(endOrient)));
        float y = (float) (stepLen * Math.cos(Math.toRadians(endOrient)));
        stepSurfaceView.autoAddPoint(x, -y);
    }

    @Override
    public void Orient(int orient) {
        // 方向回调
        compassView.setOrient(-orient); // 指针转动
        orientText.setText("方向:" + orient);
        endOrient = SensorUtil.getInstance().getRotateEndOrient(orient);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorUtil.getInstance().printAllSensor(this); // 打印所有可用传感器

        setContentView(R.layout.activity_main);
        stepText = (TextView) findViewById(R.id.step_text);
        orientText = (TextView) findViewById(R.id.orient_text);
        stepSurfaceView = (StepSurfaceView) findViewById(R.id.step_surfaceView);
        compassView = (CompassView) findViewById(R.id.compass_view);
        imageView = (ImageView) findViewById(R.id.image_view);

        // 注册计步监听
//        stepSensor = new StepSensorPedometer(this, this);
//        if (!stepSensor.registerStep()) {
            stepSensor = new StepSensorAcceleration(this, this);
            if (!stepSensor.registerStep()) {
                Toast.makeText(this, "计步功能不可用！", Toast.LENGTH_SHORT).show();
            }
//        }


        // 注册方向监听
        orientSensor = new OrientSensor(this, this);
        if (!orientSensor.registerOrient()) {
            Toast.makeText(this, "方向功能不可用！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销传感器监听
        stepSensor.unregisterStep();
        orientSensor.unregisterOrient();
    }

    public void btnClick(View view) {
        // 添加背景图
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        // 获取相册图片
        Cursor cursor = managedQuery(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        cursor.moveToFirst();
        Bitmap bitmap = resizePhono(cursor.getString(0));
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 压缩图片适应屏幕
     */
    private Bitmap resizePhono(String file) {
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 仅加载图片边界，测量大小
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, op);
        double ratio = Math.max((double) op.outWidth / getResources().getDisplayMetrics().widthPixels,
                (double) op.outHeight / getResources().getDisplayMetrics().widthPixels);
        if (ratio > 1) {
            op.inSampleSize = (int) ratio; // 压缩图片适应屏幕
            Log.i(TAG, "压缩比率: " + op.inSampleSize);
        }

        // 完全加载图片
        op.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, op);
    }
}
