package com.gemmily.testdemo.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Gemmily on 2017/12/1.
 */

public class ScrollerLayout extends ViewGroup {
    private Scroller mScroller;
    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;
    /**
     * 界面可滚动的左边界
     */
    private int leftBorder;

    /**
     * 界面可滚动的右边界
     */
    private int rightBorder;

    public ScrollerLayout(Context context) {
        super(context);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        // 判断触发时间最小距离
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (b) {
            int count = getChildCount();
            for (int j = 0; j < count; j++) {
                View child = getChildAt(j);
                child.layout(j * child.getMeasuredWidth(), 0, (j + 1) * child.getMeasuredWidth(), child.getMeasuredHeight());
            }
            // 初始化左右边界值
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(getChildCount() - 1).getRight();
        }
    }
}
