package com.gemmily.testdemo.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gemmily.testdemo.R;

/**
 * Created by wyg on 2017/1/16.
 */
public class SwipeRefreshView extends SwipeRefreshLayout {

    private final int mScaledTouchSlop;
    private final View mFooterView;
    private ListView mListView;
    private OnLoadListener onLoadListener;
    private float mDownY, mUpY;     //在分发事件的时候处理子控件的触摸事件
    private boolean isLoading;


    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFooterView = View.inflate(context, R.layout.view_footer, null);
        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 获取ListView,设置ListView的布局位置
        if (mListView == null) {
            if (getChildCount() > 0) {
                if (getChildAt(0) instanceof ListView) {
                    mListView = (ListView) getChildAt(0);
                    setListViewOnScroll();
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLoadMore()) {
                    loadData();
                }
                break;
            case MotionEvent.ACTION_UP:
                mUpY = getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setListViewOnScroll() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                // 移动过程中判断 触发下拉加载更多
                if (isLoadMore()) {
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    private boolean isLoadMore() {
        // 上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        if (condition1) {
            Log.i("result", "上拉状态");
        }
        // 当前页面可见的item是最后一个条目
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {
            condition2 = (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1);
            if (condition2) {
                Log.i("result", "是最后一个条目");
            }
        }

        // 加载状态
        boolean condition3 = !isLoading;
        if (condition3) {
            Log.i("result", "不是正在加载状态");
        }

        return condition1 && condition2 && condition3;
    }

    private void loadData() {
        if (onLoadListener != null) {
            setLoading(true);
            onLoadListener.onLoad();
        }
    }

    public void setLoading(boolean loading) {
        // 更改状态
        isLoading = loading;
        if (isLoading) {
            // 显示 footer
            mListView.addFooterView(mFooterView);
        } else {
            // 隐藏 footer
            mListView.removeFooterView(mFooterView);

            // 重置 滑动坐标
            mDownY = 0;
            mUpY = 0;
        }
    }

    public interface OnLoadListener {
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener listener) {

        this.onLoadListener = listener;
    }

}
