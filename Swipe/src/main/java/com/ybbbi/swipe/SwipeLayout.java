package com.ybbbi.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ybbbi
 * 2019-07-05 11:59
 */
public class SwipeLayout extends FrameLayout {
    private ViewDragHelper dragHelper;
    private View content;
    private View delete;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper = ViewDragHelper.create(this, callback);
    }

    /**
     * 在布局加载结束后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        content = getChildAt(0);
        delete = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        content.layout(0, 0, content.getMeasuredWidth(), content.getMeasuredHeight());
        delete.layout(content.getRight(), 0, content.getRight() + delete.getMeasuredWidth(), delete.getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;

        }
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return true;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            if(content.getLeft()<-delete.getMeasuredWidth()/2){
                //
                dragHelper.smoothSlideViewTo(content,-delete.getMeasuredWidth(),0);
                ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
            }else{
                dragHelper.smoothSlideViewTo(content,0,0);
                ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);

            }

        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child == content) {

                if (left > 0) {
                    left = 0;
                } else if (left <- delete.getMeasuredWidth()) {
                    left = -delete.getMeasuredWidth();
                }
            }
            if(child==delete){
                if (left <content.getMeasuredWidth()- delete.getMeasuredWidth()) {
                    left = content.getMeasuredWidth()-delete.getMeasuredWidth();
                }else if(left>content.getMeasuredWidth()){
                    left=content.getMeasuredWidth();
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if (content == changedView) {
                delete.layout(delete.getLeft() + dx, 0, delete.getRight() + dx, delete.getHeight());
            } else if (changedView == delete) {
                content.offsetLeftAndRight(dx);
            }
            invalidate();



        }

    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(dragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }
}
