package com.gemmily.testdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.icu.text.DecimalFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gemmily.testdemo.R;


/**
 * Created by Gemmily on 2017/11/29.
 */

public class CustomView extends View {
    private Paint mArcPaint;
    private Paint mShadowPaint;
    private Paint mTextPaint;
    private Bitmap mBitmap;
    private final int diameter = 500;
    private final int padding = 30;
    private int lastX, lastY;
    private ValueAnimator mProgressAnimator;
    // 当前弧度
    private float mCurrentAngel;
    private String mCurrent = "0.0%";

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Paint initPaint(String color) {
        // Paint paint = new Paint();
        // 绘画时抗锯齿
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 设置透明度
        // mArcPaint.setAlpha(9);
        // 设置颜色过滤器
        // mArcPaint.setColorFilter(ColorMatrix colorMatrix)
        paint.setColor(Color.parseColor(color));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        return paint;
    }

    private void init() {
        mArcPaint = initPaint("#ff5e00");
        mShadowPaint = initPaint("#80000000");
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(40);
        getBitmap();
        initAnimation();
    }

    private void getBitmap() {
        // 从资源中获取bitmap
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.home_plan);
        // Drawable转bitmap
        //        Drawable drawable = getResources().getDrawable(R.mipmap.home_plan);
        //        int width = drawable.getIntrinsicWidth();
        //        int height = drawable.getIntrinsicHeight();
        //        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        //        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //        Canvas canvas = new Canvas(bitmap);
        //        drawable.setBounds(0, 0, width, height);
        //        drawable.draw(canvas);
    }


    private void canvasOnDraw(Canvas canvas) {
        // 绘制一个区域
        // canvas.drawRect(RectF r,  Paint paint);
        // 绘制一个路径
        //  canvas.drawPath(Path path, Paint paint);
        // 贴图 Param2.bitmap的源区域 param3. bitmap的目标区域
        //  canvas.drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint);
        //起点X、Y轴的位置， 终点X、Y轴的位置
        // canvas.drawLine(float startX, float startY, float stopX, float stopY, Paint paint);
        // 绘制文字 坐标为文字的下基线
        // canvas.drawText(String text, float x, floaty, Paint paint);
        // 画圆 圆心坐标和半径
        // canvas.drawCircle(float cx, float cy, float radius, Paint paint);
        // 画圆弧 param1 椭圆外切矩形 param2 开始角度 param3 扫过角度 param4 是否包含圆心
        // 以时钟3点钟的方向为0度 顺时针为正方向
        //  canvas.drawArc( RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 绘制的view必须要有具体宽高
        // 父视图经过计算传给子视图 父类提供的可供参考的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量
        // MeasureSpec = specSize （记录大小）+ specMode（记录规格）
        // specMode
        // EXACTLY AI_MOST UNSPECIFIED

        setMeasuredDimension(diameter, diameter);
        // 设置子View实际的宽高
        // 在setMeasuredDimension()方法调用之后
        // 才能使用getMeasuredWidth()和getMeasuredHeight()来获取视图测量出的宽高，以此之前调用这两个方法得到的值都会是0。

        //        int width = getSize(300, widthMeasureSpec);
        //        int height = getSize(300, heightMeasureSpec);
        //        setMeasuredDimension(width, height);
    }

    private int getSize(int defaultSize, int measureSpec) {
        int mSize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                //
                mSize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                // wrap_content
                mSize = size;
                break;
            case MeasureSpec.EXACTLY:
                // match_content 指定大小
                mSize = size;
                break;
        }
        return 0;
    }
    /*
    <CustomView
     android:layout_width=" 300dp"
     android:layout_height="wrap_content"/>
    */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 坐标相对于父视图
        super.onLayout(changed, left, top, right, bottom);
        //确定视图的位置。
        // 排列所有子View的位置
        // 通过getChildCount()获取所有子view，getChildAt获取childview调用各自的layout(int, int, int, int)方法来排列自己。
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘画
        RectF rectF = new RectF(padding, padding, diameter - padding, diameter - padding);
        canvas.drawArc(rectF, 135, 270, false, mShadowPaint);
        canvas.drawArc(rectF, 135, mCurrentAngel, false, mArcPaint);
        canvas.drawText("test", rectF.centerX() - padding, diameter - 4 * padding, mTextPaint);
        canvas.drawText(mCurrent, rectF.centerX() - padding, rectF.centerY(), mTextPaint);
        canvas.drawBitmap(mBitmap, rectF.centerX() - mBitmap.getWidth() / 2, diameter - 4 * padding, mShadowPaint);
    }

    private void initAnimation() {
        // 3种动画效果
        // Tween Animation(只能对View进行操作 有透明 移动 缩放 旋转四种效果 )
        // FrameAnimation(将多张图片连贯播放)
        // PropertyAnimation(改变属性值来实动画效果)

        // ValueAnimator 属性动画机制的核心类 计算初始值结束值之间过度动画
        // 耗时300ms 从0过度到1
        //  final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        //        animator.setDuration(300);
        //        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //            @Override
        //            public void onAnimationUpdate(ValueAnimator valueAnimator) {
        //                float currentValue = (float) animator.getAnimatedValue();
        //            }
        //        });
        //        animator.start();
        // 设置动画的重复次数
        // animator.setRepeatCount();
        mProgressAnimator = new ValueAnimator().ofFloat(0, 60);
        mProgressAnimator.setDuration(3000);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float current = (float) valueAnimator.getAnimatedValue();
                mCurrent = current * 4 / 3 + "%";
                mCurrentAngel = current * 270 / 100;
                invalidate();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
               /*
                *1.
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                *2.
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                */
                ((View) getParent()).scrollBy(-offsetX, -offsetY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void startProgressAnimation() {
        mProgressAnimator.start();
    }
}
