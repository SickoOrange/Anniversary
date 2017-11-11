package com.berber.orange.memories.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.model.AnniversaryDTO;
import com.berber.orange.memories.dbservice.Anniversary;
import com.berber.orange.memories.dbservice.AnniversaryDao;
import com.berber.orange.memories.model.ItemType;
import com.berber.orange.memories.widget.TimeLineMarker;
import com.dinuscxj.progressbar.CircleProgressBar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z003txeu
 * on 27.09.2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    private List<Anniversary> mDateSets;
    private Context mContext;

    public TimeLineAdapter(List<Anniversary> mDateSets, Context context) {
        if (mDateSets == null) {
            this.mDateSets = new ArrayList<>();
        }
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

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, int position) {
        //get target object
        final Anniversary anniversary = mDateSets.get(position);
        holder.mTitle.setText(anniversary.getTitle());

        //set object date
        if (anniversary.getDate() != null) {
            String date = SimpleDateFormat.getDateInstance().format(anniversary.getDate());
            holder.mDate.setText(date.split(",")[0]);
        }

        //calculate left date
        long restMillis = anniversary.getDate().getTime() - System.currentTimeMillis();
        long restDays = restMillis / (24 * 60 * 60 * 1000);
        if (restDays >= 0) {
            holder.mLeftDay.setText("+ " + restDays);
        } else {
            holder.mLeftDay.setText("- " + restDays);

        }

        ValueAnimator animator = ValueAnimator.ofInt(0, 70);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.e("TAG", "value animator " + valueAnimator.getAnimatedValue());
                int progress = (int) valueAnimator.getAnimatedValue();
                holder.currentAnniversaryProgress.setProgress(progress);
            }
        });
        animator.setRepeatMode(ValueAnimator.INFINITE);
        animator.setDuration(2000);
        animator.start();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mDateSets == null) {
            return 0;
        }
        return mDateSets.size();
    }

    public void addNewItem(AnniversaryDTO dto, TimeLineAdapter adapter) {

        Anniversary newItem = new Anniversary();
        newItem.setTitle(dto.getTitle());
        newItem.setLocation(dto.getLocation());
        newItem.setDescription(dto.getDescription());
        newItem.setDate(dto.getDate());
        newItem.setCreateDate(dto.getCreateDate());
        // newItem.setRemindDate(dto.getRemindDate());
        mDateSets.add(newItem);
        adapter.notifyDataSetChanged();
        //adapter.notifyItemChanged(mDateSets.size()-1);
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        TextView mLeftDay;
        TextView mTitle;
        TimeLineMarker mTimeLine;
        RelativeLayout itemRoot;
        TextView mDate;
        CircleProgressBar currentAnniversaryProgress;

        TimeLineViewHolder(View itemView, final int type) {
            super(itemView);


            itemRoot = itemView.findViewById(R.id.item_layout);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);
            mTitle = itemView.findViewById(R.id.item_time_line_txt);
            mDate = itemView.findViewById(R.id.anniversary_add_anni_date);
            mLeftDay = itemView.findViewById(R.id.left_day_label);
            currentAnniversaryProgress = itemView.findViewById(R.id.custom_progress1);

            if (type == ItemType.ATOM) {
                mTimeLine.setBeginLine(null);
                mTimeLine.setEndLine(null);
                mTimeLine.setMarkerDrawable(null);

            } else if (type == ItemType.START) {
                mTimeLine.setBeginLine(null);
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_timeline_marker_now);
                mTimeLine.setMarkerDrawable(mContext.getResources().getDrawable(R.mipmap.ic_category_3));
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
