package com.ybbbi.indexbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * ybbbi
 * 2019-07-10 12:49
 */
public class IndexBar extends View {
    private Paint paint;
    private static final int COLOR_DEFAULT = Color.WHITE;
    private static final int COLOR_PRESS = Color.GRAY;


    String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    private float height;
    private int y = -1;


    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(COLOR_DEFAULT);

        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp16));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = getMeasuredHeight() * 1f / arr.length;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        char text='A';


        for (int i = 0; i < arr.length; i++) {

//           text = (char) (text + i);
            float x = getMeasuredWidth() / 2;
            String text = arr[i];
            float y = height / 2 + i * height + gettextheight(text) / 2;



                paint.setColor(i==this.y?COLOR_PRESS:COLOR_DEFAULT);


            canvas.drawText(text, x, y, paint);

        }
    }

    private int gettextheight(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (y != (int) (event.getY() / height)) {
                    y = (int) (event.getY() / height);
                    String s = arr[y];
                    if (y >= 0 && y < arr.length)
                        if (listener != null)
                            listener.onChange(s);


                }

                break;
            case MotionEvent.ACTION_UP:
                y=-1;
                break;
        }
        invalidate();
        return true;
    }

    private OnChangeListener listener;

    public interface OnChangeListener {
        void onChange(String s);
    }

    public void setOnChangeListener(OnChangeListener listener) {

        this.listener = listener;
    }


}
