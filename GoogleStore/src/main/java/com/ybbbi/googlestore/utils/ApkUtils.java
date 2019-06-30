package com.ybbbi.googlestore.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.ybbbi.googlestore.global.MyApp;

import java.io.File;

/**
 * ybbbi
 * 2019-06-30 10:50
 */
public class ApkUtils {
    public static void install(String path){
        File file =new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        MyApp.context.startActivity(intent);
    }
}
