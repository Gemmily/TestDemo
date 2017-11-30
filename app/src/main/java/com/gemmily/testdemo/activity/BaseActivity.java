package com.gemmily.testdemo.activity;

import android.support.v7.app.AppCompatActivity;

import com.gemmily.network.http.ApiManager;

/**
 * Created by Administrator on 2017/5/19.
 */
public class BaseActivity extends AppCompatActivity {
    protected ApiManager apiManager = ApiManager.getInstance();
}
