package demo.step;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class StepSurfaceView extends SurfaceView {
    private final Paint paint;
    private Bitmap bitmap;
    private SurfaceHolder mHolder;
    private Canvas canLock;
    private final int r = 10; // 圆点半径
    private boolean flag = true;
    private Canvas canTmp;
    private float X = 0;
    private float Y = 0;

    public StepSurfaceView(Context context) {
        this(context, null);
    }

    public StepSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder(); // 获得SurfaceHolder对象

        // 设置背景透明
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        paint = new Paint(); // 创建画笔对象
        paint.setColor(Color.BLUE); // 设置画笔颜色
    }

    /**
     * 当屏幕被触摸时调用
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        X = event.getX();
        Y = event.getY();
        addPoint(X, Y);
        return true;
    }

    /**
     * 自动增加点
     */
    public void autoAddPoint(float x, float y) {
        X += x;
        Y += y;
        addPoint(X, Y);
    }

    /**
     * 在canLock增加点
     */
    private void addPoint(float x, float y) {
        canLock = mHolder.lockCanvas(); // 加锁，获取canLock

        // 保存绘图到bitamp
        if (flag == true) {
            flag = false;
            bitmap = Bitmap.createBitmap(canLock.getWidth(), canLock.getHeight(), Bitmap.Config.ARGB_8888);
            canTmp = new Canvas();
            canTmp.setBitmap(bitmap);
        }
        canTmp.drawCircle(x, y, r, paint); // 通过canTmp在bitamp上画点

        canLock.drawBitmap(bitmap, 0, 0, null); // 将bitamp绘到canLock
        mHolder.unlockCanvasAndPost(canLock); // 解锁，把画布显示在屏幕上
    }
}