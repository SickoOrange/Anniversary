package com.berber.orange.memories.activity.additem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.model.ModelAnniversaryTypeDTO;
import com.berber.orange.memories.activity.model.NotificationType;
import com.berber.orange.memories.dbservice.Anniversary;
import com.berber.orange.memories.dbservice.AnniversaryDao;
import com.berber.orange.memories.dbservice.ModelAnniversaryType;
import com.berber.orange.memories.dbservice.ModelAnniversaryTypeDao;
import com.berber.orange.memories.dbservice.NotificationSending;
import com.berber.orange.memories.dbservice.NotificationSendingDao;
import com.berber.orange.memories.utils.ScreenUtil;
import com.berber.orange.memories.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int HOME_ITEM_SIZE = 10;

    private ViewPager viewPager;
    private ArrayList<ModelAnniversaryTypeDTO> modelAnniversaryTypes;
    private LinearLayout indicatorContainer;
    private int prePosition;
    private TextView anniversaryDateTextView;
    private EditText mAnniversaryTitleEditText;
    private EditText anniversaryDescriptionEditText;
    private TextView anniversaryNotificationTextView;
    private RadioButton selectedRadioFrequencyButton;
    private CircleImageView anniversaryTypeImage;
    private TextView anniversaryTypeName;
    private long notificationTimeBeforeInMillis;
    private final int REQUEST_NEW_ITEM = 9001;
    private AnniversaryDao anniversaryDao;
    private NotificationSendingDao notificationSendingDao;
    private ModelAnniversaryTypeDao modelAnniversaryTypeDao;
    private RadioButton selectedRadioTypeButton;
    private boolean isNotificationEnable = false;
    private Date currentPickDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ScreenUtil.immerseStatusBar(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Your Anniversary");

        initAnniversaryTypeData();
        initView();
        init();

        anniversaryDao = ((APP) getApplication()).getDaoSession().getAnniversaryDao();
        notificationSendingDao = ((APP) getApplication()).getDaoSession().getNotificationSendingDao();
        modelAnniversaryTypeDao = ((APP) getApplication()).getDaoSession().getModelAnniversaryTypeDao();


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
        mAnniversaryTitleEditText = findViewById(R.id.anniversary_add_anni_title);


        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        String dateString = sdf.format(date);
        String[] splits = dateString.split(" ");


        //anniversary date
        anniversaryDateTextView = findViewById(R.id.anniversary_type_image_view);
        anniversaryDateTextView.setOnClickListener(this);
        //set default currently date
        anniversaryDateTextView.setText(splits[0]);

        //anniversary location
        TextView anniversaryLocation = findViewById(R.id.anniversary_add_anni_location);
        anniversaryLocation.setOnClickListener(this);

        //anniversary notification
        anniversaryNotificationTextView = findViewById(R.id.anniversary_add_anni_notification);
        anniversaryNotificationTextView.setOnClickListener(this);

        //anniversary description
        anniversaryDescriptionEditText = findViewById(R.id.anniversary_add_anni_description);

        // save into database button
        Button btnSave = findViewById(R.id.anniversary_add_btn_save);
        btnSave.setOnClickListener(this);

    }

    private void openNotificationSettingDialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("通知设定")
                .customView(R.layout.notification_setting_picker, true)
                .positiveText("Submit")
                .negativeText("Cancel")
                .build();
        View customView = dialog.getCustomView();
        if (customView == null) {
            throw new NullPointerException("custom view can't be null");
        }
        final EditText customTimeEditView = customView.findViewById(R.id.custom_notification_value);
        RadioGroup notificationFrequencyGroup = customView.findViewById(R.id.radio_notification_frequency);
        notificationFrequencyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedRadioFrequencyButton = radioGroup.findViewById(i);
            }
        });

        RadioGroup notificationTypeGroup = customView.findViewById(R.id.radio_notification_type);
        notificationTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedRadioTypeButton = radioGroup.findViewById(i);
            }
        });
        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                String frequencyString = customTimeEditView.getText().toString();
                if (TextUtils.isEmpty(frequencyString)) {
                    Utils.showToast(AddItemActivity.this, "请填写通知的频率", 1);
                    return;
                }
                if (selectedRadioTypeButton == null || selectedRadioFrequencyButton == null) {
                    Utils.showToast(AddItemActivity.this, "请选择一个合适的选项", 1);
                    return;
                }
                String msg = frequencyString + " " + selectedRadioFrequencyButton.getText().toString() + " before" + ",   " + selectedRadioTypeButton.getText().toString();
                anniversaryNotificationTextView.setText(msg);
                notificationTimeBeforeInMillis = calculateNotificationTimeInMillis(frequencyString, selectedRadioFrequencyButton.getText().toString());
                isNotificationEnable = true;
            }
        });
        dialog.getBuilder().onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void initAnniversaryTypeData() {
        modelAnniversaryTypes = new ArrayList<>();
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("美食", R.mipmap.ic_category_0));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("电影", R.mipmap.ic_category_1));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("酒店住宿", R.mipmap.ic_category_2));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("生活服务", R.mipmap.ic_category_3));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("KTV", R.mipmap.ic_category_4));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("旅游", R.mipmap.ic_category_5));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("学习培训", R.mipmap.ic_category_6));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("汽车服务", R.mipmap.ic_category_7));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("摄影写真", R.mipmap.ic_category_8));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("休闲娱乐", R.mipmap.ic_category_10));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("丽人", R.mipmap.ic_category_11));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("运动健身", R.mipmap.ic_category_12));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("大保健", R.mipmap.ic_category_13));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("团购", R.mipmap.ic_category_14));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("景点", R.mipmap.ic_category_16));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("全部分类", R.mipmap.ic_category_15));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.anniversary_type_image_view:
                //get current anniversary date
                openDatePickerDialog();
                break;
            case R.id.anniversary_add_anni_title:
                //get current title
                //currentAnniversaryTitle = mAnniversaryTitleEditText.getText().toString();
                break;
            case R.id.anniversary_add_anni_description:
                //get current description
                //currentAnniversaryDescription = anniversaryDescriptionEditText.getText().toString();
                break;

            case R.id.anniversary_add_anni_notification:
                openNotificationSettingDialog();
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
        String anniversaryTitle = mAnniversaryTitleEditText.getText().toString();
        if (TextUtils.isEmpty(anniversaryTitle)) {
            alertWarningDialog("标题不能为空");
            return;
        }
        anniversary.setTitle(anniversaryTitle);

        //handle location
        anniversary.setLocation("China");

        //handle date
        if (currentPickDate != null) {
            anniversary.setDate(currentPickDate);
        } else {
            alertWarningDialog("你必须选择一个日期");
            return;
        }

        //handle create Date
        anniversary.setCreateDate(new Date());

        //handle description
        String anniversaryDescription = anniversaryDescriptionEditText.getText().toString();
        anniversary.setDescription(anniversaryDescription);

        long anniversaryId = anniversaryDao.insert(anniversary);

        //handle remind date
        if (isNotificationEnable) {
            Date notificationDate = calculateAnniversaryNotificationDate(currentPickDate, notificationTimeBeforeInMillis);
            NotificationSending notificationSending = new NotificationSending();
            notificationSending.setSendingDate(notificationDate);
            notificationSending.setRecipient("heylbly@gmail.com");
            notificationSending.setNotificationType(getNotificationType(selectedRadioTypeButton.getText().toString()));

            notificationSending.setAnniversaryId(anniversaryId);
            notificationSending.setAnniversary(anniversary);
            long notificationSendingId = notificationSendingDao.insert(notificationSending);
            anniversary.setNotificationSending(notificationSending);
            anniversary.setNotificationSendingId(notificationSendingId);
            anniversaryDao.update(anniversary);
        }


        RecyclerView currentChild = (RecyclerView) viewPager.getChildAt(viewPager.getCurrentItem());
        AnniversaryTypeRecyclerViewAdapter adapter = (AnniversaryTypeRecyclerViewAdapter) currentChild.getAdapter();
        ModelAnniversaryTypeDTO currentImageResource = adapter.getCurrentImageResource();

        //handle anniversaryType
        ModelAnniversaryType modelAnniversaryType = new ModelAnniversaryType();
        modelAnniversaryType.setName(currentImageResource.getName());
        modelAnniversaryType.setImageResource(currentImageResource.getImageResource());
        long modelAnniversaryTypeId = modelAnniversaryTypeDao.insert(modelAnniversaryType);

        anniversary.setModelAnniversaryType(modelAnniversaryType);
        anniversary.setModelAnniversaryTypeId(modelAnniversaryTypeId);
        anniversaryDao.update(anniversary);

        Intent intent = new Intent();
        intent.putExtra("obj", anniversary);
        setResult(REQUEST_NEW_ITEM, intent);
        finish();
    }

    private NotificationType getNotificationType(String notificationTypeString) {
        NotificationType notificationType = null;
        switch (notificationTypeString) {
            case "System Notification":
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


    private long calculateNotificationTimeInMillis(String value, String frequency) {
        long hourIndex = 0;
        int prefixIndex = Integer.valueOf(value);
        switch (frequency) {
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


//    private void openTimePickerDialog() {
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
//                .title(R.string.time_picker_title)
//                .customView(R.layout.dialog_timepicker, false)
//                .positiveText(android.R.string.ok)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        View customView = dialog.getCustomView();
//                        TimePicker timePicker = customView.findViewById(R.id.timePicker);
//                        String hour = String.valueOf(timePicker.getCurrentHour());
//                        String minute = String.valueOf(timePicker.getCurrentMinute());
//
//                        if (hour.length() == 1) {
//                            hour = "0" + hour;
//                        }
//
//                        if (minute.length() == 1) {
//                            minute = "0" + minute;
//                        }
//                        currentPickTimeString = String.format("%s:%s", hour, minute);
//                        // anniversaryTimeTextView.setText(currentPickTimeString);
//                        Utils.showToast(AddItemActivity.this, hour + " " + minute, 1);
//                    }
//                })
//                .negativeText(android.R.string.cancel);
//        builder.show();
//
//    }

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
                        int currentYear = datePicker.getYear();
                        int currentMonth = datePicker.getMonth();
                        int currentDay = datePicker.getDayOfMonth();
                        currentPickDate = new Date(currentYear - 1900, currentMonth, currentDay);

                        if (currentPickDate.getTime() < System.currentTimeMillis()) {
                            Utils.showToast(AddItemActivity.this, "选择的日期不能小于当前的日期", 0);
                            return;
                        }
                        anniversaryDateTextView.setText(SimpleDateFormat.getDateInstance().format(currentPickDate));
                    }
                })
                .negativeText(android.R.string.cancel);
        builder.show();
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