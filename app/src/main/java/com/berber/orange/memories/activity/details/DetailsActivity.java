package com.berber.orange.memories.activity.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.model.db.AnniversaryDao;
import com.berber.orange.memories.model.db.DaoSession;
import com.berber.orange.memories.model.db.GoogleLocation;
import com.berber.orange.memories.model.db.GoogleLocationDao;
import com.berber.orange.memories.model.db.NotificationSendingDao;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.youth.banner.Banner;


import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private NumberProgressBar detailsAnniProgressbar;

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
    //private FadingTextView fadingTextView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_details;
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


        Long anniversaryId = intent.getLongExtra("obj", 0);
        List<Anniversary> anniversaryList = anniversaryDao.queryBuilder().where(AnniversaryDao.Properties.Id.eq(anniversaryId)).list();


        placePhotoBanner = findViewById(R.id.details_place_photo_banner);
        // fadingTextView = findViewById(R.id.fadingTextView);

        detailsAnniProgressbar = findViewById(R.id.details_anni_progressbar);
        detailsAnniProgressbar.setProgress(47);


        mAnniversaryTitleTV = findViewById(R.id.details_anni_title);
        mAnniversaryDateTV = findViewById(R.id.details_anni_date);
        mAnniversaryDescriptionTV = findViewById(R.id.details_anni_description);
        mAnniversaryTypeIV = findViewById(R.id.details_anni_type);


        mLocationNameTV = findViewById(R.id.details_location_name);
        mLocationAddressTV = findViewById(R.id.details_location_address);
        mLocationNumberTV = findViewById(R.id.details_location_number);


        if (anniversaryList.size() == 1) {
            Anniversary anniversary = anniversaryList.get(0);
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();


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

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void doPlacePhotoRequest(String placeId, GoogleApiClient googleApiClient) {
        new PlacePhotoTask(this, googleApiClient) {
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
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.setDelayTime(2500);

        banner.setVisibility(View.VISIBLE);
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
