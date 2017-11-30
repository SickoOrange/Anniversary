package com.berber.orange.memories.activity.preview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * ya yin
 * Created by orange on 2017/11/30.
 */

public class AnniImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (path instanceof Integer) {
            int pathId = (int) path;
            Glide.with(context).load(pathId).into(imageView);
        }
    }
}
