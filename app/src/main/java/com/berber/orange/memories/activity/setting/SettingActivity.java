package com.berber.orange.memories.activity.setting;

import android.support.v7.widget.Toolbar;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;

public class SettingActivity extends BaseActivity {
    private Toolbar toolbar;

//https://stackoverflow.com/questions/26564400/creating-a-preference-screen-with-support-v21-toolbar

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("System Setting");

        getFragmentManager().beginTransaction().replace(R.id.setting_frame_content, new SettingFragment()).commit();

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar);
        mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    @Override
    protected void doTaskAfterPermissionsGranted(int requestCode) {

    }
}
