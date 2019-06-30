package com.ybbbi.googlestore.bean;

import com.ybbbi.googlestore.global.MyApp;
import com.ybbbi.googlestore.http.DownloadManager;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.File;

/**
 * ybbbi
 * 2019-06-29 16:03
 */
    @Table(name = "ybbbi")
public class DownloadBean {
    @Column(name="state")
    public int state;
    @Column(name="downloadUrl")
    public String downloadUrl;
    @Column(name="currentProgress")
    public long currentProgress;
    @Column(name="id",autoGen = false,isId = true)
    public int id;
    @Column(name="Progress")
    public long Progress;
    @Column(name="path")
    public String path;
    public static DownloadBean create(AppInfo info){
        DownloadBean bean=new DownloadBean();
        bean.downloadUrl=info.downloadUrl;
        bean.Progress=info.size;
        bean.id=info.id;
        bean.currentProgress=0;
        bean.state= DownloadManager.STATE_NONE;
        bean.path=DownloadManager.DOWNLOAD_DIR+File.separator+info.name+".apk";
        return bean;

    }
}
