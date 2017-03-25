package life;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.R.attr.strokeWidth;

public class MainSurfaceView extends SurfaceView {
    private SurfaceHolder mHolder;
    private Bitmap mBitmap;
    private Canvas mTmpCanvas;

    private Paint mPaint;
    private Paint mStrokePaint;
    private Path mArrowPath; // 箭头路径

    private int cR = 10; // 圆点半径
    private int arrowR = 20; // 箭头半径

    private float mCurX = 0;
    private float mCurY = 0;
    private int mPreOrient;

    public MainSurfaceView(Context context) {
        this(context, null);
    }

    public MainSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder(); // 获得SurfaceHolder对象
//        // 设置背景透明
//        setZOrderOnTop(true);
//        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        initPaint();     // 初始化画笔
        initArrowPath(); // 初始化箭头路径
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(mPaint);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);
    }

    /**
     * 初始化箭头
     */
    private void initArrowPath() {
        // 初始化箭头路径
        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();
    }

    /**
     * 当屏幕被触摸时调用
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurX = event.getX();
        mCurY = event.getY();
        addPoint();
        return true;
    }

    /**
     * 自动增加点
     */
    public void autoAddPoint(float stepLen, float endOrient) {
        mCurX += (float) (stepLen * Math.sin(Math.toRadians(endOrient)));
        mCurY += (float) (stepLen * Math.cos(Math.toRadians(endOrient)));
        addPoint();
    }

    /**
     * 增加点
     */
    private void addPoint() {
        if (mTmpCanvas == null) {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mTmpCanvas = new Canvas(mBitmap);
            mTmpCanvas.drawColor(Color.GRAY);
        }
        mTmpCanvas.drawCircle(mCurX, mCurY, cR, mPaint); // 在mBitmap上画点
        drawBitmap(0);  // 在surfaceView绘图
    }

    public void autoDrawArrow(int orient) {
        if (orient - mPreOrient > 6) {
            drawBitmap(orient);
        }
        mPreOrient = orient;
    }

    private void drawBitmap(int orient) {
        Canvas canvas = mHolder.lockCanvas(); // 加锁，获取canLock
        if (canvas == null || mBitmap == null) return;
        canvas.drawBitmap(mBitmap, 0, 0, null); // 将mBitmap绘到canLock
        canvas.save(); // 保存画布
        canvas.translate(mCurX, mCurY); // 平移画布
        canvas.rotate(orient); // 转动画布
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f),
                0, 360, false, mStrokePaint);
        canvas.restore(); // 恢复画布
        mHolder.unlockCanvasAndPost(canvas); // 解锁，把画布显示在屏幕上
    }

    /**
     * 更换背景地图
     */
    public void changeBitmap(Bitmap bitmap) {
        mBitmap = resizeBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, true), getWidth(), getHeight());
        if (mTmpCanvas == null) {
            mTmpCanvas = new Canvas();
        }
        mTmpCanvas.setBitmap(mBitmap);
    }

    /**
     * 缩放bitmap
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, float x, float y) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(x / w, y / h);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
    }
}