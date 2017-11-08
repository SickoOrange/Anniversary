package com.berber.orange.memories.activity;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.main.ScrollingActivity;
import com.berber.orange.memories.dbservice.Anniversary;
import com.berber.orange.memories.dbservice.AnniversaryDao;
import com.berber.orange.memories.dbservice.NotificationSending;
import com.berber.orange.memories.dbservice.NotificationSendingDao;
import com.berber.orange.memories.utils.ScreenUtil;
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
    private Switch enableNotificationButton;
    private BottomSheetDialog notificationTypePickerDialog;
    private BottomSheetDialog notificationTimePickerDialog;
    private RadioButton selectedButton;
    private CircleImageView anniversaryTypeImage;
    private TextView anniversaryTypeName;
    private long notificationTimeBeforeInMillis;
    private final int REQUEST_NEW_ITEM = 9001;
    private AnniversaryDao anniversaryDao;
    private NotificationSendingDao notificationSendingDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        boolean immerse = ScreenUtil.immerseStatusBar(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Your Anniversary");

        initAnniversaryTypeData();
        initView();
        init();

        anniversaryDao = ((APP) getApplication()).getDaoSession().getAnniversaryDao();
        notificationSendingDao = ((APP) getApplication()).getDaoSession().getNotificationSendingDao();

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

        anniversaryTypeImage = findViewById(R.id.anniversary_add_type_image);
        anniversaryTypeName = findViewById(R.id.anniversary_add_type_name);

        //anniversary tile edit text
        anniversaryTitleEditText = findViewById(R.id.anniversary_add_anni_title);


        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        String dateString = sdf.format(date);
        String[] splits = dateString.split(" ");


        //anniversary date
        anniversaryDateTextView = findViewById(R.id.anniversary_add_anni_date);
        anniversaryDateTextView.setOnClickListener(this);
        //set default currently date
        anniversaryDateTextView.setText(splits[0]);
        //anniversary time
        anniversaryTimeTextView = findViewById(R.id.anniversary_add_anni_time);
        anniversaryTimeTextView.setOnClickListener(this);
        anniversaryTimeTextView.setText(splits[1]);

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

        //enable notification button
        enableNotificationButton = findViewById(R.id.notification_enable_btn);

    }


    private void openNotificationTypePickerDialog() {
        notificationTypePickerDialog = new BottomSheetDialog(AddItemActivity.this);
        View notificationTypePickerDialogView = LayoutInflater.from(AddItemActivity.this).inflate(R.layout.notification_type_picker, null);
        LinearLayout ll1 = notificationTypePickerDialogView.findViewById(R.id.notification_type_picker_ll1);
        ll1.setOnClickListener(this);
        LinearLayout ll2 = notificationTypePickerDialogView.findViewById(R.id.notification_type_picker_ll2);
        ll2.setOnClickListener(this);
        LinearLayout ll3 = notificationTypePickerDialogView.findViewById(R.id.notification_type_picker_ll3);
        ll3.setOnClickListener(this);
        notificationTypePickerDialog.setContentView(notificationTypePickerDialogView);
        notificationTypePickerDialog.show();
    }

    private void openNotificationTimePickerDialog() {
        notificationTimePickerDialog = new BottomSheetDialog(AddItemActivity.this);
        View notificationTimePickerDialogView = LayoutInflater.from(AddItemActivity.this).inflate(R.layout.notification_time_picker, null);
        Button timeSettingCustom1 = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_notification_time_label_1);
        timeSettingCustom1.setOnClickListener(this);
        Button timeSettingCustom2 = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_notification_time_label_2);
        timeSettingCustom2.setOnClickListener(this);
        Button timeSettingCustom3 = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_notification_time_label_3);
        timeSettingCustom3.setOnClickListener(this);
        Button timeSettingCustom4 = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_notification_time_label_4);
        timeSettingCustom4.setOnClickListener(this);
        Button timeSettingCustom5 = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_notification_time_label_5);
        timeSettingCustom5.setOnClickListener(this);
        Button timeSettingCustom6 = notificationTimePickerDialogView.findViewById(R.id.bottom_sheet_notification_time_label_6);
        timeSettingCustom6.setOnClickListener(this);
        notificationTimePickerDialog.setContentView(notificationTimePickerDialogView);
        notificationTimePickerDialog.show();
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
                if (enableNotificationButton.isChecked()) {
                    openNotificationTimePickerDialog();
                }
                break;
            case R.id.anniversary_add_anni_notification_type:
                if (enableNotificationButton.isChecked()) {
                    openNotificationTypePickerDialog();
                }
                break;

            case R.id.notification_type_picker_ll1:
                Utils.showToast(AddItemActivity.this, "Notification type", 1);
                anniversaryNotificationTypeTextView.setText(R.string.notification_notification);
                notificationTypePickerDialog.dismiss();
                break;
            case R.id.notification_type_picker_ll2:
                Utils.showToast(AddItemActivity.this, "Email type", 1);
                anniversaryNotificationTypeTextView.setText(R.string.notification_email);
                notificationTypePickerDialog.dismiss();

                break;
            case R.id.notification_type_picker_ll3:
                Utils.showToast(AddItemActivity.this, "All type", 1);
                anniversaryNotificationTypeTextView.setText(R.string.notification_all);
                notificationTypePickerDialog.dismiss();
                break;

            case R.id.bottom_sheet_notification_time_label_6:
                openNotificationCustomTimeSettingDialog();
                notificationTimePickerDialog.dismiss();
                break;

            case R.id.bottom_sheet_notification_time_label_5:
                Button button5 = (Button) view;
                anniversaryNotificationTimeTextView.setText(button5.getText().toString().toLowerCase());
                notificationTimePickerDialog.dismiss();
                break;

            case R.id.bottom_sheet_notification_time_label_4:
                Button button4 = (Button) view;
                anniversaryNotificationTimeTextView.setText(button4.getText().toString().toLowerCase());
                notificationTimePickerDialog.dismiss();

                break;

            case R.id.bottom_sheet_notification_time_label_3:
                Button button3 = (Button) view;
                anniversaryNotificationTimeTextView.setText(button3.getText().toString().toLowerCase());
                notificationTimePickerDialog.dismiss();
                break;

            case R.id.bottom_sheet_notification_time_label_2:
                Button button2 = (Button) view;
                anniversaryNotificationTimeTextView.setText(button2.getText().toString().toLowerCase());
                notificationTimePickerDialog.dismiss();
                break;

            case R.id.bottom_sheet_notification_time_label_1:
                Button button1 = (Button) view;
                anniversaryNotificationTimeTextView.setText(button1.getText().toString().toLowerCase());
                notificationTimePickerDialog.dismiss();
                break;

            case R.id.anniversary_add_btn_save:
                //collectInfo();
                writeInfo();
                break;

        }
    }

    private void writeInfo() {
        //save all information into entity
        Anniversary anniversary = new Anniversary();

        // handle anniversary title
        String anniversaryTitle = anniversaryTitleEditText.getText().toString();
        if (TextUtils.isEmpty(anniversaryTitle)) {
            alertWarningDialog("Anniversary  title can't be empty");
            return;
        }
        anniversary.setTitle(anniversaryTitle);

        //handle location
        anniversary.setLocation("London");

        //handle date
        Date currentAnniversaryDate = getCurrentAnniversaryDate();
        if (currentAnniversaryDate != null) {
            anniversary.setDate(currentAnniversaryDate);
        } else {
            alertWarningDialog("you must pick a certain date and time");
            return;
        }

        //handle create Date
        anniversary.setCreateDate(new Date());

        //handle description
        String anniversaryDescription = anniversaryDescriptionEditText.getText().toString();
        anniversary.setDescription(anniversaryDescription);

        long anniversaryId = anniversaryDao.insert(anniversary);

        //handle remind date
        if (enableNotificationButton.isChecked()) {
            Date notificationDate = calculateAnniversaryNotificationDate(currentAnniversaryDate, notificationTimeBeforeInMillis);
            NotificationSending notificationSending = new NotificationSending();
            notificationSending.setSendingDate(notificationDate);
            notificationSending.setRecipient("heylbly@gmail.com");
            String notificationTypeString = anniversaryNotificationTypeTextView.getText().toString();
            notificationSending.setNotificationType(getNotificationType(notificationTypeString));

            notificationSending.setAnniversaryId(anniversaryId);
            notificationSending.setAnniversary(anniversary);
            //anniversary.setNotificationSending(notificationSending);
            notificationSendingDao.insert(notificationSending);
        }

        //


//        Intent intent = new Intent(AddItemActivity.this, ScrollingActivity.class);
//        intent.putExtra("object", dto);
//        setResult(REQUEST_NEW_ITEM, intent);
//        finish();
    }

    private void collectInfo() {
        //save all information into entity
        AnniversaryDTO dto = new AnniversaryDTO();

        // handle anniversary title
        String anniversaryTitle = anniversaryTitleEditText.getText().toString();
        if (TextUtils.isEmpty(anniversaryTitle)) {
            alertWarningDialog("Anniversary  title can't be empty");
            return;
        }
        dto.setTitle(anniversaryTitle);

        //handle location
        dto.setLocation("London");

        //handle date
        Date currentAnniversaryDate = getCurrentAnniversaryDate();
        if (currentAnniversaryDate != null) {
            dto.setDate(currentAnniversaryDate);
        } else {
            alertWarningDialog("you must pick a certain date and time");
            return;
        }

        //handle create Date
        dto.setCreateDate(new Date());

        //handle description
        String anniversaryDescription = anniversaryDescriptionEditText.getText().toString();
        dto.setDescription(anniversaryDescription);

        //handle remind date
        if (enableNotificationButton.isChecked()) {
            Date notificationDate = calculateAnniversaryNotificationDate(currentAnniversaryDate, notificationTimeBeforeInMillis);
            NotificationSendingDTO notificationSendingDTO = new NotificationSendingDTO();
            notificationSendingDTO.setSendingDate(notificationDate);
            notificationSendingDTO.setRecipient(" ");
            String notificationTypeString = anniversaryNotificationTypeTextView.getText().toString();
            notificationSendingDTO.setNotificationType(getNotificationType(notificationTypeString));
            dto.setNotificationSendingDTO(notificationSendingDTO);
        }


        Intent intent = new Intent(AddItemActivity.this, ScrollingActivity.class);
        intent.putExtra("object", dto);
        setResult(REQUEST_NEW_ITEM, intent);
        finish();
    }

    private NotificationType getNotificationType(String notificationTypeString) {
        NotificationType notificationType = null;
        switch (notificationTypeString) {
            case "Notification":
                notificationType = NotificationType.NOTIFICATION;
                break;
            case "Email":
                notificationType = NotificationType.EMAIL;
                break;
            case "All":
                notificationType = NotificationType.ALL;
                break;
        }
        return notificationType;
    }

    private void alertWarningDialog(String content) {
        new MaterialDialog.Builder(this)
                .title("Warning")
                .content(content)
                .positiveText("OK")
                .build()
                .show();
    }

    private void openNotificationCustomTimeSettingDialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("通知时间设定")
                .customView(R.layout.notification_custom_time_picker, true)
                .positiveText("SUBMIT")
                .build();
        View customView = dialog.getCustomView();
        if (customView == null) {
            throw new NullPointerException("custom view can't be null");
        }
        final EditText customTimeEditView = customView.findViewById(R.id.custom_time_value);
        RadioGroup radioGroup = customView.findViewById(R.id.custom_time_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                selectedButton = radioButton;
                Utils.showToast(AddItemActivity.this, "" + radioButton.getText().toString(), 1);
            }
        });
        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                String msg = customTimeEditView.getText().toString() + " " + selectedButton.getText().toString() + " before";
                anniversaryNotificationTimeTextView.setText(msg);
                notificationTimeBeforeInMillis = calculateNotificationIndex(msg);
            }
        });
        dialog.show();

    }

    private long calculateNotificationIndex(String msg) {
        String[] strings = msg.split(" ");
        int prefixIndex = Integer.valueOf(strings[0]);
        long hourIndex = 0;
        switch (strings[1]) {
            case "minute":
                hourIndex = prefixIndex * 60 * 1000;
                break;
            case "hour":
                hourIndex = prefixIndex * 60 * 60 * 1000;
                break;
            case "week":
                hourIndex = prefixIndex * 24 * 7 * 3600 * 1000;
                break;
            case "day":
                hourIndex = prefixIndex * 24 * 3600 * 1000;
                break;
            case "month":
                break;
        }
        return hourIndex;
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

        if (TextUtils.isEmpty(currentPickDateString) || TextUtils.isEmpty(currentPickTimeString)) {
            return null;
        }

        String dateString = currentPickDateString + " " + currentPickTimeString;
        Date currentDate = null;
        try {
            currentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH).parse(dateString);
            currentPickTimeString = "";
            currentPickTimeString = "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    private Date calculateAnniversaryNotificationDate(Date currentDate, long hourIndex) {
        long timeInMillis = 0;
        if (currentDate != null) {
            timeInMillis = currentDate.getTime() - (hourIndex);
        }
        return new Date(timeInMillis);
    }

    public CircleImageView getAnniversaryTypeImageView() {
        return anniversaryTypeImage;
    }

    public TextView getAnniversaryTypeTextView() {
        return anniversaryTypeName;
    }
}