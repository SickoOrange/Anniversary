package com.berber.orange.memories.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.details.DetailsActivity;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.model.db.AnniversaryDao;
import com.berber.orange.memories.model.db.GoogleLocation;
import com.berber.orange.memories.model.db.ModelAnniversaryType;
import com.berber.orange.memories.model.db.NotificationSending;
import com.berber.orange.memories.widget.TimeLineMarker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by z003txeu
 * on 27.09.2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> implements onMoveAndSwipedListener {
    private List<Anniversary> mDateSets = new ArrayList<>();
    private CoordinatorActivity mContext;
    private int calProgress = 0;

    public TimeLineAdapter(List<Anniversary> mDateSets, Context context) {
        if (mDateSets == null) {
            this.mDateSets = new ArrayList<>();
        }
        this.mDateSets = mDateSets;
        mContext = (CoordinatorActivity) context;
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
    public void onBindViewHolder(final TimeLineViewHolder holder, final int position) {
        Log.e("TAG", "onBindViewHolder");
        //get target object
        final Anniversary anniversary = mDateSets.get(position);
        holder.mAnniversaryTitle.setText(anniversary.getTitle());

        String description = anniversary.getDescription();
        if (!TextUtils.isEmpty(description)) {
            holder.mDescriptionLabel.setText(description);
        }else {
            holder.mDescriptionLabel.setText("暂无描述");
        }

        //set object date
        if (anniversary.getDate() != null) {
            String date = SimpleDateFormat.getDateInstance().format(anniversary.getDate());
            holder.mAnniversaryDate.setText(date);
        }

        GoogleLocation googleLocation = anniversary.getGoogleLocation();
        //set location
        String location = null;
        if (googleLocation != null) {
            String locationName = googleLocation.getLocationName();
            String reg = "\\d{1,3}°[0-6]\\d.\\d{3}′";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(locationName);
            if (m.find()) {
                location = googleLocation.getLocationAddress();
            } else {
                location = googleLocation.getLocationName() + ", " + googleLocation.getLocationAddress();
            }
        }
        holder.mPlaceLabel.setText(location == null ? "暂无地点信息" : location);


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


        //calculateDate(holder, anniversary);
        calculateDateWithJoda(holder, anniversary);


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

        final int progress=calProgress;
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Anniversary selectedTarget = mDateSets.get(position);
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("anniversaryId", selectedTarget.getId());
                intent.putExtra("progressValue", progress);
                //intent.putExtra("dateInformation", holder.mLeftDayLabel.getText().toString());
                mContext.startActivity(intent);
            }
        });
    }

    private void calculateDateWithJoda(TimeLineViewHolder holder, Anniversary anniversary) {
        //纪念日时间
        Date anniversaryDate = anniversary.getDate();
        DateTime anniversaryDateWithJoda = new DateTime(anniversaryDate, DateTimeZone.getDefault());
        //纪念日创建时间
        Date createDate = anniversary.getCreateDate();
        DateTime anniversaryCreateDateWithJoda = new DateTime(createDate, DateTimeZone.getDefault());
        //当前时间
        DateTime currentDate = DateTime.now(DateTimeZone.getDefault());

        String totalDayString;
        String restDayString = null;
        String pastDayString;

        int progress = 0;

        if (anniversaryDateWithJoda.isBeforeNow()) {
            //纪念日时间比当前时间要早
            totalDayString = "0 day";
            restDayString = "0 day";
            int days = Days.daysBetween(anniversaryDateWithJoda, currentDate).getDays();
            pastDayString = String.valueOf(days);
            progress = 100;
            holder.mAnniversaryStatusLabel.setText("End");
        } else {
            int totalHour = Hours.hoursBetween(anniversaryCreateDateWithJoda, anniversaryDateWithJoda).getHours();
            int restHour = Hours.hoursBetween(currentDate, anniversaryDateWithJoda).getHours();

            int totalDay = Days.daysBetween(anniversaryCreateDateWithJoda, anniversaryDateWithJoda).getDays();
            int restDay = Days.daysBetween(currentDate, anniversaryDateWithJoda).getDays();


            if (totalDay > 1) {
                totalDayString = String.valueOf(totalDay) + " days";
            } else {
                totalDayString = String.valueOf(totalDay) + " day";
            }


            if (restDay >= 1) {
                pastDayString = String.valueOf(totalDay - restDay);
                progress = (int) ((totalHour - restHour) * 100.0 / totalHour);
                if (restDay < 3) {
                    holder.mAnniversaryStatusLabel.setText("Up Coming");
                } else {
                    holder.mAnniversaryStatusLabel.setText(" ");
                }
                if (restDay == 1) {
                    restDayString = String.valueOf(restDay) + " day";
                } else {
                    restDayString = String.valueOf(restDay) + " days";
                }

            } else if (restDay == 0 && restHour > 0 && restHour < 24) {
                progress = (int) ((totalHour - restHour) * 100.0 / totalHour);
                restDayString = "less than 1 day ";

            } else {
                pastDayString = String.valueOf(restDay * -1);
                progress = 100;
                holder.mAnniversaryStatusLabel.setText("End");
                restDayString = "0 day";
            }
        }


       // String label = restDayString + " / " + totalDayString;
        String label = restDayString;
        holder.mLeftDayLabel.setText(label);
        //holder.mCurrentAnniversaryProgress.setProgress(progress);
        calProgress = progress;
    }


    @Override
    public int getItemCount() {
        if (mDateSets == null) {
            return 0;
        }
        return mDateSets.size();
    }

    public void addNewItem(Anniversary anniversary, AnniversaryDao anniversaryDao) {
        List<Anniversary> list = anniversaryDao.queryBuilder().where(AnniversaryDao.Properties.Id.eq(anniversary.getId())).list();
        if (list.size() == 1) {
            mDateSets.add(list.get(0));
            notifyDataSetChanged();
        }
    }

    public List<Anniversary> getDatas() {
        return mDateSets;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDateSets, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        new MaterialDialog.Builder(mContext)
                .title("警告")
                .content("此次行为将会删除这条纪念日，请在删除前做好备份工作. 是否确定删除")
                .positiveText("OK")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mDateSets.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .show();
        notifyDataSetChanged();
        Log.e("TAG", "onItemDismiss");
    }


    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemRoot;
        CircleImageView mAnniversaryTypeImage;
        TextView mAnniversaryTitle;
        TimeLineMarker mTimeLine;
        TextView mLeftDayLabel;
        TextView mAnniversaryStatusLabel;
        TextView mAnniversaryDate;
       // NumberProgressBar mCurrentAnniversaryProgress;
        ImageView mNotificationIcon;
        TextView mDescriptionLabel;
        TextView mPlaceLabel;

        TimeLineViewHolder(View itemView, final int type) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.item_layout);
            mAnniversaryTypeImage = itemView.findViewById(R.id.anniversary_type_image_view);
            mAnniversaryTitle = itemView.findViewById(R.id.anniversary_title_label);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);
            mLeftDayLabel = itemView.findViewById(R.id.anniversary_left_day);
            mAnniversaryDate = itemView.findViewById(R.id.anniversary_date_label);
            //mCurrentAnniversaryProgress = itemView.findViewById(R.id.anniversary_progress_bar);
            mAnniversaryStatusLabel = itemView.findViewById(R.id.anniversary_status_label);
            mNotificationIcon = itemView.findViewById(R.id.anniversary_notification_icon);
            mDescriptionLabel = itemView.findViewById(R.id.anniversary_description_label);
            mPlaceLabel = itemView.findViewById(R.id.anniversary_place_label);

        }
    }

}
