package com.berber.orange.memories.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.helper.Constant;
import com.berber.orange.memories.activity.helper.MatisseImagePicker;

/**
 * ya yin
 * Created by orange on 2017/12/8.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private SettingActivity settingActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
        Preference setting_main_cover_change = findPreference("setting_main_cover_change");
        setting_main_cover_change.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "setting_main_cover_change":
                MatisseImagePicker.open((AppCompatActivity) this.getActivity(), Constant.SETTING_FRAGMENT_MAIN_MATISSE_PICKER);
                break;
            case "setting_details_cover_change":
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
