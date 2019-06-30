package com.ybbbi.googlestore.global;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.DbManager;
import org.xutils.x;

/*
 *ybbbi
 *2019-06-19 17:36
 */public class MyApp extends Application {

     public static Context context;
        public static DbManager db;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        context = getApplicationContext();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        //Log.i("MyApp", "onCreate: myapp");

    }

}
