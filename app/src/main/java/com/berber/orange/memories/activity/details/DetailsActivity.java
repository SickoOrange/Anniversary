package com.berber.orange.memories.activity.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.model.db.AnniversaryDao;
import com.berber.orange.memories.model.db.DaoSession;
import com.berber.orange.memories.model.db.GoogleLocation;
import com.berber.orange.memories.model.db.GoogleLocationDao;
import com.berber.orange.memories.model.db.NotificationSendingDao;
import com.berber.orange.memories.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.ads.reward.mediation.InitializableMediationRewardedVideoAdAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.tomer.fadingtextview.FadingTextView;
import com.youth.banner.Banner;
import com.youth.banner.WeakHandler;

import org.greenrobot.greendao.query.Query;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private NumberProgressBar detailsAnniProgressbar;

    private final String placeId = "ChIJrTLr-GyuEmsRBfy61i59si0";
    private Banner placePhotoBanner;
    private GoogleApiClient googleApiClient;
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
        Anniversary anniversary = (Anniversary) intent.getSerializableExtra("obj");
        Long locationDatabaseId = anniversary.getGoogleLocationId();


        Button selectedPlace = findViewById(R.id.details_select_place_btn);
        selectedPlace.setOnClickListener(this);

        placePhotoBanner = findViewById(R.id.details_place_photo_banner);
        // fadingTextView = findViewById(R.id.fadingTextView);

        detailsAnniProgressbar = findViewById(R.id.details_anni_progressbar);
        detailsAnniProgressbar.setProgress(47);

        List<GoogleLocation> list = googleLocationDao.queryBuilder().where(GoogleLocationDao.Properties.Id.eq(locationDatabaseId)).build().list();

        if (list.size() == 1) {
            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
            //get place photo
            doPlacePhotoRequest(list.get(0).getPlaceId(), googleApiClient);
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
            case R.id.details_select_place_btn:
                //doPlacePhotoRequest(placeId);
                break;
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
                        Log.e("TAG", "AttributedPhoto: " + attributions.toString());
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
