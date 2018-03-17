package com.gemmily.testdemo.activity;

/**
 * Created by Gemmily on 2018/3/17.
 */

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.gemmily.testdemo.R;
import com.gemmily.testdemo.adapter.StrAdapter;
import com.gemmily.testdemo.view.SwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

public class SwiperActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshView.OnLoadListener {

    private android.widget.ListView list;
    private SwipeRefreshView swipelayout;
    private List<String> mList;
    private StrAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        swipelayout = (SwipeRefreshView) findViewById(R.id.swipe_layout);
        list = (ListView) findViewById(R.id.list);
        mList = new ArrayList<>();
        mAdapter = new StrAdapter(this, mList);
        list.setAdapter(mAdapter);
        swipelayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipelayout.setOnRefreshListener(this);
        swipelayout.setOnLoadListener(this);
        initData(0);
    }

    private void initData(int num) {
        for (int i = num; i < num + 30; i++) {
            mList.add("num:" + i);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                initData(0);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(SwiperActivity.this, "刷新数据", Toast.LENGTH_SHORT).show();
                swipelayout.setRefreshing(false);
            }
        }, 1200);
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData(mList.size());
                mAdapter.notifyDataSetChanged();
                Toast.makeText(SwiperActivity.this, "加载数据", Toast.LENGTH_SHORT).show();
                swipelayout.setLoading(false);

            }
        }, 1200);

    }
}

