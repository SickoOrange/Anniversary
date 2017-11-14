package com.berber.orange.memories.adapter;

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
import com.daimajia.numberprogressbar.NumberProgressBar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

//    @Override
//    public int getItemViewType(int position) {
//        Log.e("TAG", "getItemViewType" + position + "  " + mDateSets.size());
//
////        int size = mDateSets.size() - 1;
////        if (size == 0)
////            return ItemType.ATOM;
////        else if (position == 0)
////            return ItemType.START;
////        else if (position == size)
////            return ItemType.END;
//        return ItemType.NORMAL;
//       // return 0;
//    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TimeLineViewHolder(inflate, viewType);
    }

    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, int position) {
        Log.e("TAG", "onBindViewHolder" + position);

        //get target object
        final Anniversary anniversary = mDateSets.get(position);
        holder.mAnniversaryTitle.setText(anniversary.getTitle() + " " + position);

        //set object date
        if (anniversary.getDate() != null) {
            String date = SimpleDateFormat.getDateInstance().format(anniversary.getDate());
            //holder.mDate.setText(date.split(",")[0]);
            holder.mAnniversaryDate.setText(date);
        }

        //calculate left date progress
        Date createDate = anniversary.getCreateDate();
        Date anniversaryShowDate = anniversary.getDate();
        long currentTimeMillis = System.currentTimeMillis();


        long currentRestMillis = anniversaryShowDate.getTime() - currentTimeMillis;
        long totalRestMillis = anniversaryShowDate.getTime() - createDate.getTime();
//
        long restDays = currentRestMillis / (24 * 60 * 60 * 1000);
        long totalDay = totalRestMillis / (24 * 60 * 60 * 1000);

        System.out.println(totalDay + " " + restDays);
        int progress = (int) (restDays * 100.0 / totalDay);
        holder.mCurrentAnniversaryProgress.setProgress(progress);


//        if (restDays >= 0) {
//            holder.mCurrentAnniversaryProgress.setProgress((int) (restDays * 100 / totalDay));
//            //  holder.mLeftDay.setText("+ " + restDays);
//        }

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


//        if (position == 0) {
//            // holder.mTimeLine.setBeginLine(null);
//            holder.mTimeLine.setBeginLine(null);
//        } else if (getItemViewType(position) == ItemType.START) {
//            //holder.mTimeLine.setBeginLine(null);
//            //Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_timeline_marker_now);
//            // holder.mTimeLine.setMarkerDrawable(drawable);
//        } else if (position == mDateSets.size() - 1) {
//            //holder.mTimeLine.setEndLine(null);
//        } else {
//        }

        if (position == 0) {
            holder.mTimeLine.setBeginLineView(false);
            holder.mTimeLine.setEndLineView(true);
        } else if (position == mDateSets.size() - 1) {
            holder.mTimeLine.setBeginLineView(true);
            holder.mTimeLine.setEndLineView(false);
        } else {
            holder.mTimeLine.setBeginLineView(true);
            holder.mTimeLine.setEndLineView(true);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/11/14 open details page
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

    public void addNewItem(Anniversary anniversary, AnniversaryDao anniversaryDao) {
        //mDateSets.clear();
        //mDateSets.addAll(anniversaryDao.queryBuilder().list());
        //notifyDataSetChanged();

        // mDateSets.add(5,anniversary);
        // notifyItemInserted(5);
        //notifyItemRangeChanged(5,mDateSets.size()-5);
        mDateSets.add(anniversary);
        notifyDataSetChanged();
    }

    public List<Anniversary> getDatas() {
        return mDateSets;
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


        }
    }

}
