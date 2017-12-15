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
    public static final String CUSTOM_VIEW = "CUSTOM_VIEW";
    public static final String CUSTOM_VIEW_PAGER = "CUSTOM_VIEW_PAGER";
    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.view_custom)
    CustomView viewCustom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = getIntent().getStringExtra("type");
        switch (type) {
            case CUSTOM_VIEW:
                setContentView(R.layout.view_custom_view);
                break;
            case CUSTOM_VIEW_PAGER:
                setContentView(R.layout.view_custom_view_pager);
                break;
        }
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_click)
    public void onViewClicked() {
        viewCustom.startProgressAnimation();
    }
}
