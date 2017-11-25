package com.berber.orange.memories.activity.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.youth.banner.loader.ImageLoader;

import java.io.ByteArrayOutputStream;

/**
 * Created by orange on 2017/11/25.
 */

class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Log.e("TAG","display image in banner");
        Bitmap bitmap = (Bitmap) path;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(context).load(stream.toByteArray()).into(imageView);
    }
}
