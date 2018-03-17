package com.gemmily.testdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.gemmily.testdemo.R;
import com.gemmily.testdemo.view.CustomView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gemmily on 2017/12/4.
 */

public class CustomViewActivity extends AppCompatActivity {


    @BindView(R.id.view_custom)
    CustomView viewCustom;
    @BindView(R.id.btn_click)
    Button btnClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_custom_view);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_click)
    public void onViewClicked() {
        viewCustom.startProgressAnimation();
    }
}
