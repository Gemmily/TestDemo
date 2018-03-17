package com.gemmily.testdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.gemmily.testdemo.adapter.RViewHolder;
import com.gemmily.testdemo.listener.OnDeleteListener;

/**
 * Created by Administrator on 2017/1/17.
 */
public class ItemDeleteView extends RecyclerView {
    private Context mContext;
    private int mLastX, mLastY;                 // 记录上次触摸位置
    private int mPosition;                      // 当前item位置
    private LinearLayout mItemLayout;            // item的布局
    private TextView tvDelete;                   // 删除按钮
    private int mMaxSlop;                        // 最大滑动距离
    private boolean isDragging;                  // 是否是垂直列表
    private boolean isMoved;                     //item是否跟随手指移动
    private boolean isScrolled;                  // item是否跟开始滑动
    private VelocityTracker mVelocityTracker;     // 手指在滑动过程种的速度


    private int mState;                           // 按钮的状态
    private final static int BTN_CLOSED = 0;      // 关闭
    private final static int BTN_WILL_CLOSE = 1;  // 将要关闭
    private final static int BTN_WILL_OPEN = 2;   // 将要打开
    private final static int BTN_OPENED = 3;      // 打开

    private Scroller mScroller;
    private OnDeleteListener mListener;


    public ItemDeleteView(Context context) {
        super(context);
        init(context);
    }

    public ItemDeleteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ItemDeleteView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mScroller = new Scroller(context, new LinearInterpolator());
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mVelocityTracker.addMovement(e);
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState == 0) {
                    //根据位置，获取点击项的view
                    View view = findChildViewUnder(x, y);
                    if (view == null)
                        return false;
                    RViewHolder viewHolder = (RViewHolder) getChildViewHolder(view);
                    mPosition = getChildAdapterPosition(view);
                    mItemLayout = viewHolder.layout;
                    tvDelete = viewHolder.delete;
                    mMaxSlop = tvDelete.getWidth();
                    tvDelete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListener.onDeleteClick(mPosition);
                            mItemLayout.scrollTo(0, 0);
                            mState = 0;
                        }
                    });
                } else if (mState == 3) {
                    mScroller.startScroll(mItemLayout.getScrollX(), 0, -mMaxSlop, 0, 200);
                    invalidate();
                    mState = 0;
                    return false;
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int dy = mLastY - y;
                int scrollX = mItemLayout.getScrollX();
                // 取绝对值 判断是否是横向滑动
                if (Math.abs(dx) > Math.abs(dy)) {
                    isMoved = true;
                    if (scrollX + dx <= 0) {
                        mItemLayout.scrollTo(0, 0);
                        return true;
                    } else if (scrollX + dx >= mMaxSlop) {
                        mItemLayout.scrollTo(mMaxSlop, 0);
                        return true;
                    }
                    // item跟随手滑动
                    mItemLayout.scrollBy(dx, 0);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!isMoved && !isDragging && mListener != null) {
                    mListener.onItemClick(mItemLayout, mPosition);
                }
                isMoved = false;
                //计算手指滑动的速度
                mVelocityTracker.computeCurrentVelocity(1000);
                //水平方向速度（向左为负）
                float xVelocity = mVelocityTracker.getXVelocity();
                //垂直方向速度
                float yVelocity = mVelocityTracker.getYVelocity();
                int deltaX = 0;
                int upScrollX = mItemLayout.getScrollX();

                if (Math.abs(xVelocity) > 100 && Math.abs(xVelocity) > Math.abs(yVelocity)) {
                    //左滑速度大于100，则删除按钮显示
                    if (xVelocity <= -100) {
                        deltaX = mMaxSlop - upScrollX;
                        mState = 2;
                    } else if (xVelocity > 100) {
                        //右滑速度大于100，则删除按钮隐藏
                        deltaX = -upScrollX;
                        mState = 1;
                    }
                } else {
                    //item的左滑动距离大于删除按钮宽度的一半，则则显示删除按钮
                    if (upScrollX >= mMaxSlop / 2) {
                        deltaX = mMaxSlop - upScrollX;
                        mState = 2;
                    } else if (upScrollX < mMaxSlop / 2) {
                        //否则隐藏
                        deltaX = -upScrollX;
                        mState = 1;
                    }
                }

                //item自动滑动到指定位置
                mScroller.startScroll(upScrollX, 0, deltaX, 0, 200);
                isScrolled = true;
                invalidate();

                mVelocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        /**
         * mScroller.computeScrollOffset()
         * 返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。
         * 这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。
         */
        if (mScroller.computeScrollOffset()) {
            mItemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else if (isScrolled) {
            isScrolled = false;
            if (mState == 1) {
                mState = 0;
            }

            if (mState == 2) {
                mState = 3;
            }

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVelocityTracker.recycle();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        isDragging = (state == SCROLL_STATE_DRAGGING);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mListener = listener;
    }

}
