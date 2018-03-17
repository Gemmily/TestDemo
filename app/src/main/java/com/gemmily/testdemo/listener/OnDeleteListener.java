package com.gemmily.testdemo.listener;

import android.view.View;

/**
 * Created by Administrator on 2017/1/17.
 */
public interface OnDeleteListener {

    void onItemClick(View view, int position);

    void onDeleteClick(int position);
}
