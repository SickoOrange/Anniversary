package com.berber.orange.memories.activity.additem;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.berber.orange.memories.R;
import com.berber.orange.memories.SharedPreferencesHelper;
import com.berber.orange.memories.helper.Constant;
import com.berber.orange.memories.helper.GooglePlaceRequestHandler;
import com.berber.orange.memories.activity.model.ModelAnniversaryTypeDTO;
import com.berber.orange.memories.activity.model.NotificationType;
import com.berber.orange.memories.database.firebasemodel.AnniversaryModel;
import com.berber.orange.memories.database.firebasemodel.GoogleLocationModel;
import com.berber.orange.memories.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gyf.barlibrary.ImmersionBar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

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
    private RadioButton selectedRadioTypeButton;
    private Date currentPickDate = null;
    private TextView mAnniversaryLocation;
    private GoogleLocationModel googleLocationModel = null;
    private AlarmManager alarmManager;
    private GooglePlaceRequestHandler googlePlaceRequestHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        //ScreenUtil.immerseStatusBar(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Your Anniversary");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //google place api
        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        googlePlaceRequestHandler = new GooglePlaceRequestHandler(mGoogleApiClient);

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
        mAnniversaryLocation = findViewById(R.id.anniversary_add_anni_location);
        mAnniversaryLocation.setOnClickListener(this);

        //anniversary notification


        //anniversary description
        anniversaryDescriptionEditText = findViewById(R.id.anniversary_edit_anni_description);

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
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("酒店", R.mipmap.ic_category_2));
        // modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("KTV", R.mipmap.ic_category_4));
        //modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("旅游", R.mipmap.ic_category_5));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("逛风景", R.mipmap.ic_category_6));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("炸鸡啤酒", R.mipmap.ic_category_7));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("冰激凌", R.mipmap.ic_category_8));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("K歌", R.mipmap.ic_category_3));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("休闲娱乐", R.mipmap.ic_category_10));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("美容", R.mipmap.ic_category_11));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("大宝剑", R.mipmap.ic_category_12));
        // modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("旅游", R.mipmap.ic_category_13));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("团购", R.mipmap.ic_category_14));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("景点", R.mipmap.ic_category_16));
        modelAnniversaryTypes.add(new ModelAnniversaryTypeDTO("自定义", R.mipmap.ic_category_15));
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
            case R.id.anniversary_edit_anni_description:
                //get current description
                //currentAnniversaryDescription = anniversaryDescriptionEditText.getText().toString();
                break;
            case R.id.anniversary_add_btn_save:
                //collectInfo();
                writeInfo();
                break;

            case R.id.anniversary_add_anni_location:
                //open new activity for pick a place
                //openPickerPlaceDialog();
                googlePlaceRequestHandler.openPickerPlaceDialog(this, Constant.PLACE_PICKER_REQUEST);
                break;

        }
    }

    private void writeInfo() {

        ModelAnniversaryTypeDTO currentImageResource = getCurrentTypeResource();
        if (currentImageResource == null) {
            alertWarningDialog("请选择事件类型");
            return;
        }


        // ensure anniversary title not empty
        String anniversaryTitle = mAnniversaryTitleEditText.getText().toString();
        if (TextUtils.isEmpty(anniversaryTitle)) {
            alertWarningDialog("标题不能为空");
            return;
        }

        //handle date
        if (currentPickDate == null) {
            alertWarningDialog("请选择一个日期");
            return;
        }
        if (googleLocationModel == null) {
            alertWarningDialog("请选择一个地点");
            return;
        }

        String anniversaryDescription = anniversaryDescriptionEditText.getText().toString();
        //real time database
        AnniversaryModel anniversaryModel = new AnniversaryModel();
        anniversaryModel.setAnniversaryTypeModel(currentImageResource);
        anniversaryModel.setTitle(anniversaryTitle);
        anniversaryModel.setDate(DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").print(new DateTime(currentPickDate).withZone(DateTimeZone.UTC)));
        anniversaryModel.setCreateDate(DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").print(DateTime.now(DateTimeZone.UTC)));
        anniversaryModel.setDescription(anniversaryDescription);
        anniversaryModel.setGoogleLocation(googleLocationModel);

        String uuid = (String) SharedPreferencesHelper.getInstance().getData("user_uuid", "undefined");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users" + "/" + uuid);
        DatabaseReference push = reference.child("anniversaries").push();
        push.setValue(anniversaryModel)
                .addOnSuccessListener(aVoid -> Log.e("TAG", "PUSH ANNIVERSARY LIST SUCCESS"))
                .addOnFailureListener(e -> Log.e("TAG", "PUSH ANNIVERSARY LIST FAILURE"));


        finish();
    }

    private ModelAnniversaryTypeDTO getCurrentTypeResource() {
        RecyclerView currentChild = (RecyclerView) viewPager.getChildAt(viewPager.getCurrentItem());
        AnniversaryTypeRecyclerViewAdapter adapter = (AnniversaryTypeRecyclerViewAdapter) currentChild.getAdapter();
        return adapter.getCurrentImageResource();
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


    private Calendar calculateNotificationTimeInCalendar(String value, String frequency) {
        long hourIndex = 0;
        int prefixIndex = Integer.valueOf(value);
        Calendar instance = Calendar.getInstance();
        switch (frequency) {
            case "minute":
                //hourIndex = prefixIndex * 60 * 1000;
                instance.add(Calendar.MINUTE, prefixIndex * (-1));
                break;
            case "hour":
                //hourIndex = prefixIndex * 60 * 60 * 1000;
                instance.add(Calendar.HOUR, prefixIndex * (-1));
                break;
            case "week":
                //hourIndex = prefixIndex * 24 * 7 * 3600 * 1000;
                instance.add(Calendar.WEEK_OF_YEAR, prefixIndex * (-1));
                break;
            case "day":
                //hourIndex = prefixIndex * 24 * 3600 * 1000;
                instance.add(Calendar.DAY_OF_MONTH, prefixIndex * (-1));
                break;
            case "month":
                instance.add(Calendar.MONTH, prefixIndex * (-1));
                break;
        }
        return instance;
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
                .onPositive((dialog, which) -> {
                    View customView = dialog.getCustomView();
                    DatePicker datePicker = customView.findViewById(R.id.datePicker);
                    int currentYear = datePicker.getYear();
                    int currentMonth = datePicker.getMonth();
                    int currentDay = datePicker.getDayOfMonth();
                    DateTime dateTime = new DateTime(currentYear, currentMonth + 1, currentDay, 0, 0);
                    currentPickDate = dateTime.withZone(DateTimeZone.UTC).toDate();
                    anniversaryDateTextView.setText(SimpleDateFormat.getDateInstance().format(currentPickDate));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Log.e("TAG", place.getId());
                String toastMsg = String.format("Place: %s", place.getName() + "/n" + place.getAddress() + "/n" + place.getPhoneNumber());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mAnniversaryLocation.setText(place.getAddress());

                googleLocationModel = new GoogleLocationModel();
                googleLocationModel.setLocationName(place.getName() == null ? null : place.getName().toString());
                googleLocationModel.setLocationAddress(place.getAddress() == null ? null : place.getAddress().toString());
                googleLocationModel.setLocationPhoneNumber(place.getPhoneNumber() == null ? null : place.getPhoneNumber().toString());
                googleLocationModel.setPlaceId(place.getId());
                googleLocationModel.setAttribution(place.getAttributions() == null ? null : place.getAttributions().toString());
                googleLocationModel.setWebSiteUri(place.getAttributions() == null ? null : place.getAttributions().toString());
                googleLocationModel.setLatitude(place.getLatLng() == null ? 0 : place.getLatLng().latitude);
                googleLocationModel.setLongitude(place.getLatLng() == null ? 0 : place.getLatLng().longitude);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO: Please implement GoogleApiClient.OnConnectionFailedListener to
        // handle connection failures.
    }
}