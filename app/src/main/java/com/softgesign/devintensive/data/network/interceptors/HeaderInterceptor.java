package com.softgesign.devintensive.data.network.interceptors;

import com.softgesign.devintensive.data.manegers.DataManager;
import com.softgesign.devintensive.data.manegers.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Пан on 12.07.2016.
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        PreferencesManager pm = DataManager.getInstance().getPreferencesManager();
        Request original=chain.request();
        Request.Builder requestBuilder=original.newBuilder()
                .header("X-Access-Token",pm.getAuthToken())
                .header("Request-User-Id",pm.getUserId())
                .header("User-Agent","DevIntensiveApp")
                .header("Cache-Control","max-age="+(60*60*24));
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
