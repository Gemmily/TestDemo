package com.gemmily.testdemo.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gemmily.testdemo.R;

import java.util.List;

/**
 * Created by wyg on 2017/1/16.
 */
public class StrAdapter extends BaseAdapter {
    private Context context;
    private List<String> mData;

    public StrAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_text, null);
            viewHolder.textView = (TextView) view.findViewById(R.id.text1);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setGravity(Gravity.CENTER);
        viewHolder.textView.setPadding(0, 20, 0, 20);
        viewHolder.textView.setText(mData.get(i));
        return view;

    }

    public class ViewHolder {
        public TextView textView;
    }
}
