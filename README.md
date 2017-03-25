# 利用Android计步和方向传感传感器组合使用,可以在地图上记录人行走的轨迹图

## step包(计步功能):
	StepSensorBase.java 计步传感器抽象类，计步公用方法和变量
	StepSensorAcceleration.java 加速度传感器实现计步功能
	StepSensorPedometer.java 直接使用内置计步传感器实现计步功能
	
## orient包(方向功能):
	OrientSensor.java 方向功能
	
## SensorUtil.java 传感器工具方法，主要是修正方向算法(即转动停止后的方向,才作为行走轨迹的方向)

## 使用

```java

public class MainActivity extends AppCompatActivity 
	implements StepSensorBase.StepCallBack, OrientSensor.OrientCallBack {	
	.........
	
	@Override
	public void Step(int stepNum) {
		//  计步回调
//        stepText.setText("步数:" + stepCount++);
		stepText.append(endOrient + ", ");

		// 步长和方向角度转为圆点坐标
		float x = (float) (stepLen * Math.sin(Math.toRadians(endOrient)));
		float y = (float) (stepLen * Math.cos(Math.toRadians(endOrient)));        
	}

	@Override
	public void Orient(int mOrient) {
		// 方向回调
		// 获取转动停止后的方向
		endOrient = SensorUtil.getInstance().getRotateEndOrient(mOrient);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SensorUtil.getInstance().printAllSensor(this); // 打印所有可用传感器
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
		
		.........
	}
	.........
}
	
```