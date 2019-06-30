package com.ybbbi.googlestore.utils;

import android.content.Intent;
import android.net.Uri;

import com.ybbbi.googlestore.global.MyApp;

/**
 * ybbbi
 * 2019-06-30 10:50
 */
public class ApkUtils {
    public static void install(String path){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://"+path),"application/vnd.android.package-archive");
        MyApp.context.startActivity(intent);
    }
}
