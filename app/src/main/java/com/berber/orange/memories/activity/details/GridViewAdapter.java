package com.berber.orange.memories.activity.details;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.berber.orange.memories.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orange on 2017/11/28.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<Uri> mDatas = new ArrayList<>();
    private Context context;

    public GridViewAdapter(Context context, List<Uri> mDatas) {
        if (mDatas != null) {
            this.mDatas = mDatas;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.isEmpty() ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
            ((ImageView) convertView).setImageURI(mDatas.get(position));
        }
        return convertView;
    }


    public void addNewItems(List<Uri> uris) {
        mDatas.addAll(uris);
        notifyDataSetChanged();
    }
}
