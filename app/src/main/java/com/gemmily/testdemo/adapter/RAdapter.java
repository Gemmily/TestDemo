package com.gemmily.testdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.gemmily.testdemo.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */
public class RAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> mData;

    public RAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RViewHolder(LayoutInflater.from(context).inflate(R.layout.item_delete, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RViewHolder viewHolder = (RViewHolder) holder;
        viewHolder.content.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
}
