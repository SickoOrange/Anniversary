package com.berber.orange.memories.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.AnniversaryDTO;
import com.berber.orange.memories.dbservice.Anniversary;
import com.berber.orange.memories.dbservice.AnniversaryDao;
import com.berber.orange.memories.model.ItemType;
import com.berber.orange.memories.utils.Utils;
import com.berber.orange.memories.widget.TimeLineMarker;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by z003txeu
 * on 27.09.2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    private List<Anniversary> mDateSets;
    private Context mContext;

    public TimeLineAdapter(List<Anniversary> mDateSets, Context context) {
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
        //get target object
        Anniversary anniversary = mDateSets.get(position);
        holder.mTitle.setText(anniversary.getTitle());

        //set object date
        if (anniversary.getDate() != null) {
            String date = SimpleDateFormat.getDateInstance().format(anniversary.getDate());
            holder.mDate.setText(date.split(",")[0]);
        }


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

    public void addNewItem(AnniversaryDTO dto, TimeLineAdapter adapter) {

        Anniversary newItem = new Anniversary();
        newItem.setTitle(dto.getTitle());
        newItem.setLocation(dto.getLocation());
        newItem.setDescription(dto.getDescription());
        newItem.setDate(dto.getDate());
        newItem.setCreateDate(dto.getCreateDate());
        newItem.setRemindDate(dto.getRemindDate());
        mDateSets.add(newItem);
        adapter.notifyDataSetChanged();
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TimeLineMarker mTimeLine;
        RelativeLayout itemRoot;
        TextView mDate;

        TimeLineViewHolder(View itemView, final int type) {
            super(itemView);


            itemRoot = itemView.findViewById(R.id.item_layout);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);
            mTitle = itemView.findViewById(R.id.item_time_line_txt);
            mDate = itemView.findViewById(R.id.date_label);

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


    public void readTable(AnniversaryDao anniversaryDao) {
        // List<Anniversary> list = anniversaryDao.queryBuilder().list();
        //mDateSets.clear();
        //mDateSets.addAll(list);
    }


}
