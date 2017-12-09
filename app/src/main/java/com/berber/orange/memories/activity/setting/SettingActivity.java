package com.berber.orange.memories.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;

import com.berber.orange.memories.R;
import com.berber.orange.memories.SharedPreferencesHelper;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.activity.helper.Constant;
import com.zhihu.matisse.Matisse;

import java.util.List;

public class SettingActivity extends BaseActivity {
    private Toolbar toolbar;
    private SettingFragment settingFragment;

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

        settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction().replace(R.id.setting_frame_content, settingFragment).commit();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.SETTING_FRAGMENT_MAIN_MATISSE_PICKER:
                doTaskAfterPickImage(getMatisseData(data), "main_cover");
                break;
            case Constant.SETTING_FRAGMENT_DETAILS_MATISSE_PICKER:
                doTaskAfterPickImage(getMatisseData(data), "details_cover");
                break;
        }
    }

    private void doTaskAfterPickImage(List<String> matisseData, String positionKey) {
        if (matisseData.isEmpty()) {
            return;
        }
        String pathString = matisseData.get(0);

        //save the path into shared preference
        SharedPreferencesHelper.getInstance().saveData(positionKey, pathString);

        String lastPathSegment = Uri.parse(pathString).getLastPathSegment();
        //change preference ui
        if ("main_cover".equals(positionKey)) {
            settingFragment.mainCoverPreference.setSummary(lastPathSegment);
        } else if ("details_cover".equals(positionKey)) {
            settingFragment.detailsCoverPreference.setSummary(lastPathSegment);
        }

        //main cover, use glide load image

    }

    private List<String> getMatisseData(Intent intent) {
        if (intent != null) {
            return Matisse.obtainPathResult(intent);
        }
        return null;
    }
}
