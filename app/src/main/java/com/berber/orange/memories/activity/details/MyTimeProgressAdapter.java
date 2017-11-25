package com.berber.orange.memories.activity.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.berber.orange.memories.R;
import com.berber.orange.memories.widget.TimeLineMarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orange on 2017/11/25.
 */

class MyTimeProgressAdapter extends RecyclerView.Adapter<MyTimeProgressAdapter.MyTimeProgressViewHolder> {
    private Context mContext;
    private List<String> mDataSets;

    public MyTimeProgressAdapter(Context context) {
        mContext = context;
        mDataSets = new ArrayList<>();
        mDataSets.add(MyTimeProgress.TOTAL.toString());
        mDataSets.add(MyTimeProgress.PAST.toString());
        mDataSets.add(MyTimeProgress.REST.toString());
    }

    @Override
    public MyTimeProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_progress, parent, false);
        return new MyTimeProgressViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyTimeProgressViewHolder holder, int position) {
        setPositionType(holder, position);
        setLabelText(holder, position);
    }

    private void setLabelText(MyTimeProgressViewHolder holder, int position) {
        switch (position) {
            case 0:
                //total days
                holder.myTimeProgressLabel.setText("距离你的事件总共还有   200天");
                break;
            case 1:
                //already past days
                holder.myTimeProgressLabel.setText("距离你的时间已经过去了 150天");
                break;
            case 2:
                //rest days
                holder.myTimeProgressLabel.setText("距离你的事件还剩下     50天");
                break;
        }
    }

    private void setPositionType(MyTimeProgressViewHolder holder, int position) {
        if (position == 0) {
            //top
            holder.myTimeProgressDrawable.setBeginLineView(false);
            holder.myTimeProgressDrawable.setEndLineView(true);
        }

        if (position == 1) {
            holder.myTimeProgressDrawable.setBeginLineView(true);
            holder.myTimeProgressDrawable.setEndLineView(true);
        }

        if (position == 2) {
            holder.myTimeProgressDrawable.setBeginLineView(true);
            holder.myTimeProgressDrawable.setEndLineView(false);
        }


    }

    @Override
    public int getItemCount() {
        return mDataSets.size();
    }

    public class MyTimeProgressViewHolder extends RecyclerView.ViewHolder {

        TextView myTimeProgressLabel;
        TimeLineMarker myTimeProgressDrawable;

        public MyTimeProgressViewHolder(View itemView) {
            super(itemView);
            myTimeProgressDrawable = itemView.findViewById(R.id.time_progress_drawable);
            myTimeProgressLabel = itemView.findViewById(R.id.time_progress_label);
        }
    }
}
