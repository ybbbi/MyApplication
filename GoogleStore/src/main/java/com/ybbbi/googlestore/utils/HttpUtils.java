package com.ybbbi.googlestore.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * ybbbi
 * 2019-06-30 11:25
 */
public class HttpUtils {

    private static HttpUtils mInstance = new HttpUtils();
    private OkHttpClient client;
    private Request request;
    private Response response;

    private HttpUtils() {


    }
    public Response execute(String url) {
        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);

        try {
            response = call.execute();
            if(response!=null){
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpUtils getInstance() {
        return mInstance;
    }
}
