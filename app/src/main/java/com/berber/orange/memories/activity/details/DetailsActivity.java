package com.berber.orange.memories.activity.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.activity.MatisseImagePicker;
import com.berber.orange.memories.activity.model.NotificationType;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.model.db.AnniversaryDao;
import com.berber.orange.memories.model.db.DaoSession;
import com.berber.orange.memories.model.db.GoogleLocation;
import com.berber.orange.memories.model.db.GoogleLocationDao;
import com.berber.orange.memories.model.db.NotificationSending;
import com.berber.orange.memories.model.db.NotificationSendingDao;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.youth.banner.Banner;
import com.zhihu.matisse.Matisse;


import org.apmem.tools.layouts.FlowLayout;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;


public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private NumberProgressBar detailsAnniProgressbar;
    private boolean isFavoriteClick;

    private final String placeId = "ChIJrTLr-GyuEmsRBfy61i59si0";
    private Banner placePhotoBanner;
    private GoogleApiClient googleApiClient;
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

    private static final int DETAILS_ACTIVITY_REQUEST_CHOOSE_IMAGE = 45;
    private int DETAILS_REQUEST_PICK_IMAGE_PERM = 109;
    private FlowLayout imageFlowLayout;
    //private TagFlowLayout imagesFlowLayout;
    //private FadingTextView fadingTextView;

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

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        DaoSession daoSession = ((APP) getApplication()).getDaoSession();
        AnniversaryDao anniversaryDao = daoSession.getAnniversaryDao();
        NotificationSendingDao notificationSendingDao = daoSession.getNotificationSendingDao();
        GoogleLocationDao googleLocationDao = daoSession.getGoogleLocationDao();


        Long anniversaryId = intent.getLongExtra("anniversaryId", 0);
        List<Anniversary> anniversaryList = anniversaryDao.queryBuilder().where(AnniversaryDao.Properties.Id.eq(anniversaryId)).list();

        int progressValue = intent.getIntExtra("progressValue", 0);

        String dateInformation = intent.getStringExtra("dateInformation");

        placePhotoBanner = findViewById(R.id.details_place_photo_banner);
        // fadingTextView = findViewById(R.id.fadingTextView);

        detailsAnniProgressbar = findViewById(R.id.details_anni_progressbar);
        detailsAnniProgressbar.setProgress(progressValue);

        mTimeProgressLabel1 = findViewById(R.id.time_progress_label1);
        mTimeProgressLabel2 = findViewById(R.id.time_progress_label2);
        mTimeProgressLabel3 = findViewById(R.id.time_progress_label3);


        imageFlowLayout = findViewById(R.id.image_gallery);
