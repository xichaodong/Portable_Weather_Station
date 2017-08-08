package com.sara.com.portable_weather_station.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 17682 on 2017/3/2.
 */

public class HttpUtil {
    public static void senOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
