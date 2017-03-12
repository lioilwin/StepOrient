//package com.base.basepedo;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.base.basepedo.orient.MyCompassView;
//import com.base.basepedo.util.SensorUtil;
//
//import java.util.List;
//
//public class MainActivity2 extends AppCompatActivity {
//    private SensorManager mSensorManager;
//    private Sensor mSensor;
//    // 自定义指针View
//    private MyCompassView myCompassView;
//    // 方向传感监听
//    private final SensorEventListener mSensorListener = new SensorEventListener() {
//        public void onSensorChanged(SensorEvent event) {
//            if (myCompassView != null) {
//                myCompassView.setOrient(event.values);
//                myCompassView.invalidate();
//                text_step.setText((int)event.values[0]+"");
//            }
//        }
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        }
//    };
//    private TextView text_step;
//
//
//    @Override
//    protected void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//        SensorUtil.printAll(this);
//        setContentView(R.layout.activity_main);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        mSensorManager.unregisterListener(mSensorListener);
//        super.onDestroy();
//    }
//}