package com.ybbbi.googlestore.http;

import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;

import com.squareup.okhttp.Response;
import com.ybbbi.googlestore.NetURL.NetUrl;
import com.ybbbi.googlestore.bean.AppInfo;
import com.ybbbi.googlestore.bean.DownloadBean;
import com.ybbbi.googlestore.global.MyApp;
import com.ybbbi.googlestore.utils.HttpUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ybbbi
 * 2019-06-29 15:08
 */
public class DownloadManager {
    ArrayList<onDownloadListener> list = new ArrayList<>();
    SparseArray<DownloadBean> sparseArray = new SparseArray<>();
    public static final String DOWNLOAD_DIR = "data/data/"
            + MyApp.context.getPackageName()
            + File.separator + "download";
    public static final int STATE_NONE = 0;
    public static final int STATE_DOWNLOAD = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_FINISH = 3;
    public static final int STATE_WAIT = 4;
    public static final int STATE_ERROR = 5;
    Handler handler = new Handler();

    //线程池下载 核数*2+1

    ThreadPoolExecutor executor = null;
    private static DownloadManager mInstance = new DownloadManager();
    DbManager db;


    private DownloadManager() {
        DbManager.DaoConfig config =  new DbManager.DaoConfig()
                .setDbName("ybbbi");
        db= x.getDb(config);


        initDownloadinfo();
        int thread = Runtime.getRuntime().availableProcessors() * 2 + 1;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(thread);
        File file = new File(DOWNLOAD_DIR);
        if (!file.exists()) {
            file.mkdirs();

        }
    }

    private void initDownloadinfo() {
        try {
            List<DownloadBean> downloadBeans =db.findAll(DownloadBean.class);
            if (downloadBeans != null && downloadBeans.size() >0) {
                for (DownloadBean bean : downloadBeans) {
                    sparseArray.put(bean.id, bean);
                }
            Log.i("DownloadManager", "initDownloadinfo: 查询"+downloadBeans.size()+"条数据");
            }else{
                Log.i("DownloadManager", "initDownloadinfo: 无数据");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static DownloadManager getInstance() {
        return mInstance;
    }

    public DownloadBean getdownloadinfo(AppInfo info) {
        return sparseArray.get(info.id);

    }

    public void download(AppInfo info) {
        DownloadBean bean = sparseArray.get(info.id);
        if (bean == null) {
            bean = DownloadBean.create(info);
            sparseArray.put(bean.id, bean);
            saveDownloadinfo(bean);
        }
        int state = bean.state;
        if (state == STATE_NONE || state == STATE_PAUSE || state == STATE_ERROR) {
            DownloadTask downloadTask = new DownloadTask(bean);
            bean.state = STATE_WAIT;
            notifyUpdate(bean);
            executor.execute(downloadTask);
        }

    }

    /**
     * 存储已下载App信息
     */
    private void saveDownloadinfo(DownloadBean bean) {
        try {
            db.save(bean);
            Log.i("DownloadManager", "saveDownloadinfo: 存储成功");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知所有监听器传递更新
     *
     * @param bean
     */
    private void notifyUpdate(final DownloadBean bean) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                for (onDownloadListener listener : list) {
                    listener.update(bean);
                }

            }
        });
    }

    protected class DownloadTask implements Runnable {
        DownloadBean bean;



        public DownloadTask(DownloadBean bean) {

            this.bean = bean;
        }

        @Override
        public void run() {
        InputStream inputStream=null;
         FileOutputStream fos=null;
         Response response;
            bean.state = STATE_DOWNLOAD;
            notifyUpdate(bean);
            updateDownloadinfo(bean);

            File file = new File(bean.path);

            if (!file.exists() || file.length() != bean.currentProgress) {
                bean.currentProgress = 0;
                file.delete();
                String url = String.format(NetUrl.URL_DOWNLOAD, bean.downloadUrl);
                response = HttpUtils.getInstance().execute(url);


            } else {
                String url = String.format(NetUrl.URL_DOWNLOAD_BREAK, bean.downloadUrl, bean.currentProgress);
                response = HttpUtils.getInstance().execute(url);

            }

            if (!response.isSuccessful()) {


                return;
            }
            try {
                if (response != null && (response.body().byteStream()) != null) {
                    try {
                         inputStream =response.body().byteStream();
                        fos = new FileOutputStream(file, true);
                        byte b[] = new byte[1024 * 8];
                        int len = -1;
                        while ((len = inputStream.read(b)) != -1 && bean.state == STATE_DOWNLOAD) {
                            fos.write(b, 0, len);
                            bean.currentProgress += len;

                            notifyUpdate(bean);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                       /* bean.state = STATE_ERROR;
                        notifyUpdate(bean);*/
                    } finally {
                        inputStream.close();
                        try {
                            if ( fos != null) {

                                fos.close();
                            }
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                        if (file.length() == bean.Progress && bean.state == STATE_DOWNLOAD) {
                            bean.state = STATE_FINISH;
                            notifyUpdate(bean);
                        } else if (bean.state == STATE_PAUSE) {
                            notifyUpdate(bean);
                        }
                        updateDownloadinfo(bean);

                    }
                } else {
                    bean.state = STATE_ERROR;
                    notifyUpdate(bean);

                }
            } catch (IOException e) {

                e.printStackTrace();
            }


        }
    }

    /**
     * 更新app下载信息
     */
    private void updateDownloadinfo(DownloadBean bean) {
        try {
          db.update(bean,"currentProgress","state");
           Log.i("DownloadManager", "updateDownloadinfo: 更新成功");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void pause(AppInfo info) {
        DownloadBean bean = sparseArray.get(info.id);
        if (bean != null) {
            bean.state = STATE_PAUSE;

        }

    }

    public interface onDownloadListener {
        void update(DownloadBean bean);
    }

    public void addonDownloadListener(onDownloadListener listener) {
        if (!list.contains(listener)) {
            list.add(listener);
        }
    }

    public void removeonDownloadListener(onDownloadListener listener) {
        if (!list.contains(listener)) {
            list.remove(listener);
        }
    }

}
