package com.berber.orange.memories.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.helper.Constant;
import com.berber.orange.memories.activity.helper.MatisseImagePicker;

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

        detailsCoverPreference = findPreference(SETTING_DETAILS_COVER_PREFERENCE);
        detailsCoverPreference.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case SETTING_MAIN_COVER_PREFERENCE:
                MatisseImagePicker.open((AppCompatActivity) this.getActivity(), Constant.SETTING_FRAGMENT_MAIN_MATISSE_PICKER);
                break;
            case SETTING_DETAILS_COVER_PREFERENCE:
                MatisseImagePicker.open((AppCompatActivity) this.getActivity(), Constant.SETTING_FRAGMENT_DETAILS_MATISSE_PICKER);
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
