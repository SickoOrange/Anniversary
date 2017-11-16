package com.berber.orange.memories.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berber.orange.memories.R;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.model.db.AnniversaryDao;
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
    private List<Anniversary> mDateSets = new ArrayList<>();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            holder.mAnniversaryDate.setText(date + ", Germany Nurnberg");
        }

        //set image type
        holder.mAnniversaryTypeImage.setImageResource(anniversary.getModelAnniversaryType().getImageResource());

        //notification date
        Date sendingDate = anniversary.getNotificationSending().getSendingDate();

        //calculate left date progress
        Date createDate = anniversary.getCreateDate();
        Date anniversaryShowDate = anniversary.getDate();
        Log.e("TAG", "Anniversary show date " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(anniversaryShowDate));
        Log.e("TAG", "Anniversary created date " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(createDate));
        Log.e("TAG", "Anniversary current date " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
        Log.e("TAG", "Anniversary notification date " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(sendingDate));

        long currentTimeMillis = System.currentTimeMillis();


        long currentRestMillis = anniversaryShowDate.getTime() - currentTimeMillis;
        long totalRestMillis = anniversaryShowDate.getTime() - createDate.getTime();
        long restDays = currentRestMillis / (24 * 60 * 60 * 1000);
        long totalDay = totalRestMillis / (24 * 60 * 60 * 1000);

        //set anniversary status
        if (restDays <= (long) 20 && restDays >= 0) {
            holder.mAnniversaryStatusLabel.setText("Up Coming");
            holder.mAnniversaryStatusLabel.setBackground(mContext.getResources().getDrawable(R.drawable.dot_text_view));
        } else if (restDays < 0) {
            holder.mAnniversaryStatusLabel.setText("Finish");
            holder.mAnniversaryStatusLabel.setBackground(mContext.getResources().getDrawable(R.drawable.dot_text_view));
        } else {
            holder.mAnniversaryStatusLabel.setText("");
            holder.mAnniversaryStatusLabel.setBackground(null);
        }


        if (totalDay == 0) {
            String label = "0/0";
            holder.mLeftDayLabel.setText(label);
            holder.mCurrentAnniversaryProgress.setProgress(100);
        } else if (totalDay == restDays) {
            String label = restDays + "/" + totalDay;
            holder.mLeftDayLabel.setText(label);
            int progress = (int) (currentRestMillis * 100.0 / totalRestMillis);
            holder.mCurrentAnniversaryProgress.setProgress(100 - progress);
        } else {
            String label = restDays + "/" + totalDay;
            holder.mLeftDayLabel.setText(label);
            int progress = (int) (currentRestMillis * 100.0 / totalRestMillis);
            holder.mCurrentAnniversaryProgress.setProgress(100 - progress);
        }


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
        //mDateSets.add(anniversary);
        List<Anniversary> list = anniversaryDao.queryBuilder().where(AnniversaryDao.Properties.Id.eq(anniversary.getId())).list();
        if (list.size() == 1) {
            mDateSets.add(list.get(0));
            notifyDataSetChanged();
        }
    }

    public List<Anniversary> getDatas() {
        return mDateSets;
    }


    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemRoot;
        CircleImageView mAnniversaryTypeImage;
        TextView mAnniversaryTitle;
        TimeLineMarker mTimeLine;
        TextView mLeftDayLabel;
        TextView mAnniversaryStatusLabel;

        TextView mAnniversaryDate;
        //  TextView mAnniversaryNotificationDate;

        NumberProgressBar mCurrentAnniversaryProgress;

        TimeLineViewHolder(View itemView, final int type) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.item_layout);
            mAnniversaryTypeImage = itemView.findViewById(R.id.anniversary_type_image_view);
            mAnniversaryTitle = itemView.findViewById(R.id.anniversary_title_label);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);
            mLeftDayLabel = itemView.findViewById(R.id.anniversary_left_day);

            mAnniversaryDate = itemView.findViewById(R.id.anniversary_date_label);
            //  mAnniversaryNotificationDate = itemView.findViewById(R.id.anniversary_notification_date_label);
            mCurrentAnniversaryProgress = itemView.findViewById(R.id.anniversary_progress_bar);
            mAnniversaryStatusLabel = itemView.findViewById(R.id.anniversary_status_label);


        }
    }

}
