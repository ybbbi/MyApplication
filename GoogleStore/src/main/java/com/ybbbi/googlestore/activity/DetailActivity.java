package com.ybbbi.googlestore.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.ybbbi.googlestore.NetURL.NetUrl;
import com.ybbbi.googlestore.R;
import com.ybbbi.googlestore.bean.AppInfo;
import com.ybbbi.googlestore.http.HttpHelper;
import com.ybbbi.googlestore.moudle.DesMoudle;
import com.ybbbi.googlestore.moudle.DetailMoudle;
import com.ybbbi.googlestore.moudle.DownloadMoudle;
import com.ybbbi.googlestore.moudle.ScreenMoudle;
import com.ybbbi.googlestore.moudle.TagMoudle;
import com.ybbbi.googlestore.view.StateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ybbbi
 * 2019-06-26 14:51
 */
public class DetailActivity extends AppCompatActivity {


    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.detail_frame)
    FrameLayout detailFrame;
    private StateLayout stateLayout;
    private DetailMoudle detailMoudle;
    private ScreenMoudle screenMoudle;
    private TagMoudle tagMoudle;
    private DesMoudle desMoudle;
    private DownloadMoudle downloadMoudle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String packageName = getIntent().getStringExtra("packageName");
        stateLayout = new StateLayout(this);

        setacionbar();
        setContentView(stateLayout);

        detailMoudle = new DetailMoudle();
        tagMoudle = new TagMoudle();
        screenMoudle = new ScreenMoudle();
        desMoudle = new DesMoudle();
        downloadMoudle = new DownloadMoudle();
        screenMoudle.setActivity(this);
        stateLayout.bindSuccessView(getsuccessView());
        stateLayout.showloadingView();

        loadData(packageName);

    }

    private void setacionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_detail));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取成功的视图
     *
     * @return
     */
    private View getsuccessView() {
        View view = View.inflate(this, R.layout.detail, null);
        ButterKnife.bind(this, view);
        llContent.addView(detailMoudle.getView());
        llContent.addView(tagMoudle.getView());
        llContent.addView(screenMoudle.getView());
        llContent.addView(desMoudle.getView());
        detailFrame.addView(downloadMoudle.getView());

        desMoudle.setScrollView(scrollView);
        return view;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadMoudle.removeObserver();
    }

    /**
     * 加载数据
     */
    private void loadData(String packName) {

        HttpHelper.create().get(NetUrl.DETAIL + packName, new HttpHelper.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                stateLayout.showsuccessView();
                Gson json = new Gson();
                AppInfo info = json.fromJson(result, AppInfo.class);
                if (info != null) {
                    detailMoudle.loadData(info);
                    tagMoudle.loadData(info);
                    screenMoudle.loadData(info);
                    desMoudle.loadData(info);
                    downloadMoudle.loadData(info);

                }

            }

            @Override
            public void onFail(Exception e) {

            }
        }, this);
    }


}