//        imageFlowLayout.setAdapter(new GridViewAdapter(this, null));

        updateDateInformationUI(dateInformation);

        mNotificationHint = findViewById(R.id.details_notification_hint);
        mNotificationDateTV = findViewById(R.id.details_notification_date);
        mNotificationTypeTV = findViewById(R.id.details_notification_type);
        mNotificationEmailTV = findViewById(R.id.details_notification_email);

        notificationButton = findViewById(R.id.details_notification_btn);


        mAnniversaryTitleTV = findViewById(R.id.details_anni_title);
        mAnniversaryDateTV = findViewById(R.id.details_anni_date);
        mAnniversaryDescriptionTV = findViewById(R.id.details_anni_description);
        mAnniversaryTypeIV = findViewById(R.id.details_anni_type);


        mLocationNameTV = findViewById(R.id.details_location_name);
        mLocationAddressTV = findViewById(R.id.details_location_address);
        mLocationNumberTV = findViewById(R.id.details_location_number);

        detailsLocationRequestPhotoHint = findViewById(R.id.details_location_request_photo_hint);
        // imagesFlowLayout = findViewById(R.id.details_images_flow);

        Button detailsAddImageButton = findViewById(R.id.details_add_image_btn);
        detailsAddImageButton.setOnClickListener(this);
        favoriteButton = findViewById(R.id.details_icon_favorite);
        favoriteButton.setOnClickListener(this);
        alphaAnimationIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationIcon.setDuration(500);

        ImageView detailsImageContent = findViewById(R.id.details_image_content);
        Glide.with(this).load(R.drawable.baby2).into(detailsImageContent);


        if (anniversaryList.size() == 1) {
            Anniversary anniversary = anniversaryList.get(0);
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
            updateUI(anniversary);
        }

    }

    private void updateUI(Anniversary anniversary) {
        //set anniversary information
        String title = anniversary.getTitle();
        mAnniversaryTitleTV.setText(title);

        DateFormat instance = SimpleDateFormat.getDateInstance();
        mAnniversaryDateTV.setText(instance.format(anniversary.getDate()));

        String description = anniversary.getDescription();
        if (TextUtils.isEmpty(description)) {
            mAnniversaryDescriptionTV.setText("你暂时还没有对此次事件添加对应的描述");
        } else {
            mAnniversaryDescriptionTV.setText(description);
        }

        mAnniversaryTypeIV.setImageResource(anniversary.getModelAnniversaryType().getImageResource());


        GoogleLocation googleLocation = anniversary.getGoogleLocation();
        //get place photo
        doPlacePhotoRequest(googleLocation.getPlaceId(), googleApiClient);

        //set location information about location
        mLocationNameTV.setText(googleLocation.getLocationName());
        mLocationAddressTV.setText(googleLocation.getLocationAddress());
        mLocationNumberTV.setText(googleLocation.getLocationPhoneNumber());

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

    private void updateDateInformationUI(String dateInformation) {
        if (!TextUtils.isEmpty(dateInformation) && dateInformation.contains("/")) {
            String[] split = dateInformation.split("/");
            String total = split[1];
            String rest = split[0];
            int past = Integer.valueOf(total) - Integer.valueOf(rest);
            mTimeProgressLabel1.setText(String.format("距离事件开始还有%s天", total));

            if (past < 0) {
                mTimeProgressLabel2.setText(String.format("距离事件已经过去了%s天", String.valueOf(past * -1)));
                mTimeProgressLabel3.setText(String.format("距离事件开始还剩下%s天", "0"));
            } else {
                mTimeProgressLabel2.setText(String.format("距离事件已经过去了%s天", String.valueOf(past)));
                mTimeProgressLabel3.setText(String.format("距离事件开始还剩下%s天", rest));
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
            case R.id.details_icon_favorite:
                System.out.println("click");
                if (!isFavoriteClick) {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_black_24px);
                    //  favoriteButton.setAnimation(alphaAnimationIcon);
                    isFavoriteClick = true;
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24px);
                    //favoriteButton.setAnimation(alphaAnimationIcon);
                    isFavoriteClick = false;
                }
                break;
            case R.id.details_add_image_btn:
                if (hasPermissionToPickImage(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    MatisseImagePicker.open(DetailsActivity.this, DETAILS_ACTIVITY_REQUEST_CHOOSE_IMAGE);
                } else {
                    EasyPermissions.requestPermissions(
                            this,
                            "Pick Image",
                            DETAILS_REQUEST_PICK_IMAGE_PERM,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    );
                    break;
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DETAILS_ACTIVITY_REQUEST_CHOOSE_IMAGE:
                if (data == null) {
                    return;
                }
                List<Uri> mSelected = Matisse.obtainResult(data);
                if (!mSelected.isEmpty()) {
//                    GridViewAdapter adapter = (GridViewAdapter) imageFlowLayout.getAdapter();
//                    adapter.addNewItems(mSelected);
                    CircleImageView imageView = (CircleImageView) new ImageView(this);
                    imageView.setLayoutParams(new FlowLayout.LayoutParams(50, 50));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setPadding(5, 5, 5, 5);
                    imageView.setImageURI(mSelected.get(0));
                }
                break;


        }
    }

    @SuppressLint("StaticFieldLeak")
    private void doPlacePhotoRequest(String placeId, GoogleApiClient googleApiClient) {
        new PlacePhotoTask(DetailsActivity.this, googleApiClient) {
            @Override
            protected void onPreExecute() {
                // TODO: 2017/11/25  do some loading prepare work
            }

            @Override
            protected void onPostExecute(List<AttributedPhoto> attributedPhotos) {
                List<Bitmap> list = new ArrayList<>();
                if (!attributedPhotos.isEmpty()) {
                    for (AttributedPhoto photo : attributedPhotos) {
                        list.add(photo.bitmap);
                    }
                }
                setBannerImageLoader(placePhotoBanner, list);

            }
        }.execute(placeId);
    }

    public void setBannerImageLoader(Banner banner, List<Bitmap> images) {

        if (images.isEmpty()) {
            detailsLocationRequestPhotoHint.setText("很遗憾,我们并没有找到有关次地点的相关图片.");
        } else {
            detailsLocationRequestPhotoHint.setText("我们为你找到了一些关于此地点的有趣图片.");
        }

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.setDelayTime(2500);

        banner.start();
    }

    static class PlacePhotoTask extends AsyncTask<String, Void, List<PlacePhotoTask.AttributedPhoto>> {

        private final WeakReference<DetailsActivity> mTarget;

        private GoogleApiClient mGoogleApiClient;

        public PlacePhotoTask(DetailsActivity activity, GoogleApiClient GoogleApiClient) {
            mTarget = new WeakReference<>(activity);
            this.mGoogleApiClient = GoogleApiClient;
        }

        @Override
        protected List<PlacePhotoTask.AttributedPhoto> doInBackground(String... strings) {
            if (strings.length != 1) {
                return null;
            }

            //get place id
            final String placeId = strings[0];

            AttributedPhoto attributedPhoto = null;

            PlacePhotoMetadataResult result = Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId).await();
            List<AttributedPhoto> list = new ArrayList<>();
            if (result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadata = result.getPhotoMetadata();
                if (photoMetadata.getCount() > 0 && !isCancelled()) {

                    int count = photoMetadata.getCount();
                    for (int i = 0; i < count; i++) {
                        PlacePhotoMetadata placePhotoMetadata = photoMetadata.get(i);
                        CharSequence attributions = placePhotoMetadata.getAttributions();
                        Bitmap bitmap = placePhotoMetadata.getPhoto(mGoogleApiClient).await().getBitmap();
                        attributedPhoto = new AttributedPhoto(attributions, bitmap);
                        list.add(attributedPhoto);
                    }
                }
                photoMetadata.release();
            }

            return list;
        }

        class AttributedPhoto {
            final CharSequence attribution;
            final Bitmap bitmap;

            AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
                this.attribution = attribution;
                this.bitmap = bitmap;
            }
        }
    }

}
