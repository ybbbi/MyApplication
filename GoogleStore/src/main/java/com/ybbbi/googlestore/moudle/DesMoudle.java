package com.ybbbi.googlestore.moudle;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ybbbi.googlestore.R;
import com.ybbbi.googlestore.animate.AnimateHeight;
import com.ybbbi.googlestore.bean.AppInfo;

import butterknife.BindView;

/**
 * ybbbi
 * 2019-06-27 23:37
 */
public class DesMoudle extends BaseMoudle<AppInfo> implements View.OnClickListener {
    private int height;
    @BindView(R.id.des_image)
    ImageView desImage;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_tv_author)
    TextView tvTvAuthor;

    @Override
    public int getLayout() {
        return R.layout.des_detail;
    }

    @Override
    public void loadData(AppInfo appInfo) {

        tvDes.setText(appInfo.des);
        moudleView.setOnClickListener(this);
        tvTvAuthor.setText(appInfo.author);
        tvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = tvDes.getHeight();

                tvDes.setMaxLines(5);
                tvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        tvDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        height5 = tvDes.getHeight();
                        height5 = tvDes.getHeight();
                        tvDes.setMaxLines(Integer.MAX_VALUE);
                        ViewGroup.LayoutParams params = tvDes.getLayoutParams();
                        params.height = height5;
                        tvDes.setLayoutParams(params);
                    }
                });
            }
        });

    }

    private int height5;
    private boolean isExpanding = false;
    private boolean isrunning = false;

    @Override
    public void onClick(View v) {
        if (isrunning) {
            return;
        }
        AnimateHeight animateHeight = null;
        if (isExpanding) {
            animateHeight = new AnimateHeight(height, height5, tvDes);

        } else {
            //打开
            animateHeight = new AnimateHeight(height5, height, tvDes);
            animateHeight.setonValueChange(new AnimateHeight.onValueChange() {
                @Override
                public void value(int value) {
                    scrollView.scrollBy(0, value);

                }
            });
        }
        animateHeight.start(500);
        isExpanding = !isExpanding;
        ViewCompat.animate(desImage).setListener(new ViewPropertyAnimatorListener(


        ) {
            @Override
            public void onAnimationStart(View view) {
                isrunning = true;
            }

            @Override
            public void onAnimationEnd(View view) {
                isrunning = false;
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).setDuration(500).rotationBy(180).start();
    }

    ScrollView scrollView;

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }
}
