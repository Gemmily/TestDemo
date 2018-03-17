package com.gemmily.testdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gemmily.testdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_view)
    TextView tvView;
    @BindView(R.id.tv_viewpager)
    TextView tvViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_view, R.id.tv_viewpager, R.id.tv_delete, R.id.tv_swiper})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_view:
                startActivity( new Intent(this, CustomViewActivity.class));
                break;
            case R.id.tv_viewpager:
                startActivity( new Intent(this, CustomPagerActivity.class));
                break;
            case R.id.tv_delete:
                startActivity(new Intent(this, DeleteViewActivity.class));
                break;
            case R.id.tv_swiper:
                startActivity(new Intent(this, SwiperActivity.class));
                break;
        }
    }
}
