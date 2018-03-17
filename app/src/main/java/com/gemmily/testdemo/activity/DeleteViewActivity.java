package com.gemmily.testdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.gemmily.testdemo.R;
import com.gemmily.testdemo.adapter.RAdapter;
import com.gemmily.testdemo.listener.OnDeleteListener;
import com.gemmily.testdemo.view.ItemDeleteView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Gemmily on 2018/3/17.
 */

public class DeleteViewActivity extends AppCompatActivity implements OnDeleteListener {

    private ItemDeleteView itemrecycle;
    private RAdapter mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        initView();
        initData();
    }


    private void initView() {
        mList = new ArrayList<>();
        itemrecycle = (ItemDeleteView) findViewById(R.id.item_recycle);
        mAdapter = new RAdapter(this, mList);
        itemrecycle.setLayoutManager(new LinearLayoutManager(this));
        itemrecycle.setAdapter(mAdapter);
        itemrecycle.setOnDeleteListener(this);

    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            mList.add("num: " + i);
        }

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(DeleteViewActivity.this, "" + mList.get(position), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        mAdapter.removeItem(position);
    }
}
