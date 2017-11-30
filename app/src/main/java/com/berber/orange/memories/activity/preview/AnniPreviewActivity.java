package com.berber.orange.memories.activity.preview;

import android.graphics.drawable.Drawable;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class AnniPreviewActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initView() {
        super.initView();
        Banner banner = findViewById(R.id.anni_preview_banner);
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.profile);
        images.add(R.drawable.login_bg);
        images.add(R.drawable.couple_love_silhouettes_happiness_116879_1080x1920);
        images.add(R.drawable.header_view);


        //设置图片加载器
        banner.setImageLoader(new AnniImageLoader());
        //设置图片集合
        banner.setImages(images);
        banner.setDelayTime(2500);
        banner.start();
    }

    @Override
    protected void doTaskAfterPermissionsGranted(int requestCode) {

    }
}
