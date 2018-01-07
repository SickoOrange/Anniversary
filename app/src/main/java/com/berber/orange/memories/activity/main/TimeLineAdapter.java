package com.berber.orange.memories.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.details.DetailsActivity;
import com.berber.orange.memories.activity.model.ModelAnniversaryTypeDTO;
import com.berber.orange.memories.database.FirebaseDatabaseHelper;
import com.berber.orange.memories.database.ItemType;
import com.berber.orange.memories.database.firebasemodel.AnniversaryModel;
import com.berber.orange.memories.database.firebasemodel.GoogleLocationModel;
import com.berber.orange.memories.widget.TimeLineMarker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by z003txeu
 * on 27.09.2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements onMoveAndSwipedListener {
    private List<Object> mDateSets = new ArrayList<>();
    private CoordinatorActivity mContext;
    private int calProgress = 0;
    private Map<ItemType, List<Object>> itemTypeListMap;

    TimeLineAdapter(Context context) {
        mContext = (CoordinatorActivity) context;
        FirebaseDatabaseHelper.getInstance().getAnniversariesReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("TAG", "onChildAdded: child add " + dataSnapshot.getKey());
                AnniversaryModel value = dataSnapshot.getValue(AnniversaryModel.class);
                value.setAnniuuid(dataSnapshot.getKey());
                addItem(value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("TAG", "onChildChanged: child update " + s);
                String key = dataSnapshot.getKey();
                AnniversaryModel value = dataSnapshot.getValue(AnniversaryModel.class);
                value.setAnniuuid(key);
                updateItem(value);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                AnniversaryModel value = dataSnapshot.getValue(AnniversaryModel.class);
                value.setAnniuuid(dataSnapshot.getKey());
                Log.e("TAG", "onChildRemoved: remove child " + dataSnapshot.getKey());
                removeItem(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemViewType(int position) {
        Object o = mDateSets.get(position);
        if (o instanceof String) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateForItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        View inflateForDate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        if (viewType == 0) {
            return new TimeLineViewHolder(inflateForItem);
        } else {
            return new TimeLineDateViewHolder(inflateForDate);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TimeLineDateViewHolder) {
            DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime((String) mDateSets.get(position)).withZone(DateTimeZone.getDefault());
            String formatString = dateTime.toString(DateTimeFormat.longDate());
            TimeLineDateViewHolder dateViewHolder = (TimeLineDateViewHolder) holder;
            dateViewHolder.itemDateTextView.setText(dateTime.toString("MMM yyyy"));
        } else if (holder instanceof TimeLineViewHolder) {
            //get target object
            TimeLineViewHolder viewHolder = (TimeLineViewHolder) holder;
            final AnniversaryModel anniversaryModel = (AnniversaryModel) mDateSets.get(position);
            viewHolder.mAnniversaryTitle.setText(anniversaryModel.getTitle());

            String description = anniversaryModel.getDescription();
            if (!TextUtils.isEmpty(description)) {
                viewHolder.mDescriptionLabel.setText(description);
            } else {
                viewHolder.mDescriptionLabel.setText("暂无描述");
            }

            //set object date
            if (anniversaryModel.getDate() != null) {
                DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(anniversaryModel.getDate()).withZone(DateTimeZone.getDefault());

                viewHolder.mAnniversaryDate.setText(dateTime.toString(DateTimeFormat.longDate()));
            }

            GoogleLocationModel googleLocation = anniversaryModel.getGoogleLocation();
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
            viewHolder.mPlaceLabel.setText(location == null ? "暂无地点信息" : location);


            //set image type
            ModelAnniversaryTypeDTO anniversaryTypeMode = anniversaryModel.getAnniversaryTypeModel();
            if (anniversaryTypeMode != null) {
                viewHolder.mAnniversaryTypeImage.setImageResource(anniversaryTypeMode.getImageResource());
            }

            calculateDateWithJoda(viewHolder, anniversaryModel);

            ItemType type = FirebaseDatabaseHelper.getInstance().getItemType(anniversaryModel, itemTypeListMap);
            switch (type) {
                case ALL:
                    viewHolder.mTimeLine.setBeginLineView(true);
                    viewHolder.mTimeLine.setEndLineView(true);
                    viewHolder.mTimeLine.setVisibility(View.VISIBLE);
                    break;
                case HEAD:
                    viewHolder.mTimeLine.setBeginLineView(false);
                    viewHolder.mTimeLine.setEndLineView(true);
                    viewHolder.mTimeLine.setVisibility(View.VISIBLE);
                    break;
                case TAIL:
                    viewHolder.mTimeLine.setBeginLineView(true);
                    viewHolder.mTimeLine.setEndLineView(false);
                    viewHolder.mTimeLine.setVisibility(View.VISIBLE);
                    break;
                case NONE:
                    viewHolder.mTimeLine.setBeginLineView(false);
                    viewHolder.mTimeLine.setEndLineView(false);
                    viewHolder.mTimeLine.setVisibility(View.INVISIBLE);
                    break;
            }

            final int progress = calProgress;
            holder.itemView.setOnClickListener(view -> {
                AnniversaryModel selectedModel = (AnniversaryModel) mDateSets.get(position);
                Intent intent = new Intent(mContext, DetailsActivity.class);
//                intent.putExtra("anniversary_uuid", selectedTarget.getAnniuuid());
                intent.putExtra("anniversary", selectedModel);
                //intent.putExtra("progressValue", progress);
                //intent.putExtra("dateInformation", holder.mLeftDayLabel.getText().toString());
                mContext.startActivity(intent);
            });
        }
    }

    private void calculateDateWithJoda(TimeLineViewHolder holder, AnniversaryModel anniversary) {
        //纪念日时间
        String anniversaryDate = anniversary.getDate();

        DateTime anniversaryDateWithJoda = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(anniversaryDate).withZone(DateTimeZone.getDefault());

        //纪念日创建时间
        String createDate = anniversary.getCreateDate();
        DateTime anniversaryCreateDateWithJoda = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(anniversaryDate).withZone(DateTimeZone.getDefault());
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


    public List<Object> getDatas() {
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
                .onPositive((dialog, which) -> {
                    AnniversaryModel anniversaryModel = (AnniversaryModel) mDateSets.get(position);
                    FirebaseDatabaseHelper.getInstance().deleteChildByKey(anniversaryModel.getAnniuuid());
                })
                .show();
    }


    private void updateItem(AnniversaryModel value) {
        List<AnniversaryModel> modelList = Stream
                .of(mDateSets)
                .filter(o -> o instanceof AnniversaryModel && !((AnniversaryModel) o).getAnniuuid().equals(value.getAnniuuid()))
                .map(o -> (AnniversaryModel) o)
                .toList();
        modelList.add(value);
        updateList(modelList);
    }

    private void addItem(AnniversaryModel model) {
        List<AnniversaryModel> modelList = Stream
                .of(mDateSets)
                .filter(o -> o instanceof AnniversaryModel)
                .map(o -> (AnniversaryModel) o).toList();
        modelList.add(model);
        updateList(modelList);
    }

    private void removeItem(String uuid) {
        List<AnniversaryModel> modelList = Stream
                .of(mDateSets)
                .filter(o -> o instanceof AnniversaryModel && !((AnniversaryModel) o).getAnniuuid().equals(uuid))
                .map(o -> (AnniversaryModel) o).toList();
        updateList(modelList);
    }

    private void updateList(List<AnniversaryModel> list) {
        Map<String, List<AnniversaryModel>> sortedMap = FirebaseDatabaseHelper.getInstance().groupData(list);
        itemTypeListMap = FirebaseDatabaseHelper.getInstance().flateDate(sortedMap);

        mDateSets = itemTypeListMap.get(ItemType.DATA);
        notifyDataSetChanged();
    }


    class TimeLineDateViewHolder extends RecyclerView.ViewHolder {
        TextView itemDateTextView;

        TimeLineDateViewHolder(View itemView) {
            super(itemView);
            itemDateTextView = itemView.findViewById(R.id.item_date_tv);
        }
    }


    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemRoot;
        CircleImageView mAnniversaryTypeImage;
        TextView mAnniversaryTitle;
        TimeLineMarker mTimeLine;
        TextView mLeftDayLabel;
        TextView mAnniversaryStatusLabel;
        TextView mAnniversaryDate;
        ImageView mNotificationIcon;
        TextView mDescriptionLabel;
        TextView mPlaceLabel;

        TimeLineViewHolder(View itemView) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.item_layout);
            mAnniversaryTypeImage = itemView.findViewById(R.id.anniversary_type_image_view);
            mAnniversaryTitle = itemView.findViewById(R.id.anniversary_title_label);
            mTimeLine = itemView.findViewById(R.id.item_time_line_view);
            mLeftDayLabel = itemView.findViewById(R.id.anniversary_left_day);
            mAnniversaryDate = itemView.findViewById(R.id.anniversary_date_label);
            mAnniversaryStatusLabel = itemView.findViewById(R.id.anniversary_status_label);
            mNotificationIcon = itemView.findViewById(R.id.anniversary_notification_icon);
            mDescriptionLabel = itemView.findViewById(R.id.anniversary_description_label);
            mPlaceLabel = itemView.findViewById(R.id.anniversary_place_label);

        }
    }

}
