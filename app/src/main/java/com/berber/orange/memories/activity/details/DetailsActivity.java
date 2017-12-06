package com.berber.orange.memories.activity.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.berber.orange.memories.activity.helper.Constant;
import com.berber.orange.memories.activity.helper.ImageUtils;
import com.berber.orange.memories.activity.helper.MatisseImagePicker;
import com.berber.orange.memories.activity.model.NotificationType;
import com.berber.orange.memories.activity.preview.AnniPreviewActivity;
import com.berber.orange.memories.activity.helper.FileUtils;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.model.db.AnniversaryDao;
import com.berber.orange.memories.model.db.GoogleLocation;
import com.berber.orange.memories.model.db.NotificationSending;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.youth.banner.Banner;
import com.zhihu.matisse.Matisse;


import org.apmem.tools.layouts.FlowLayout;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private AlphaAnimation alphaAnimationIcon;


    private FlowLayout imageFlowLayout;
    private Long anniversaryId;
    private TextView imageFlowHint;
    private Anniversary anniversary;
    private Intent intent;
    private ImageView mAnniversaryDecriptionEditBtn;
    private boolean isEditButtonClick;
    private EditText editAnniversaryDescription;
    private ImageView mAnniversaryCancelEdit;
    private ImageView mUpdateCurrentLocation;
    private GoogleLocation googleLocation;

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


        anniversaryId = intent.getLongExtra("anniversaryId", 0);
        AnniversaryDao anniversaryDao = anniversaryDaoUtils.getAnniversaryDao();
        List<Anniversary> anniversaryList = anniversaryDao.queryBuilder().where(AnniversaryDao.Properties.Id.eq(anniversaryId)).list();


        placePhotoBanner = findViewById(R.id.details_place_photo_banner);

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

        mAnniversaryDecriptionEditBtn = findViewById(R.id.details_edit_content);
        mAnniversaryDecriptionEditBtn.setOnClickListener(this);

        mAnniversaryCancelEdit = findViewById(R.id.details_cancel_content);
        mAnniversaryCancelEdit.setOnClickListener(this);

        mLocationNameTV = findViewById(R.id.details_location_name);
        mLocationAddressTV = findViewById(R.id.details_location_address);
        mLocationNumberTV = findViewById(R.id.details_location_number);

        mUpdateCurrentLocation = findViewById(R.id.details_edit_location);
        mUpdateCurrentLocation.setOnClickListener(this);

        detailsLocationRequestPhotoHint = findViewById(R.id.details_location_request_photo_hint);

        Button detailsAddImageButton = findViewById(R.id.details_add_image_btn);
        detailsAddImageButton.setOnClickListener(this);
        favoriteButton = findViewById(R.id.details_icon_favorite);
        favoriteButton.setOnClickListener(this);
        alphaAnimationIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationIcon.setDuration(500);

        ImageView detailsImageContent = findViewById(R.id.details_image_content);
