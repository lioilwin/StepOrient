package life;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import life.orient.OrientSensor;
import life.step.StepSensorAcceleration;
import life.step.StepSensorBase;
import life.util.SensorUtil;

public class MainActivity extends AppCompatActivity implements StepSensorBase.StepCallBack, OrientSensor.OrientCallBack {
    public static final int REQUEST_IMG = 1;
    private final String TAG = "MainActivity";

    private TextView mStepText;
    private TextView mOrientText;
    private MainSurfaceView mMainSurfaceView;

    private StepSensorBase mStepSensor; // 计步传感器
    private OrientSensor mOrientSensor; // 方向传感器
    private int mStepLen = 50; // 步长
    private int mEndOrient; // 转动停止后方向

    @Override
    public void Step(int stepNum) {
        //  计步回调
        mStepText.setText("步数:" + stepNum);

        // 步长和方向角度转为圆点坐标

        mMainSurfaceView.autoAddPoint(mStepLen, mEndOrient);
    }

    @Override
    public void Orient(int orient) {
        // 方向回调
        mOrientText.setText("方向:" + orient);
        mMainSurfaceView.autoDrawArrow(orient);
        // 获取手机转动停止后的方向
        mEndOrient = SensorUtil.getInstance().getRotateEndOrient(orient);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorUtil.getInstance().printAllSensor(this); // 打印所有可用传感器

        setContentView(R.layout.activity_main);
        mStepText = (TextView) findViewById(R.id.step_text);
        mOrientText = (TextView) findViewById(R.id.orient_text);
        mMainSurfaceView = (MainSurfaceView) findViewById(R.id.step_surfaceView);

        // 注册计步监听
//        mStepSensor = new StepSensorPedometer(this, this);
//        if (!mStepSensor.registerStep()) {
        mStepSensor = new StepSensorAcceleration(this, this);
        if (!mStepSensor.registerStep()) {
            Toast.makeText(this, "计步功能不可用！", Toast.LENGTH_SHORT).show();
        }
//        }


        // 注册方向监听
        mOrientSensor = new OrientSensor(this, this);
        if (!mOrientSensor.registerOrient()) {
            Toast.makeText(this, "方向功能不可用！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销传感器监听
        mStepSensor.unregisterStep();
        mOrientSensor.unregisterOrient();
    }

    public void btnClick(View view) {
        // 添加背景地图
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        // 获取相册图片
        Cursor cursor = managedQuery(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        cursor.moveToFirst();
        Bitmap bitmap = fitBitmap(cursor.getString(0));
        mMainSurfaceView.changeBitmap(bitmap);
    }

    /**
     * 压缩图片适应屏幕
     */
    private Bitmap fitBitmap(String file) {
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 仅加载图片边界，测量大小
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, op);
        // 压缩比例
        op.inSampleSize = Math.max(op.outWidth / getResources().getDisplayMetrics().widthPixels,
                op.outHeight / getResources().getDisplayMetrics().widthPixels);
        Log.i(TAG, "压缩比率: " + op.inSampleSize);
        // 完全加载图片
        op.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, op);
    }
}
