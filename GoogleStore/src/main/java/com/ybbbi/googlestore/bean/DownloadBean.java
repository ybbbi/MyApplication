package com.ybbbi.googlestore.bean;

import com.ybbbi.googlestore.global.MyApp;
import com.ybbbi.googlestore.http.DownloadManager;

import java.io.File;

/**
 * ybbbi
 * 2019-06-29 16:03
 */
public class DownloadBean {
    public int state;
    public String downloadUrl;
    public long currentProgress;
    public int id;
    public long Progress;
    public String path;
    public static DownloadBean create(AppInfo info){
        DownloadBean bean=new DownloadBean();
        bean.downloadUrl=info.downlaodUrl;
        bean.Progress=info.size;
        bean.id=info.id;
        bean.currentProgress=0;
        bean.state= DownloadManager.STATE_NONE;
        bean.path=DownloadManager.DOWNLOAD_DIR+File.separator+info.name+".apk";
        return bean;

    }
}
