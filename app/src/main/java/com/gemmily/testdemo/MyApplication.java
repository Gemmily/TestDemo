package com.gemmily.testdemo;

import android.app.Application;
import android.content.Context;

import com.gemmily.network.http.ApiManager;


/**
 * Created by Administrator on 2017/5/16.
 */
public class MyApplication extends Application {
    public static String cookie;
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        ApiManager.getInstance().init(this);
    }
}
