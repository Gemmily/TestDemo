package com.gemmily.testdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gemmily.testdemo.R;

import butterknife.ButterKnife;

/**
 * Created by Gemmily on 2018/3/17.
 */

public class CustomPagerActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_custom_view_pager);
        ButterKnife.bind(this);
    }
}
