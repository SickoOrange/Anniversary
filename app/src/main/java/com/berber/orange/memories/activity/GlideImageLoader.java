package com.berber.orange.memories.activity;

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

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Log.e("TAG", "display image in banner");


       imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (path instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) path;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(context).load(stream.toByteArray()).fitCenter().into(imageView);
        } else {
            Glide.with(context).load(path).fitCenter().into(imageView);
        }

    }
}
