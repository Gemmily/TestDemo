package com.gemmily.testdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Gemmily on 2017/12/4.
 */

public class CustomSwipeView extends LinearLayout {
    private Scroller mScroller;

    public CustomSwipeView(Context context) {
        super(context);
    }

    public CustomSwipeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwipeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }
}
