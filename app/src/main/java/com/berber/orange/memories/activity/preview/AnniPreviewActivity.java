package com.berber.orange.memories.activity.preview;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhihu.matisse.internal.ui.widget.CheckView;

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
        final CheckView checkView = findViewById(R.id.anni_preview_check_view);
        checkView.setClickable(true);

        final Banner banner = findViewById(R.id.anni_preview_banner);
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.profile);
        images.add(R.drawable.login_bg);
        images.add(R.drawable.couple_love_silhouettes_happiness_116879_1080x1920);
        images.add(R.drawable.header_view);

        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.isAutoPlay(false);
        //设置图片加载器
        banner.setImageLoader(new AnniImageLoader());
        //设置图片集合
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.Stack);
        banner.setDelayTime(2500);
        banner.start();


        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                System.out.println(position);
                checkView.setChecked(true);
            }
        });

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println(state);
                System.out.println("onPageScrollStateChanged");
            }
        });

    }

    @Override
    protected void doTaskAfterPermissionsGranted(int requestCode) {

    }
}
