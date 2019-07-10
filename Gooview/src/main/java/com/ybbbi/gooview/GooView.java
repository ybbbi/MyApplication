package com.ybbbi.gooview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * ybbbi
 * 2019-07-10 18:08
 */
public class GooView extends View {
    private Paint paint;
    ;

    public GooView(Context context) {
        this(context, null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
    }

    private PointF dragF = new PointF(400, 400);
    private PointF stickF = new PointF(400, 400);
    private PointF controlF = new PointF(300, 200);
    private PointF[] stickpoint = {new PointF(400, 350), new PointF(400, 350)};
    private PointF[] dragpoint = {new PointF(400, 350), new PointF(400, 350)};
    private float dragR = 20;
    private float stickR = 20;
    private double lineK;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float yOffset = dragF.y - stickF.y;
        float xOffset = dragF.x - stickF.x;
        if (xOffset != 0) {
            lineK = yOffset / xOffset;
        }
        dragpoint = GeometryUtil.getIntersectionPoints(dragF, dragR, lineK);
        stickpoint = GeometryUtil.getIntersectionPoints(stickF, stickR, lineK);
        controlF = GeometryUtil.getPointByPercent(dragF, stickF, 0.618f);

        stickR=calculate();


        canvas.drawCircle(dragF.x, dragF.y, dragR, paint);
        if(!isover) {
        canvas.drawCircle(stickF.x, stickF.y, stickR, paint);
            Path path = new Path();
            path.moveTo(stickpoint[0].x, stickpoint[0].y);
            path.quadTo(controlF.x, controlF.y, dragpoint[0].x, dragpoint[0].y);
            path.lineTo(dragpoint[1].x, dragpoint[1].y);
            path.quadTo(controlF.x, controlF.y, stickpoint[1].x, stickpoint[1].y);


            canvas.drawPath(path, paint);
        }
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(stickF.x, stickF.y, max, paint);
            paint.setStyle(Paint.Style.FILL);

    }
    private float max=300;
    private float calculate() {
        float points = GeometryUtil.getDistanceBetween2Points(dragF, stickF);
        float percent = points / max;


        return GeometryUtil.evaluateValue(percent,20,3);
    }
    boolean isover=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                dragF.set(event.getX(), event.getY());
                float points = GeometryUtil.getDistanceBetween2Points(dragF, stickF);
                isover=points>max;
                break;
            case MotionEvent.ACTION_UP:
                final PointF point=new PointF(dragF.x,dragF.y);

                ValueAnimator animator = ValueAnimator.ofInt(0, 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animation.getAnimatedFraction();
                        PointF point1 = GeometryUtil.getPointByPercent(point,stickF, fraction);
                        dragF.set(point1);
                        invalidate();
                    }
                });
                animator.setInterpolator(new OvershootInterpolator(3));
                animator.setDuration(600);
                animator.start();
                break;
        }
        invalidate();
        return true;
    }
}