//        String uriString = (String) SharedPreferencesHelper.getInstance().getData("preview_picture", "");
//        if (!TextUtils.isEmpty(uriString)) {
//            Glide.with(this).load(Uri.parse(uriString)).into(detailsImageContent);
//        } else {
//            Glide.with(this).load("http://2.bp.blogspot.com/-SUUnHOeFZO4/ULaBZT2tCVI/AAAAAAAAAgA/khtFfcumLJE/s1600/%E8%87%BA%E5%8C%97%E6%84%9B%E6%83%85%E7%B6%B2+%E6%88%91%E7%9F%A5%E9%81%93%E4%BD%A0%E5%9C%A8%E7%AD%89%E6%88%91.jpg").into(detailsImageContent);
//        }
        Glide.with(this).load(R.drawable.backgroud4).into(detailsImageContent);

        if (anniversaryList.size() == 1) {
            anniversary = anniversaryList.get(0);


            updateUI(anniversary);

            //update image flow layout
            List<File> images = FileUtils.readImages(this.getFilesDir() + "/picture/anniversary_" + anniversary.getId());
            if (!images.isEmpty()) {
                imageFlowHint.setText("你在过去为此事件添加了如下照片:");

                for (int i = 0; i < images.size(); i++) {
                    updateGallery(imageFlowLayout, images.get(i));
                }
            } else {
                imageFlowHint.setText("是否尝试添加照片来记录你的回忆....");
            }

            //update place flow layout
            List<File> places = ImageUtils.readImages(this.getFilesDir() + "/place/anniversary_" + anniversary.getId());
            if (!places.isEmpty()) {
                // updateGallery(placeFlowLayout, place);
                setBannerResource(placePhotoBanner, places);
            } else {
                //do network request to get relative place image and save it into local storage

                // doPlacePhotoRequest(googleLocation.getPlaceId(), googleApiClient);
                mGooglePlaceRequestHandler.doPlacePhotoRequest(this, anniversary.getGoogleLocation().getPlaceId(), String.valueOf(anniversaryId));
            }

        }


    }

    private void updateUI(Anniversary anniversary) {
        //set anniversary information
        String title = anniversary.getTitle();
        mAnniversaryTitleTV.setText(title);

        //set favorite or not
        if (anniversary.getFavorite()) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_black_24px);
            isFavoriteClick = true;
        }

        DateFormat instance = SimpleDateFormat.getDateInstance();
        mAnniversaryDateTV.setText(instance.format(anniversary.getDate()));

        String description = anniversary.getDescription();
        if (TextUtils.isEmpty(description)) {
            mAnniversaryDescriptionTV.setText("暂无描述...");
        } else {
            mAnniversaryDescriptionTV.setText(description);
        }

        mAnniversaryTypeIV.setImageResource(anniversary.getModelAnniversaryType().getImageResource());


        //纪念日时间
        Date anniversaryDate = anniversary.getDate();
        DateTime anniversaryDateWithJoda = new DateTime(anniversaryDate, DateTimeZone.getDefault());
        //纪念日创建时间
        Date createDate = anniversary.getCreateDate();
        DateTime anniversaryCreateDateWithJoda = new DateTime(createDate, DateTimeZone.getDefault());
        //当前时间
        DateTime currentDate = DateTime.now(DateTimeZone.getDefault());

        String totalDayString;
        String restDayString;
        String pastDayString = "0";

        if (anniversaryDateWithJoda.isBeforeNow()) {
            //纪念日时间比当前时间要早
            totalDayString = "该事件创建于: " + SimpleDateFormat.getDateInstance().format(anniversaryCreateDateWithJoda.toDate());
            restDayString = "0";
            int days = Days.daysBetween(anniversaryDateWithJoda, currentDate).getDays();
            pastDayString = String.valueOf(days);
        } else {
            int totalHour = Hours.hoursBetween(anniversaryCreateDateWithJoda, anniversaryDateWithJoda).getHours();
            int restHour = Hours.hoursBetween(currentDate, anniversaryDateWithJoda).getHours();

            int totalDay = Days.daysBetween(anniversaryCreateDateWithJoda, anniversaryDateWithJoda).getDays();
            totalDayString = "距离事件总共的天数: " + String.valueOf(totalDay);
            int restDay = Days.daysBetween(currentDate, anniversaryDateWithJoda).getDays();


            if (restDay >= 1) {
                restDayString = String.valueOf(restDay);
            } else if (restDay == 0 && restHour > 0 && restHour < 24) {
                restDayString = "less than 1 day ";
            } else {
                restDayString = "0 day";
            }
        }

        mTimeProgressLabel1.setText(totalDayString);

        String label3 = "距离事件开始还剩下天数: " + restDayString;
        mTimeProgressLabel3.setText(label3);

        String label2 = "距离事件开始已过去天数: " + pastDayString;
        mTimeProgressLabel2.setText(label2);

        detailsAnniProgressbar.setProgress(intent.getIntExtra("progressValue", 0));

        //set location information about location
        GoogleLocation googleLocation = anniversary.getGoogleLocation();
        if (googleLocation != null) {
            String locationName = googleLocation.getLocationName();
            if (locationName == null || TextUtils.isEmpty(locationName)) {
                mLocationNameTV.setVisibility(View.GONE);
            } else {
                mLocationNameTV.setText(locationName);
            }

            String locationAddress = googleLocation.getLocationAddress();
            if (locationAddress == null || TextUtils.isEmpty(locationAddress)) {
                mLocationAddressTV.setVisibility(View.GONE);
            } else {
                mLocationAddressTV.setText(locationAddress);
            }

            String locationPhoneNumber = googleLocation.getLocationPhoneNumber();
            if (locationPhoneNumber == null || TextUtils.isEmpty(locationPhoneNumber)) {
                mLocationNumberTV.setVisibility(View.GONE);
            } else {
                mLocationNumberTV.setText(locationPhoneNumber);
            }
        }
        // update notification ui
        NotificationSending notificationSending = anniversary.getNotificationSending();
        notificationButton.setChecked(notificationSending != null);
        if (notificationSending != null) {
            mNotificationDateTV.setText(SimpleDateFormat.getDateInstance().format(notificationSending.getSendingDate()));
            NotificationType notificationType = notificationSending.getNotificationType();
            String notificationString = "";
            switch (notificationType) {
                case ALL:
                    notificationString = "系统消息，电子邮件";
                    break;
                case EMAIL:
                    notificationString = "电子邮件";
                    break;
                case NOTIFICATION:
                    notificationString = "系统消息";
                    break;
            }
            mNotificationTypeTV.setText(notificationString);
        } else {
            //hide all the notification content
            mNotificationHint.setVisibility(View.GONE);
            mNotificationDateTV.setVisibility(View.GONE);
            mNotificationTypeTV.setVisibility(View.GONE);
            mNotificationEmailTV.setVisibility(View.GONE);

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
                mAnniversaryDecriptionEditBtn.setImageResource(R.drawable.ic_create_black_24px);
                isEditButtonClick = false;
                break;
            case R.id.details_edit_content:
                if (!isEditButtonClick) {
                    mAnniversaryDecriptionEditBtn.setImageResource(R.drawable.ic_save_24px);
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
                    anniversaryDaoUtils.getAnniversaryDao().update(anniversary);
                    mAnniversaryDescriptionTV.setText(newDescription);
                    Toast.makeText(this, "编辑成功", Toast.LENGTH_SHORT).show();
                    mAnniversaryCancelEdit.setVisibility(View.GONE);
                    mAnniversaryDecriptionEditBtn.setImageResource(R.drawable.ic_create_black_24px);
                    isEditButtonClick = false;
                    editAnniversaryDescription.setVisibility(View.GONE);
                }
                break;
            case R.id.details_icon_favorite:
                if (!isFavoriteClick) {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_black_24px);
                    anniversary.setFavorite(true);

                    isFavoriteClick = true;
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24px);
                    anniversary.setFavorite(false);
                    isFavoriteClick = false;
                }
                anniversaryDaoUtils.getAnniversaryDao().update(anniversary);
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String destPathParent = DetailsActivity.this.getFilesDir() + "/picture/anniversary_" + anniversaryId;
                            for (String filePath : mSelectedFile) {
                                try {
                                    FileUtils.nioCopy(filePath, destPathParent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                    anniversaryDaoUtils.updateGoogleLocationTable(place, anniversaryId);

                    // TODO: 2017/12/6 update location label


                    //download new place photo, if already has old photos, then delete all old
                    FileUtils.deleteAllFile(this.getFilesDir() + "/place/anniversary_" + anniversaryId);
                    //mGooglePlaceRequestHandler.doPlacePhotoRequest(DetailsActivity.this, place.getId(), String.valueOf(anniversaryId));
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int childCount = imageFlowLayout.getChildCount();
                int index = 0;
                for (int i = 0; i < childCount; i++) {
                    CircleImageView currentImage = (CircleImageView) imageFlowLayout.getChildAt(i);
                    if (v == currentImage) {
                        index = i;
                    }
                }
                Intent intent = new Intent(DetailsActivity.this, AnniPreviewActivity.class);
                intent.putExtra("anniversaryId", anniversaryId);
                intent.putExtra("currentId", index);
                startActivity(intent);
            }
        });
        layout.addView(imageView);
    }

    public void setBannerResource(Banner banner, List<File> images) {
        banner.stopAutoPlay();

        if (images.isEmpty()) {
            detailsLocationRequestPhotoHint.setText("很遗憾,我们并没有找到有关次地点的相关图片.");
        } else {
            detailsLocationRequestPhotoHint.setText("我们为你找到了一些关于此地点的有趣图片:");
        }

        // banner.setBannerStyle(BannerConfig.NOT_INDICATOR);

        //设置图片加载器
        banner.setImageLoader(new DetailsGlideImageLoader());

        //设置图片集合
        // banner.setImages(images);
        banner.update(images);
        //banner设置方法全部调用完毕时最后调用
        banner.setDelayTime(2500);
        banner.start();
    }


    public void setPlacePhotoBanner() {
        List<File> places = ImageUtils.readImages(this.getFilesDir() + "/place/anniversary_" + anniversaryId);
        setBannerResource(placePhotoBanner, places);
    }

}
