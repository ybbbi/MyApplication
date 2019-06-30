package com.ybbbi.googlestore.moudle;

import android.widget.Button;
import android.widget.ProgressBar;

import com.ybbbi.googlestore.R;
import com.ybbbi.googlestore.bean.AppInfo;
import com.ybbbi.googlestore.bean.DownloadBean;
import com.ybbbi.googlestore.http.DownloadManager;
import com.ybbbi.googlestore.utils.ApkUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ybbbi
 * 2019-06-29 20:01
 */
public class DownloadMoudle extends BaseMoudle<AppInfo> implements DownloadManager.onDownloadListener {
    @BindView(R.id.share_dwn)
    Button shareDwn;
    @BindView(R.id.pb_download)
    ProgressBar pbDownload;
    @BindView(R.id.download_btn)
    Button downloadBtn;
    @BindView(R.id.collect_dwn)
    Button collectDwn;
    private AppInfo info;

    @Override
    public int getLayout() {
        return R.layout.download;
    }

    @Override
    public void update(DownloadBean bean) {
        if (info == null || bean.id != info.id) {
            return;
        }
        switch (bean.state) {
            case DownloadManager.STATE_DOWNLOAD:
                int v = getV(bean);
                downloadBtn.setText(v + "%");
                break;
            case DownloadManager.STATE_ERROR:
                downloadBtn.setText("重新下载");
                break;
            case DownloadManager.STATE_FINISH:
                downloadBtn.setText("安装");
                break;
            case DownloadManager.STATE_NONE:
                downloadBtn.setText("下载");
                break;
            case DownloadManager.STATE_PAUSE:
                downloadBtn.setText("继续下载");
                getV(bean);
                break;
            case DownloadManager.STATE_WAIT:
                downloadBtn.setText("等待中...");
                break;


        }
    }

    private int getV(DownloadBean bean) {
        int v = (int) (bean.currentProgress * 100f / bean.Progress + 0.5);
        downloadBtn.setBackgroundResource(0);
        pbDownload.setProgress(v);
        return v;
    }
    public void removeObserver(){
        DownloadManager.getInstance().removeonDownloadListener(this);
    }
    @Override
    public void loadData(AppInfo info) {
        this.info = info;
        DownloadManager.getInstance().addonDownloadListener(this);
        DownloadBean getdownloadinfo = DownloadManager.getInstance().getdownloadinfo(info);
        if (getdownloadinfo != null) {

            update(getdownloadinfo);
        }

    }

    @OnClick(R.id.download_btn)
    public void onClick() {
        DownloadBean getdownloadinfo = DownloadManager.getInstance().getdownloadinfo(info);
        if (getdownloadinfo == null) {
            DownloadManager.getInstance().download(info);
        } else {
            if (getdownloadinfo.state == DownloadManager.STATE_DOWNLOAD || getdownloadinfo.state == DownloadManager.STATE_WAIT) {
                DownloadManager.getInstance().pause(info);
            } else if (getdownloadinfo.state == DownloadManager.STATE_PAUSE || getdownloadinfo.state == DownloadManager.STATE_ERROR) {
                DownloadManager.getInstance().download(info);

            } else if (getdownloadinfo.state == DownloadManager.STATE_FINISH) {
                ApkUtils.install(getdownloadinfo.path);
            }

        }
    }
}
