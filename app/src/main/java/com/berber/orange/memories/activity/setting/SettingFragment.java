package com.berber.orange.memories.activity.setting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.berber.orange.memories.R;
import com.berber.orange.memories.SharedPreferencesHelper;
import com.berber.orange.memories.helper.Constant;
import com.berber.orange.memories.helper.MatisseImagePicker;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * ya yin
 * Created by orange on 2017/12/8.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private SettingActivity settingActivity;
    public static final String SETTING_MAIN_COVER_PREFERENCE = "setting_main_cover_change";
    public static final String SETTING_DETAILS_COVER_PREFERENCE = "setting_details_cover_change";
    public Preference mainCoverPreference;
    public Preference detailsCoverPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
        mainCoverPreference = findPreference(SETTING_MAIN_COVER_PREFERENCE);
        mainCoverPreference.setOnPreferenceClickListener(this);

        String main_cover = (String) SharedPreferencesHelper.getInstance().getData("main_cover", "未定义");
        mainCoverPreference.setSummary(Uri.parse(main_cover).getLastPathSegment());

        detailsCoverPreference = findPreference(SETTING_DETAILS_COVER_PREFERENCE);
        detailsCoverPreference.setOnPreferenceClickListener(this);

        String details_cover = (String) SharedPreferencesHelper.getInstance().getData("details_cover", "未定义");
        detailsCoverPreference.setSummary(Uri.parse(details_cover).getLastPathSegment());


    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case SETTING_MAIN_COVER_PREFERENCE:
                if (settingActivity.hasPermissionToPickImage(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    MatisseImagePicker.open((AppCompatActivity) this.getActivity(), Constant.SETTING_FRAGMENT_MAIN_MATISSE_PICKER);
                } else {
                    EasyPermissions.requestPermissions(
                            this,
                            "Pick Image",
                            Constant.PERMISSION_PICK_IMAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    );
                }
                break;
            case SETTING_DETAILS_COVER_PREFERENCE:
                if (settingActivity.hasPermissionToPickImage(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    MatisseImagePicker.open((AppCompatActivity) this.getActivity(), Constant.SETTING_FRAGMENT_MAIN_MATISSE_PICKER);
                } else {
                    EasyPermissions.requestPermissions(
                            this,
                            "Pick Image",
                            Constant.PERMISSION_PICK_IMAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    );
                }
                break;
        }

        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingActivity) {
            this.settingActivity = (SettingActivity) context;
        }
    }


}
