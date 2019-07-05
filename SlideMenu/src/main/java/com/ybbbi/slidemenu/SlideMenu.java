package com.ybbbi.slidemenu;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * ybbbi
 * 2019-07-02 13:27
 */
public class SlideMenu extends FrameLayout {

    private ViewDragHelper dragHelper;
    private View menu;
    private View main;
    enum  SlideState{
        open,close
    }
    private SlideState state=SlideState.close;
    FloatEvaluator floatEvaluator = new FloatEvaluator();
    private int maxleft;

    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("only have two child");
        }
        menu = getChildAt(0);
        main = getChildAt(1);
        if (!(menu instanceof ViewGroup) && !(main instanceof ViewGroup)) {
            throw new IllegalArgumentException("the view must be viewgroup");
        }
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxleft = (int) (getWidth() * 0.7f);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {

            return view == main || view == menu;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            //Log.i("SlideMenu", "clampViewPositionHorizontal: "+left);
            if (child == main) {
                left = fixLeft(left);

            }
            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == menu) {
                menu.layout(0, menu.getTop(), menu.getMeasuredWidth(), menu.getBottom());
                int mainleft = main.getLeft() + dx;
                mainleft = fixLeft(mainleft);

                main.layout(mainleft, main.getTop(), mainleft + main.getWidth(), main.getBottom());
            }
            float value = main.getLeft() * 1f / maxleft;
            executeAnim(value);
            if (main.getLeft() == 0&&state!=SlideState.close) {
                    state=SlideState.close;
                //关闭
                if (listener != null) {
                    listener.onClose();
                }
            } else if (main.getLeft() == maxleft&&state!=SlideState.open) {
                    state=SlideState.open;
                //打开
                if (listener != null) {
                    listener.onOpen();

                }
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (main.getLeft() > maxleft / 2) {
                dragHelper.smoothSlideViewTo(main, maxleft, main.getTop());
                ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
            } else {
                dragHelper.smoothSlideViewTo(main, 0, main.getTop());
                ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
            }
        }


    };
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private void executeAnim(float value) {
        main.setScaleY(floatEvaluator.evaluate(value, 1f, 0.8f));
        main.setScaleX(floatEvaluator.evaluate(value, 1f, 0.8f));

        menu.setScaleY(floatEvaluator.evaluate(value, 0.3f, 1f));
        menu.setScaleX(floatEvaluator.evaluate(value, 0.3f, 1f));
        menu.setTranslationX(floatEvaluator.evaluate(value, -menu.getWidth() / 2, 0));
        if (getBackground() != null) {
            getBackground().setColorFilter((Integer) argbEvaluator.evaluate(value, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
    }

    private int fixLeft(int left) {
        if (left > maxleft) {
            left = maxleft;
        } else if (left < 0) {
            left = 0;
        }
        return left;
    }

    private OnSlideChangeListener listener;

    public void setOnSlideChangeListener(OnSlideChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSlideChangeListener {
        void onOpen();

        void onClose();
    }
}
