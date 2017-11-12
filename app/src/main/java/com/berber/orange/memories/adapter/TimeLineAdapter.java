package com.berber.orange.memories.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
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
import com.daimajia.numberprogressbar.NumberProgressBar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        holder.mAnniversaryTitle.setText(anniversary.getTitle());

        //set object date
        if (anniversary.getDate() != null) {
            String date = SimpleDateFormat.getDateInstance().format(anniversary.getDate());
            //holder.mDate.setText(date.split(",")[0]);
        }

        //calculate left date
        long restMillis = anniversary.getDate().getTime() - System.currentTimeMillis();
        long restDays = restMillis / (24 * 60 * 60 * 1000);
        if (restDays >= 0) {
            //  holder.mLeftDay.setText("+ " + restDays);
        } else {
            //   holder.mLeftDay.setText("- " + restDays);

        }

//        ValueAnimator animator = ValueAnimator.ofInt(0, 70);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                Log.e("TAG", "value animator " + valueAnimator.getAnimatedValue());
//                int progress = (int) valueAnimator.getAnimatedValue();
//                holder.mCurrentAnniversaryProgress.setProgress(progress);
//            }
//        });
//        animator.setRepeatMode(ValueAnimator.INFINITE);
//        animator.setDuration(2000);
//        animator.start();
        holder.mCurrentAnniversaryProgress.setProgress(50);


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
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemRoot;
        CircleImageView mAnniversaryTypeImage;
        TextView mAnniversaryTitle;
        TimeLineMarker mTimeLine;

        TextView mAnniversaryDate;
        TextView mAnniversaryNotificationDate;

        NumberProgressBar mCurrentAnniversaryProgress;

        TimeLineViewHolder(View itemView, final int type) {
            super(itemView);


            itemRoot = itemView.findViewById(R.id.item_layout);
            mAnniversaryTypeImage = itemView.findViewById(R.id.anniversary_type_image);
            mAnniversaryTitle = itemView.findViewById(R.id.anniversary_title_label);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);

            mAnniversaryDate = itemView.findViewById(R.id.anniversary_date_label);
            mAnniversaryNotificationDate = itemView.findViewById(R.id.anniversary_notification_date_label);
            mCurrentAnniversaryProgress = itemView.findViewById(R.id.anniversary_progress_bar);


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
