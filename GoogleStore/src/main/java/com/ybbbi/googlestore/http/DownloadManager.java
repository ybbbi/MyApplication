package com.ybbbi.googlestore.http;

import android.util.SparseArray;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ybbbi.googlestore.NetURL.NetUrl;
import com.ybbbi.googlestore.bean.AppInfo;
import com.ybbbi.googlestore.bean.DownloadBean;
import com.ybbbi.googlestore.global.MyApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ybbbi
 * 2019-06-29 15:08
 */
public class DownloadManager {
    ArrayList<onDownloadListener> list = new ArrayList<>();
    SparseArray<DownloadBean> sparseArray = new SparseArray<>();
    public static final String DOWNLOAD_DIR = "data/data/" + MyApp.context.getPackageName() + File.separator + "download";
    public static final int STATE_NONE = 0;
    public static final int STATE_DOWNLOAD = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_FINISH = 3;
    public static final int STATE_WAIT = 4;
    public static final int STATE_ERROR = 5;


    //线程池下载 核数*2+1

    ThreadPoolExecutor executor = null;
    private static DownloadManager mInstance = new DownloadManager();

    private DownloadManager() {
        int thread = Runtime.getRuntime().availableProcessors() * 2 + 1;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(thread);
        File file = new File(DOWNLOAD_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static DownloadManager getInstance() {
        return mInstance;
    }

    public void download(AppInfo info) {
        DownloadBean bean = sparseArray.get(info.id);
        if (bean == null) {
            bean = DownloadBean.create(info);
            sparseArray.put(bean.id, bean);
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
     * 通知所有监听器传递更新
     *
     * @param bean
     */
    private void notifyUpdate(DownloadBean bean) {
        for (onDownloadListener listener : list) {
            listener.update(bean);
        }
    }

    protected class DownloadTask implements Runnable {
        DownloadBean bean;
        OkHttpClient client;
        private InputStream inputStream;
        private FileOutputStream fos;

        public DownloadTask(DownloadBean bean) {
            client = new OkHttpClient();
            this.bean = bean;
        }

        @Override
        public void run() {
            bean.state = STATE_DOWNLOAD;
            notifyUpdate(bean);

            File file = new File(bean.path);
            if (!file.exists() || file.length() != bean.currentProgress) {
                bean.currentProgress = 0;
                file.delete();
                String url = String.format(NetUrl.URL_DOWNLOAD, bean.downloadUrl);
                Request request = new Request.Builder().url(url).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    inputStream = response.body().byteStream();
                    if (response.isSuccessful()) {
                        if (response != null && inputStream != null) {
                            fos = new FileOutputStream(file, true);
                            byte b[] = new byte[1024 * 8];
                            int len = -1;
                            while ((len = inputStream.read(b)) != -1&&bean.state==STATE_DOWNLOAD) {
                                fos.write(b, 0, len);
                                bean.currentProgress += len;
                                bean.state = STATE_DOWNLOAD;
                                notifyUpdate(bean);
                            }
                        } else {
                            bean.state = STATE_ERROR;
                            notifyUpdate(bean);

                        }

                    } else {
                        bean.state = STATE_ERROR;
                        notifyUpdate(bean);
                    }
                } catch (IOException e) {
                    bean.state = STATE_ERROR;
                    notifyUpdate(bean);
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null && fos != null)
                            inputStream.close();
                        fos.close();
                    } catch (IOException e) {
                        bean.state = STATE_ERROR;
                        notifyUpdate(bean);
                        e.printStackTrace();
                    }if(file.length()==bean.Progress&&bean.state==STATE_DOWNLOAD){
                        bean.state=STATE_FINISH;
                        notifyUpdate(bean);
                    }else if(bean.state==STATE_PAUSE){
                        notifyUpdate(bean);
                    }
                }

            }else{
                bean.state = STATE_ERROR;
                notifyUpdate(bean);
            }
        }
    }

    public void pause(AppInfo info) {
        DownloadBean bean = sparseArray.get(info.id);
        if(bean!=null){
            bean.state=STATE_PAUSE;
            notifyUpdate(bean);
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
