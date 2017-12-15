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

    @OnClick({R.id.tv_view, R.id.tv_viewpager})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, CustomViewActivity.class);
        switch (view.getId()) {
            case R.id.tv_view:
                intent.putExtra("type", CustomViewActivity.CUSTOM_VIEW);
                break;
            case R.id.tv_viewpager:
                intent.putExtra("type", CustomViewActivity.CUSTOM_VIEW_PAGER);
                break;
        }
        startActivity(intent);
    }
}
