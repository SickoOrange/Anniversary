package com.berber.orange.memories.activity.preview;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.helper.ImageUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.view.BannerViewPager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class AnniPreviewActivity extends BaseActivity {


    private BannerViewPager viewPager;
    private List<File> images;
    private int selectedIndex = -1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    @Override
    protected void initView() {
        super.initView();
        // CheckView checkView = findViewById(R.id.anni_preview_check_view);
        //checkView.setClickable(true);

        Banner banner = findViewById(R.id.anni_preview_banner);


        Intent intent = getIntent();
        if (intent != null) {
            long anniuuid = intent.getLongExtra("anniversaryId", -1);
            int currentId = intent.getIntExtra("currentId", -1);
            images = ImageUtils.readImages(this.getFilesDir() + "/picture/anniversary_" + anniuuid);
            if (!images.isEmpty()) {
                setBannerImageLoader(banner, images);
            }
            try {
                Field declaredField = Banner.class.getDeclaredField("viewPager");
                declaredField.setAccessible(true);
                viewPager = (BannerViewPager) declaredField.get(banner);
                viewPager.setCurrentItem(currentId + 1);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        Button btn = findViewById(R.id.preview_btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedIndex != -1) {
//                    File file = images.get(selectedIndex);
//                    SharedPreferencesHelper.getInstance().saveData("details_picture", file.toURI().toString());
//                }
//                Toast.makeText(AnniPreviewActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void setBannerImageLoader(Banner banner, List<File> images) {

        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.isAutoPlay(false);
        banner.setBannerAnimation(Transformer.Default);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.setDelayTime(2500);
        banner.start();
    }

    @Override
    protected void doTaskAfterPermissionsGranted(int requestCode) {

    }
}
