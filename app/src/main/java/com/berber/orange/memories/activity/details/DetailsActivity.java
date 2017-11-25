package com.berber.orange.memories.activity.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.model.db.Anniversary;
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
import com.youth.banner.Banner;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private NumberProgressBar detailsAnniProgressbar;
    private RecyclerView timeProgressRecyclerView;

    private final String placeId = "ChIJrTLr-GyuEmsRBfy61i59si0";
    private ImageView imageView;
    private GoogleApiClient mGoogleApiClient;
    private Banner placePhotoBanner;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_details);
//        Intent intent = getIntent();
//        if (intent != null) {
//            Anniversary anniversary = (Anniversary) intent.getSerializableExtra("obj");
//            System.out.println("anniversary: " + anniversary.getTitle());
//        }
//    }

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

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        Button selectedPlace = findViewById(R.id.details_select_place_btn);
        selectedPlace.setOnClickListener(this);

        // imageView = findViewById(R.id.place_image);

        placePhotoBanner = findViewById(R.id.details_place_photo_banner);


        detailsAnniProgressbar = findViewById(R.id.details_anni_progressbar);
        detailsAnniProgressbar.setProgress(47);


        //time progress recycler view
//        timeProgressRecyclerView = findViewById(R.id.time_progress_recycler_view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager .setSmoothScrollbarEnabled(true);
//        linearLayoutManager.setAutoMeasureEnabled(true);
//
//        timeProgressRecyclerView.setHasFixedSize(true);
//        timeProgressRecyclerView.setNestedScrollingEnabled(false);
//        timeProgressRecyclerView.setLayoutManager(linearLayoutManager);
//        timeProgressRecyclerView.setAdapter(new MyTimeProgressAdapter(this));
//        timeProgressRecyclerView.setAdapter(new MyTimeProgressAdapter(this));
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
                //ChIJG7XcqqNXn0cRjP0Qfvclf7A
                // ChIJrTLr-GyuEmsRBfy61i59si0
                //ChIJl5NHB5FXn0cRmUfmpIqCF28
                Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, "ChIJrTLr-GyuEmsRBfy61i59si0").setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                    @Override
                    public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
                        if (!placePhotoMetadataResult.getStatus().isSuccess()) {
                            return;
                        }
                        PlacePhotoMetadataBuffer photoMetadata = placePhotoMetadataResult.getPhotoMetadata();

                        if (photoMetadata.getCount() <= 0) {
                            return;
                        }
                        Log.e("TAG", "find bit map: " + photoMetadata.getCount());
                        final List<Bitmap> bitmaps = new ArrayList<>();
                        final int count = photoMetadata.getCount();
                        for (int i = 0; i < count; i++) {

                            final int finalI = i;
                            photoMetadata.get(i).getPhoto(mGoogleApiClient).setResultCallback(new ResultCallback<PlacePhotoResult>() {
                                @Override
                                public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                                    if (!placePhotoResult.getStatus().isSuccess()) {
                                        return;
                                    }
                                    Bitmap bitmap = placePhotoResult.getBitmap();
                                    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    //  bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    bitmaps.add(bitmap);

                                    if (finalI == count - 1) {
                                        Log.e("TAG", "bit map list load finish" + bitmaps.size());
                                        if (!bitmaps.isEmpty()) {
                                            setBannerImageLoader(placePhotoBanner,bitmaps);
                                        }
                                    }
                                }
                            });
                        }


                        photoMetadata.release();

//                        placePhotoMetadata.getPhoto(mGoogleApiClient).setResultCallback(new ResultCallback<PlacePhotoResult>() {
//                            @Override
//                            public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
//                                if (!placePhotoResult.getStatus().isSuccess()) {
//                                    return;
//                                }
//                                Bitmap bitmap = placePhotoResult.getBitmap();
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//                                Glide.with(DetailsActivity.this).load(stream.toByteArray()).into(new SimpleTarget<GlideDrawable>() {
//                                    @Override
//                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                                        imageView.setImageDrawable(resource);
//                                    }
//                                });
//                            }
//                        });
                    }
                });

                break;
        }
    }

    public void setBannerImageLoader(Banner banner,List<Bitmap> images) {
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.setDelayTime(2500);
        banner.start();
    }
}
