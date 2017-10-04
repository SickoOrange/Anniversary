package com.berber.orange.memories.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berber.orange.memories.R;
import com.berber.orange.memories.model.ItemType;
import com.berber.orange.memories.widget.TimeLineMarker;

import java.util.List;

/**
 * Created by z003txeu
 * on 27.09.2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    private List<String> mDateSets;
    private Context mContext;

    public TimeLineAdapter(List<String> mDateSets, Context context) {
        this.mDateSets = mDateSets;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        int size = mDateSets.size() - 1;
        if (size == 0)
            return ItemType.ATOM;
        else if (position == 0)
            return ItemType.START;
        else if (position == size)
            return ItemType.END;
        else return ItemType.NORMAL;

    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TimeLineViewHolder(inflate, viewType);
    }

    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, int position) {
        holder.mTitle.setText(mDateSets.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDateSets.size();
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TimeLineMarker mTimeLine;
        RelativeLayout itemRoot;

        TimeLineViewHolder(View itemView, final int type) {
            super(itemView);

            itemRoot = itemView.findViewById(R.id.item_layout);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);
            mTitle = itemView.findViewById(R.id.item_time_line_txt);

            if (type == ItemType.ATOM) {
                mTimeLine.setBeginLine(null);
                mTimeLine.setEndLine(null);
                mTimeLine.setMarkerDrawable(null);

            } else if (type == ItemType.START) {
                mTimeLine.setBeginLine(null);
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_timeline_marker_now);
                mTimeLine.setMarkerDrawable(drawable);
            } else if (type == ItemType.END) {
                mTimeLine.setEndLine(null);
            }

        }
    }


}
