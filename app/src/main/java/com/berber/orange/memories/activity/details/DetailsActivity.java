package com.berber.orange.memories.activity.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.activity.BaseUtils;
import com.berber.orange.memories.database.FirebaseDatabaseHelper;
import com.berber.orange.memories.database.firebasemodel.AnniversaryModel;
import com.berber.orange.memories.database.firebasemodel.GoogleLocationModel;
import com.berber.orange.memories.helper.Constant;
import com.berber.orange.memories.helper.ImageUtils;
import com.berber.orange.memories.helper.MatisseImagePicker;
import com.berber.orange.memories.activity.preview.AnniPreviewActivity;
import com.berber.orange.memories.helper.FileUtils;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.victor.loading.book.BookLoading;
import com.youth.banner.Banner;
import com.zhihu.matisse.Matisse;


import org.apmem.tools.layouts.FlowLayout;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;


public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private NumberProgressBar detailsAnniProgressbar;
    private boolean isFavoriteClick;

    private Banner placePhotoBanner;
    private TextView mLocationNameTV;
    private TextView mLocationAddressTV;
    private TextView mLocationNumberTV;
    private TextView mAnniversaryTitleTV;
    private TextView mAnniversaryDateTV;
    private TextView mAnniversaryDescriptionTV;
    private CircleImageView mAnniversaryTypeIV;
    private TextView mTimeProgressLabel1;
    private TextView mTimeProgressLabel2;
    private TextView mTimeProgressLabel3;
    private TextView mNotificationDateTV;
    private TextView mNotificationTypeTV;
    private TextView mNotificationEmailTV;
    private Switch notificationButton;
    private TextView mNotificationHint;
    private TextView detailsLocationRequestPhotoHint;
    private ImageView favoriteButton;


    private FlowLayout imageFlowLayout;
    private TextView imageFlowHint;
    private Intent intent;
    private ImageView mAnniversaryDescriptionEditBtn;
    private boolean isEditButtonClick;
    private EditText editAnniversaryDescription;
    private ImageView mAnniversaryCancelEdit;
    private AnniversaryModel anniversary;
    private BookLoading bookLoading;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void doTaskAfterPermissionsGranted(int requestCode) {

    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intent = getIntent();
        if (intent == null) {
            return;
        }

        anniversary = intent.getParcelableExtra("anniversary");

        // anniversaryId = intent.getLongExtra("anniversaryId", 0);
        //AnniversaryDao anniversaryDao = anniversaryDaoUtils.getAnniversaryDao();
        //this.anniversary = anniversaryDaoUtils.getAnniversary(anniversaryId);

        initAllWidget();
        updateUI(anniversary);

        //update image flow layout
        List<File> images = FileUtils.readImages(this.getFilesDir() + "/picture/anniversary_" + anniversary.getAnniuuid());
        if (!images.isEmpty()) {
            imageFlowHint.setText("你在过去为此事件添加了如下照片:");
            for (int i = 0; i < images.size(); i++) {
                updateGallery(imageFlowLayout, images.get(i));
            }
        } else {
            imageFlowHint.setText("是否尝试添加照片来记录你的回忆....");
        }

        //update place banner
        List<File> places = ImageUtils.readImages(this.getFilesDir() + "/place/anniversary_" + anniversary.getAnniuuid());
        if (!places.isEmpty()) {
            // updateGallery(placeFlowLayout, place);
            //setBannerResource(placePhotoBanner, places);
            //banner设置方法全部调用完毕时最后调用
            placePhotoBanner.update(places);
            placePhotoBanner.start();
        } else {
            //do network request to get relative place image and save it into local storage
            mGooglePlaceRequestHandler.doPlacePhotoRequest(this, anniversary.getGoogleLocation().getPlaceId(), anniversary.getAnniuuid());
        }
    }


    private void initAllWidget() {

        bookLoading = findViewById(R.id.details_place_loading);
        bookLoading.start();
        placePhotoBanner = findViewById(R.id.details_place_photo_banner);
        //设置图片加载器
        placePhotoBanner.setImageLoader(new DetailsGlideImageLoader());
        placePhotoBanner.setDelayTime(2500);

        detailsAnniProgressbar = findViewById(R.id.details_anni_progressbar);
        imageFlowHint = findViewById(R.id.details_anni_image_flow_hint);

        mTimeProgressLabel1 = findViewById(R.id.time_progress_label1);
        mTimeProgressLabel2 = findViewById(R.id.time_progress_label2);
        mTimeProgressLabel3 = findViewById(R.id.time_progress_label3);


        imageFlowLayout = findViewById(R.id.image_gallery);

        editAnniversaryDescription = findViewById(R.id.anniversary_edit_anni_description);
        mNotificationHint = findViewById(R.id.details_notification_hint);
        mNotificationDateTV = findViewById(R.id.details_notification_date);
        mNotificationTypeTV = findViewById(R.id.details_notification_type);
        mNotificationEmailTV = findViewById(R.id.details_notification_email);

        notificationButton = findViewById(R.id.details_notification_btn);


        mAnniversaryTitleTV = findViewById(R.id.details_anni_title);
        mAnniversaryDateTV = findViewById(R.id.details_anni_date);
        mAnniversaryDescriptionTV = findViewById(R.id.details_anni_description);
        mAnniversaryTypeIV = findViewById(R.id.details_anni_type);

        mAnniversaryDescriptionEditBtn = findViewById(R.id.details_edit_content);
        mAnniversaryDescriptionEditBtn.setOnClickListener(this);

        mAnniversaryCancelEdit = findViewById(R.id.details_cancel_content);
        mAnniversaryCancelEdit.setOnClickListener(this);

        mLocationNameTV = findViewById(R.id.details_location_name);
        mLocationAddressTV = findViewById(R.id.details_location_address);
        mLocationNumberTV = findViewById(R.id.details_location_number);

        ImageView mUpdateCurrentLocation = findViewById(R.id.details_edit_location);
        mUpdateCurrentLocation.setOnClickListener(this);

        detailsLocationRequestPhotoHint = findViewById(R.id.details_location_request_photo_hint);

        Button detailsAddImageButton = findViewById(R.id.details_add_image_btn);
        detailsAddImageButton.setOnClickListener(this);
        favoriteButton = findViewById(R.id.details_icon_favorite);
        favoriteButton.setOnClickListener(this);
        AlphaAnimation alphaAnimationIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationIcon.setDuration(500);

        ImageView detailsImageContent = findViewById(R.id.details_image_content);

        Glide.with(this).load(R.drawable.backgroud4).into(detailsImageContent);
    }

    private void updateUI(AnniversaryModel anniversary) {
        //set anniversary information
        mAnniversaryTitleTV.setText(anniversary.getTitle());

        //set favorite or not
        if (anniversary.isFavorite()) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_black_24px);
            isFavoriteClick = true;
        }


        mAnniversaryDateTV.setText(BaseUtils.formattDate(BaseUtils.parseUTCDateString(anniversary.getDate())));

        String description = anniversary.getDescription();
        if (TextUtils.isEmpty(description)) {
            mAnniversaryDescriptionTV.setText("暂无描述...");
        } else {
            mAnniversaryDescriptionTV.setText(description);
        }

        mAnniversaryTypeIV.setImageResource(anniversary.getAnniversaryTypeModel().getImageResource()

        );


        //纪念日时间
        DateTime anniversaryDateWithJoda = BaseUtils.parseUTCDateString(anniversary.getDate());
        //纪念日创建时间
        DateTime anniversaryCreateDateWithJoda = BaseUtils.parseUTCDateString(anniversary.getCreateDate());
        //当前时间
        DateTime currentDate = DateTime.now(DateTimeZone.getDefault());

        String totalDayString;
        String restDayString;
        String pastDayString = "0";

        int progress = 0;

        if (anniversaryDateWithJoda.isBeforeNow()) {
            //纪念日时间比当前时间要早
            totalDayString = "该事件创建于: " + SimpleDateFormat.getDateInstance().format(anniversaryCreateDateWithJoda.toDate());
            restDayString = "0";
            int days = Days.daysBetween(anniversaryDateWithJoda, currentDate).getDays();
            pastDayString = String.valueOf(days);
            progress = 100;
        } else {
            int totalHour = Hours.hoursBetween(anniversaryCreateDateWithJoda, anniversaryDateWithJoda).getHours();
            int restHour = Hours.hoursBetween(currentDate, anniversaryDateWithJoda).getHours();

            int totalDay = Days.daysBetween(anniversaryCreateDateWithJoda, anniversaryDateWithJoda).getDays();
            totalDayString = "距离事件总共的天数: " + String.valueOf(totalDay);
            int restDay = Days.daysBetween(currentDate, anniversaryDateWithJoda).getDays();


            if (restDay >= 1) {
                restDayString = String.valueOf(restDay);
                progress = (int) ((totalHour - restHour) * 100.0 / totalHour);
            } else if (restDay == 0 && restHour > 0 && restHour < 24) {
                progress = (int) ((totalHour - restHour) * 100.0 / totalHour);
                restDayString = "less than 1 day ";
            } else {
                progress = 100;
                restDayString = "0 day";
            }
        }

        mTimeProgressLabel1.setText(totalDayString);

        String label3 = "距离事件开始还剩下天数: " + restDayString;
        mTimeProgressLabel3.setText(label3);

        String label2 = "距离事件开始已过去天数: " + pastDayString;
        mTimeProgressLabel2.setText(label2);

        detailsAnniProgressbar.setProgress(progress);

        //update location label
        updateLocationLabel(anniversary.getGoogleLocation());


