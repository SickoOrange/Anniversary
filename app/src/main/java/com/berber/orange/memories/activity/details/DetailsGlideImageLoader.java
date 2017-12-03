package com.berber.orange.memories.activity.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import java.io.ByteArrayOutputStream;

/**
 * Created by orange on 2017/11/25.
 */

public class DetailsGlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        Glide.with(context).load(path).centerCrop().into(imageView);
    }
}
