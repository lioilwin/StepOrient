package demo.orient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 指针View
 */
public class CompassView extends View {
    private Paint paint; // 画笔
    private Path path; // 指针绘制路径
    private float orient = 0;

    public CompassView(Context context) {
        this(context, null);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaintAndPath();
    }

    private void initPaintAndPath() {
        // 初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        // 初始化绘制路径
        path = new Path();
        path.moveTo(0, 0);// 移动到指点点
        path.lineTo(-20, 60);// 用线条连接到指定点
        path.lineTo(0, 50);
        path.lineTo(20, 60);
        path.close();// 关闭路径
    }

    /**
     * 设置指针方向
     */
    public void setOrient(float orient) {
        this.orient = orient;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT); // 设置画面背景
        int cx = canvas.getWidth() / 2;
        int cy = canvas.getHeight() / 2;
        canvas.translate(cx, 60);// 移动画面，把指针放到中央
        canvas.rotate(orient);
        canvas.drawPath(path, paint);
    }
}