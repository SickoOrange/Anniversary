package com.berber.orange.memories.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berber.orange.memories.R;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.model.db.AnniversaryDao;
import com.berber.orange.memories.model.db.GoogleLocation;
import com.berber.orange.memories.model.db.ModelAnniversaryType;
import com.berber.orange.memories.model.db.NotificationSending;
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

        //get target object
        final Anniversary anniversary = mDateSets.get(position);
        holder.mAnniversaryTitle.setText(anniversary.getTitle() + " " + position);

        //set object date
        if (anniversary.getDate() != null) {
            String date = SimpleDateFormat.getDateInstance().format(anniversary.getDate());
            //holder.mDate.setText(date.split(",")[0]);
           // holder.mAnniversaryDate.setText(date + ", Germany Nurnberg");
            //set location
            GoogleLocation googleLocation = anniversary.getGoogleLocation();
            if (googleLocation != null) {
                holder.mAnniversaryDate.setText(date + ", " + googleLocation.getLocationName());
            } else {
                holder.mAnniversaryDate.setText(date);
            }
        }

        //set image type
        ModelAnniversaryType modelAnniversaryType = anniversary.getModelAnniversaryType();
        if (modelAnniversaryType != null) {
            holder.mAnniversaryTypeImage.setImageResource(modelAnniversaryType.getImageResource());
        }

        //notification icon switch
        NotificationSending notificationSending = anniversary.getNotificationSending();
        if (notificationSending != null) {
            holder.mNotificationIcon.setImageResource(R.drawable.ic_notifications_black_18px);
        } else {
            holder.mNotificationIcon.setImageResource(R.drawable.ic_notifications_off_black_18px);
        }

        //notification date for anniversary
        // Date sendingDate = anniversary.getNotificationSending().getSendingDate();


        //calculate left date progress
        Date createDate = anniversary.getCreateDate();
        Date anniversaryShowDate = anniversary.getDate();
        Log.d("TAG", "Anniversary show date " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(anniversaryShowDate));
        Log.d("TAG", "Anniversary created date " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createDate));
        Log.d("TAG", "Anniversary current date " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        // Log.e("TAG", "Anniversary notification date " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(sendingDate));

        long currentTimeMillis = System.currentTimeMillis();
        long currentRestMillis = anniversaryShowDate.getTime() - currentTimeMillis;
        long totalRestMillis = anniversaryShowDate.getTime() - createDate.getTime();


        if (totalRestMillis < 0 && totalRestMillis > (-1 * 24 * 60 * 60 * 1000)) {
            //当天的情况下 (纪念日时间默认当然00:00)
            holder.mLeftDayLabel.setText("0/0");
            holder.mCurrentAnniversaryProgress.setProgress(100);
            holder.mAnniversaryStatusLabel.setText("In Progress");
            holder.mAnniversaryStatusLabel.setBackground(null);

        } else if (totalRestMillis <= (-1 * 24 * 60 * 60 * 1000)) {
            //已经结束了
            double totalDay = totalRestMillis * 1.0 / (24 * 60 * 60 * 1000);
            if (totalDay < 0) {
                totalDay = totalDay - 1;
                holder.mLeftDayLabel.setText("0/" + (long) totalDay);
            }
            holder.mCurrentAnniversaryProgress.setProgress(100);
            holder.mAnniversaryStatusLabel.setText("Finish");
            holder.mAnniversaryStatusLabel.setBackground(null);

        } else {
            //没有结束的
            double restDays = currentRestMillis * 1.0 / (24 * 60 * 60 * 1000);
            double totalDay = totalRestMillis * 1.0 / (24 * 60 * 60 * 1000);

            if (restDays > 0) {
                restDays = restDays + 1;
            }

            if (totalDay > 0) {
                totalDay = totalDay + 1;
            }

            holder.mLeftDayLabel.setText((long) restDays + "/" + (long) totalDay);
            int progress = (int) (currentRestMillis * 100.0 / totalRestMillis);
            holder.mCurrentAnniversaryProgress.setProgress(100 - progress);
            if ((long) restDays < 7) {
                holder.mAnniversaryStatusLabel.setText("Up Coming");
                holder.mAnniversaryStatusLabel.setBackground(null);
            } else {
                holder.mAnniversaryStatusLabel.setText("");
                holder.mAnniversaryStatusLabel.setBackground(null);
            }
        }

        if (position == 0)

        {
            holder.mTimeLine.setBeginLineView(false);
            holder.mTimeLine.setEndLineView(true);
        } else if (position == mDateSets.size() - 1)

        {
            holder.mTimeLine.setBeginLineView(true);
            holder.mTimeLine.setEndLineView(false);
        } else

        {
            holder.mTimeLine.setBeginLineView(true);
            holder.mTimeLine.setEndLineView(true);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener()

        {
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
        NumberProgressBar mCurrentAnniversaryProgress;
        ImageView mNotificationIcon;

        TimeLineViewHolder(View itemView, final int type) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.item_layout);
            mAnniversaryTypeImage = itemView.findViewById(R.id.anniversary_type_image_view);
            mAnniversaryTitle = itemView.findViewById(R.id.anniversary_title_label);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);
            mLeftDayLabel = itemView.findViewById(R.id.anniversary_left_day);
            mAnniversaryDate = itemView.findViewById(R.id.anniversary_date_label);
            mCurrentAnniversaryProgress = itemView.findViewById(R.id.anniversary_progress_bar);
            mAnniversaryStatusLabel = itemView.findViewById(R.id.anniversary_status_label);
            mNotificationIcon = itemView.findViewById(R.id.anniversary_notification_icon);

        }
    }

}
