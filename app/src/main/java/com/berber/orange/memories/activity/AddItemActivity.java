package com.berber.orange.memories.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.berber.orange.memories.R;
import com.berber.orange.memories.utils.Utils;
import com.berber.orange.memories.widget.IndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int HOME_ITEM_SIZE = 10;

    private ViewPager viewPager;
    private ArrayList<ModelAnniversaryType> modelAnniversaryTypes;
    private IndicatorView indicatorView;
    private LinearLayout indicatorContainer;
    private int prePosition;
    private TextView anniversaryTimeTextView;
    private TextView anniversaryDateTextView;
    private EditText anniversaryTitleEditText;
    private String currentAnniversaryTitle;
    private EditText anniversaryDescriptionEditText;
    private String currentAnniversaryDescription;
    private String currentPickDateString;
    private String currentPickTimeString;
    private TextView anniversaryNotificationTimeTextView;
    private TextView anniversaryNotificationTypeTextView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Your Anniversary");

        initAnniversaryTypeData();
        initView();
        init();

    }

    private void init() {

        int pageCount = (int) Math.ceil(modelAnniversaryTypes.size() * 1.0 / HOME_ITEM_SIZE);
        List<View> viewList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < pageCount; i++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_recycler_view, null, false);
            recyclerView.setLayoutManager(new GridLayoutManager(AddItemActivity.this, 5));
            //add adapter to every recycler view
            AnniversaryTypeRecyclerViewAdapter anniversaryTypeRecyclerViewAdapter = new AnniversaryTypeRecyclerViewAdapter(AddItemActivity.this, modelAnniversaryTypes, i, HOME_ITEM_SIZE);
            recyclerView.setAdapter(anniversaryTypeRecyclerViewAdapter);

            viewList.add(recyclerView);

            //create indicator
            ImageView dot = new ImageView(AddItemActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(13, 13);
            params.setMargins(15, 0, 0, 0);
            dot.setLayoutParams(params);
            dot.setImageResource(R.drawable.dot);
            indicatorContainer.addView(dot);

        }

        prePosition = 0;
        indicatorContainer.getChildAt(prePosition).setSelected(true);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorContainer.getChildAt(prePosition).setSelected(false);
                indicatorContainer.getChildAt(position).setSelected(true);
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        viewPager = findViewById(R.id.anniversary_add_type_vp);
        //indicatorView = findViewById(R.id.indicator);
        indicatorContainer = findViewById(R.id.my_indicator_container);

        CircleImageView anniversaryTypeImage = findViewById(R.id.anniversary_add_type_image);
        TextView anniversaryTypeName = findViewById(R.id.anniversary_add_type_name);

        //anniversary tile edit text
        anniversaryTitleEditText = findViewById(R.id.anniversary_add_anni_title);

        //anniversary date
        anniversaryDateTextView = findViewById(R.id.anniversary_add_anni_date);
        anniversaryDateTextView.setOnClickListener(this);
        //anniversary time
        anniversaryTimeTextView = findViewById(R.id.anniversary_add_anni_time);
        anniversaryTimeTextView.setOnClickListener(this);

        //anniversary location
        TextView anniversaryLocation = findViewById(R.id.anniversary_add_anni_location);
        anniversaryLocation.setOnClickListener(this);

        //anniversary notification
        anniversaryNotificationTimeTextView = findViewById(R.id.anniversary_add_anni_notification_time);
        anniversaryNotificationTimeTextView.setOnClickListener(this);
        anniversaryNotificationTypeTextView = findViewById(R.id.anniversary_add_anni_notification_type);
        anniversaryNotificationTypeTextView.setOnClickListener(this);

        //anniversary description
        anniversaryDescriptionEditText = findViewById(R.id.anniversary_add_anni_description);

        // save into database button
        Button btnSave = findViewById(R.id.anniversary_add_btn_save);
        btnSave.setOnClickListener(this);
    }

    private void initAnniversaryTypeData() {
        modelAnniversaryTypes = new ArrayList<>();
        modelAnniversaryTypes.add(new ModelAnniversaryType("美食", R.mipmap.ic_category_0));
        modelAnniversaryTypes.add(new ModelAnniversaryType("电影", R.mipmap.ic_category_1));
        modelAnniversaryTypes.add(new ModelAnniversaryType("酒店住宿", R.mipmap.ic_category_2));
        modelAnniversaryTypes.add(new ModelAnniversaryType("生活服务", R.mipmap.ic_category_3));
        modelAnniversaryTypes.add(new ModelAnniversaryType("KTV", R.mipmap.ic_category_4));
        modelAnniversaryTypes.add(new ModelAnniversaryType("旅游", R.mipmap.ic_category_5));
        modelAnniversaryTypes.add(new ModelAnniversaryType("学习培训", R.mipmap.ic_category_6));
        modelAnniversaryTypes.add(new ModelAnniversaryType("汽车服务", R.mipmap.ic_category_7));
        modelAnniversaryTypes.add(new ModelAnniversaryType("摄影写真", R.mipmap.ic_category_8));
        modelAnniversaryTypes.add(new ModelAnniversaryType("休闲娱乐", R.mipmap.ic_category_10));
        modelAnniversaryTypes.add(new ModelAnniversaryType("丽人", R.mipmap.ic_category_11));
        modelAnniversaryTypes.add(new ModelAnniversaryType("运动健身", R.mipmap.ic_category_12));
        modelAnniversaryTypes.add(new ModelAnniversaryType("大保健", R.mipmap.ic_category_13));
        modelAnniversaryTypes.add(new ModelAnniversaryType("团购", R.mipmap.ic_category_14));
        modelAnniversaryTypes.add(new ModelAnniversaryType("景点", R.mipmap.ic_category_16));
        modelAnniversaryTypes.add(new ModelAnniversaryType("全部分类", R.mipmap.ic_category_15));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.anniversary_add_anni_date:
                //get current anniversary date
                openDatePickerDialog();
                break;
            case R.id.anniversary_add_anni_time:
                //get current anniversary time
                openTimePickerDialog();
                break;

            case R.id.anniversary_add_anni_title:
                //get current title
                currentAnniversaryTitle = anniversaryTitleEditText.getText().toString();
                break;
            case R.id.anniversary_add_anni_description:
                //get current description
                currentAnniversaryDescription = anniversaryDescriptionEditText.getText().toString();
                break;

            case R.id.anniversary_add_anni_notification_time:
                openNotificationTimePickerDialog();
                break;
            case R.id.anniversary_add_anni_notification_type:
                openNotificationTypePickerDialog();
                break;

            case R.id.notification_type_picker_ll1:
                Utils.showToast(AddItemActivity.this, "Notification type", 1);
                break;
            case R.id.notification_type_picker_ll2:
                Utils.showToast(AddItemActivity.this, "Email type", 1);

                break;
            case R.id.notification_type_picker_ll3:
                Utils.showToast(AddItemActivity.this, "All type", 1);
                break;

            case R.id.bottom_sheet_enable_btn:
                break;


        }
    }

    private void openNotificationTypePickerDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(AddItemActivity.this);
        View notificationTypePickerDialogView = LayoutInflater.from(AddItemActivity.this).inflate(R.layout.notification_type_picker, null);
        LinearLayout ll1 = notificationTypePickerDialogView.findViewById(R.id.notification_type_picker_ll1);
        ll1.setOnClickListener(this);
        LinearLayout ll2 = notificationTypePickerDialogView.findViewById(R.id.notification_type_picker_ll2);
        ll2.setOnClickListener(this);
        LinearLayout ll3 = notificationTypePickerDialogView.findViewById(R.id.notification_type_picker_ll3);
        ll3.setOnClickListener(this);
        dialog.setContentView(notificationTypePickerDialogView);
        dialog.show();
    }

    private void openNotificationTimePickerDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(AddItemActivity.this);
        View notificationTimePickerDialogView = LayoutInflater.from(AddItemActivity.this).inflate(R.layout.notification_time_picker, null);
        final Switch enableNotificationButton = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_enable_btn);
        final NestedScrollView bottomSheetNotificationScrollView = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_notification_scroll_view);
        enableNotificationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    bottomSheetNotificationScrollView.setVisibility(View.VISIBLE);
                    anniversaryNotificationTypeTextView.setClickable(true);
                    System.out.println(true);
                } else {
                    bottomSheetNotificationScrollView.setVisibility(View.GONE);
                    anniversaryNotificationTypeTextView.setClickable(false);
                    System.out.println(false);
                }
            }
        });
        dialog.setContentView(notificationTimePickerDialogView);
        dialog.show();
    }

    private void openTimePickerDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.time_picker_title)
                .customView(R.layout.dialog_timepicker, false)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View customView = dialog.getCustomView();
                        TimePicker timePicker = customView.findViewById(R.id.timePicker);
                        String hour = String.valueOf(timePicker.getCurrentHour());
                        String minute = String.valueOf(timePicker.getCurrentMinute());

                        if (hour.length() == 1) {
                            hour = "0" + hour;
                        }

                        if (minute.length() == 1) {
                            minute = minute + "0";
                        }
                        currentPickTimeString = String.format("%s:%s", hour, minute);
                        anniversaryTimeTextView.setText(currentPickTimeString);
                        Utils.showToast(AddItemActivity.this, hour + " " + minute, 1);
                    }
                })
                .negativeText(android.R.string.cancel);
        builder.show();

    }

    private void openDatePickerDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.date_picker_title)
                .customView(R.layout.dialog_datepicker, false)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View customView = dialog.getCustomView();
                        DatePicker datePicker = customView.findViewById(R.id.datePicker);
                        String year = String.valueOf(datePicker.getYear());
                        String currentMonth = String.valueOf(datePicker.getMonth() + 1);
                        String day = String.valueOf(datePicker.getDayOfMonth());
                        currentPickDateString = String.format("%s-%s-%s", year, currentMonth, day);
                        anniversaryDateTextView.setText(currentPickDateString);

                        Utils.showToast(AddItemActivity.this, year + " " + currentMonth + " " + day, 1);
                    }
                })
                .negativeText(android.R.string.cancel);
        builder.show();
    }

    private Date getCurrentAnniversaryDate() {

        String dateString = currentPickDateString + " " + currentPickTimeString;
        Date currentDate = null;
        try {
            currentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm hh:mm", Locale.ENGLISH).parse(dateString);
            currentPickTimeString = "";
            currentPickTimeString = "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    private Date calculateAnniversaryNotificationDate(Date currentDate, int hourIndex) {
        long timeInMillis = currentDate.getTime() - (long) (hourIndex * 3600 * 1000);
        return new Date(timeInMillis);
    }
}