//        // update notification ui
//        NotificationSending notificationSending = anniversary.getNotificationSending();
//        notificationButton.setChecked(notificationSending != null);
//        if (notificationSending != null) {
//            mNotificationDateTV.setText(SimpleDateFormat.getDateInstance().format(notificationSending.getSendingDate()));
//            NotificationType notificationType = notificationSending.getNotificationType();
//            String notificationString = "";
//            switch (notificationType) {
//                case ALL:
//                    notificationString = "系统消息，电子邮件";
//                    break;
//                case EMAIL:
//                    notificationString = "电子邮件";
//                    break;
//                case NOTIFICATION:
//                    notificationString = "系统消息";
//                    break;
//            }
//            mNotificationTypeTV.setText(notificationString);
//        } else {
//            //hide all the notification content
//            mNotificationHint.setVisibility(View.GONE);
//            mNotificationDateTV.setVisibility(View.GONE);
//            mNotificationTypeTV.setVisibility(View.GONE);
//            mNotificationEmailTV.setVisibility(View.GONE);
//
//        }
    }

    private void updateLocationLabel(GoogleLocationModel locationModel) {
        //set location information about location
        if (locationModel != null) {
            String locationName = locationModel.getLocationName();
            if (locationName == null || TextUtils.isEmpty(locationName)) {
                mLocationNameTV.setVisibility(View.GONE);
            } else {
                mLocationNameTV.setText(locationName);
            }

            String locationAddress = locationModel.getLocationAddress();
            if (locationAddress == null || TextUtils.isEmpty(locationAddress)) {
                mLocationAddressTV.setVisibility(View.GONE);
            } else {
                mLocationAddressTV.setText(locationAddress);
            }

            String locationPhoneNumber = locationModel.getLocationPhoneNumber();
            if (locationPhoneNumber == null || TextUtils.isEmpty(locationPhoneNumber)) {
                mLocationNumberTV.setVisibility(View.GONE);
            } else {
                mLocationNumberTV.setText(locationPhoneNumber);
            }
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar);
        mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_cancel_content:
                editAnniversaryDescription.setVisibility(View.GONE);
                mAnniversaryCancelEdit.setVisibility(View.GONE);
                mAnniversaryDescriptionEditBtn.setImageResource(R.drawable.ic_create_black_24px);
                isEditButtonClick = false;
                break;
            case R.id.details_edit_content:
                if (!isEditButtonClick) {
                    mAnniversaryDescriptionEditBtn.setImageResource(R.drawable.ic_save_24px);
                    isEditButtonClick = true;
                    mAnniversaryCancelEdit.setVisibility(View.VISIBLE);
                    editAnniversaryDescription.setText(anniversary.getDescription());
                    editAnniversaryDescription.setVisibility(View.VISIBLE);
                    //光标设置在末尾
                    editAnniversaryDescription.setSelection(anniversary.getDescription().length());
                    editAnniversaryDescription.requestFocus();
                } else {
                    //update database
                    String newDescription = editAnniversaryDescription.getText().toString();
                    anniversary.setDescription(newDescription);
                    Map<String, Object> map = new HashMap<>();
                    map.put("description", newDescription);
                    FirebaseDatabaseHelper.getInstance().updateAnniversaryAttributeByKey(anniversary.getAnniuuid(), map);
                    mAnniversaryDescriptionTV.setText(newDescription);
                    Toast.makeText(this, "编辑成功", Toast.LENGTH_SHORT).show();
                    mAnniversaryCancelEdit.setVisibility(View.GONE);
                    mAnniversaryDescriptionEditBtn.setImageResource(R.drawable.ic_create_black_24px);
                    isEditButtonClick = false;
                    editAnniversaryDescription.setVisibility(View.GONE);
                }
                break;
            case R.id.details_icon_favorite:
                Map<String, Object> map = new HashMap<>();
                if (!isFavoriteClick) {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_black_24px);
                    anniversary.setFavorite(true);
                    isFavoriteClick = true;
                    anniversary.setFavorite(true);
                    map.put("favorite", true);


                } else {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24px);
                    anniversary.setFavorite(false);
                    isFavoriteClick = false;
                    anniversary.setFavorite(false);
                    map.put("favorite", false);
                }
                //anniversaryDaoUtils.getAnniversaryDao().update(anniversary);
                FirebaseDatabaseHelper.getInstance().updateAnniversaryAttributeByKey(anniversary.getAnniuuid(), map);
                break;
            case R.id.details_add_image_btn:
                if (hasPermissionToPickImage(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    MatisseImagePicker.open(DetailsActivity.this, Constant.DETAILS_ACTIVITY_REQUEST_CHOOSE_IMAGE);
                } else {
                    EasyPermissions.requestPermissions(
                            this,
                            "Pick Image",
                            Constant.DETAILS_REQUEST_PICK_IMAGE_PERM,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    );
                }
                break;

            case R.id.details_edit_location:
                mGooglePlaceRequestHandler.openPickerPlaceDialog(this, Constant.PLACE_PICKER_REQUEST);
                break;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.DETAILS_ACTIVITY_REQUEST_CHOOSE_IMAGE:
                if (data == null) {
                    return;
                }
                final List<String> mSelectedFile = Matisse.obtainPathResult(data);
                if (!mSelectedFile.isEmpty()) {
                    for (String file : mSelectedFile) {
                        updateGallery(imageFlowLayout, file);
                    }
                    new Thread(() -> {
                        String destPathParent = DetailsActivity.this.getFilesDir() + "/picture/anniversary_" + anniversary.getAnniuuid();
                        for (String filePath : mSelectedFile) {
                            try {
                                FileUtils.nioCopy(filePath, destPathParent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }).start();
                }
                break;

            case Constant.PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getName() + "/n" + place.getAddress() + "/n" + place.getPhoneNumber());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                    //update google location into database
                    //anniversaryDaoUtils.updateGoogleLocationTable(place, anniversaryId);

                    GoogleLocationModel googleLocationModel = new GoogleLocationModel();
                    googleLocationModel.setLocationName(place.getName() == null ? null : place.getName().toString());
                    googleLocationModel.setLocationAddress(place.getAddress() == null ? null : place.getAddress().toString());
                    googleLocationModel.setLocationPhoneNumber(place.getPhoneNumber() == null ? null : place.getPhoneNumber().toString());
                    googleLocationModel.setPlaceId(place.getId());
                    googleLocationModel.setAttribution(place.getAttributions() == null ? null : place.getAttributions().toString());
                    googleLocationModel.setWebSiteUri(place.getAttributions() == null ? null : place.getAttributions().toString());
                    googleLocationModel.setLatitude(place.getLatLng() == null ? 0 : place.getLatLng().latitude);
                    googleLocationModel.setLongitude(place.getLatLng() == null ? 0 : place.getLatLng().longitude);


                    Map<String, Object> map = new HashMap<>();
                    map.put("googleLocation", googleLocationModel);
                    FirebaseDatabaseHelper.getInstance().updateAnniversaryAttributeByKey(anniversary.getAnniuuid(), map);
                    //update location label
                    updateLocationLabel(googleLocationModel);

                    //download new place photo, if already has old photos, then delete all old
                    FileUtils.deleteAllFile(this.getFilesDir() + "/place/anniversary_" + anniversary.getAnniuuid());
                    // TODO: 2017/12/6  update location, banner image update problem
                    mGooglePlaceRequestHandler.doPlacePhotoRequest(DetailsActivity.this, place.getId(), anniversary.getAnniuuid());
                }
                break;


        }
    }

    private void updateGallery(FlowLayout layout, final Object image) {
        CircleImageView imageView = new CircleImageView(this);


        imageView.setLayoutParams(new FlowLayout.LayoutParams(150, 150));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FlowLayout.LayoutParams layoutParams = (FlowLayout.LayoutParams) imageView.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(17);
        }
        imageView.setPadding(5, 5, 5, 5);
        //imageView.setImageURI(uri);
        if (image instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) image;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Glide.with(this).load(outputStream.toByteArray()).into(imageView);
        } else {
            Glide.with(this).load(image).into(imageView);
        }
        imageView.setOnClickListener(v -> {

            int childCount = imageFlowLayout.getChildCount();
            int index = 0;
            for (int i = 0; i < childCount; i++) {
                CircleImageView currentImage = (CircleImageView) imageFlowLayout.getChildAt(i);
                if (v == currentImage) {
                    index = i;
                }
            }
            Intent intent = new Intent(DetailsActivity.this, AnniPreviewActivity.class);
            intent.putExtra("anniversaryId", anniversary.getAnniuuid());
            intent.putExtra("currentId", index);
            startActivity(intent);
        });
        layout.addView(imageView);
    }

    public void setBannerResource(Banner banner, List<File> images) {
        // banner.stopAutoPlay();
        if (images.isEmpty()) {
            detailsLocationRequestPhotoHint.setText("很遗憾,我们并没有找到有关次地点的相关图片.");
        } else {
            detailsLocationRequestPhotoHint.setText("我们为你找到了一些关于此地点的有趣图片:");
        }

        //设置图片集合
        banner.update(images);
    }


    public void setPlacePhotoBanner() {
        List<File> places = ImageUtils.readImages(this.getFilesDir() + "/place/anniversary_" + anniversary.getAnniuuid());
        setBannerResource(placePhotoBanner, places);
    }

    public void startLoading() {
        bookLoading.start();
        bookLoading.setVisibility(View.VISIBLE);
    }

    public void finishLoading() {
        bookLoading.stop();
        bookLoading.setVisibility(View.GONE);
    }
}
