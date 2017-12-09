package com.berber.orange.memories.activity.helper;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;

import com.berber.orange.memories.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.SelectionCreator;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

/**
 * ya yin
 * Created by orange on 2017/11/27.
 */

public class MatisseImagePicker {
    public static void open(AppCompatActivity activity, int requestCode) {
        Matisse.from(activity)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .maxSelectable(requestCode == Constant.DETAILS_ACTIVITY_REQUEST_CHOOSE_IMAGE ? 9 : 1)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(requestCode);
    }

}
