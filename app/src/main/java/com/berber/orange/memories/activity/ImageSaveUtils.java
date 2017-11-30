package com.berber.orange.memories.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ya yin
 * Created by orange on 2017/11/30.
 */

public class ImageSaveUtils {
    public static Bitmap getBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public static void saveBitmap(Context context, Bitmap bitmap, Long anniversaryId) throws IOException {
        String fileParent = context.getFilesDir() + "/picture/anniversary_" + anniversaryId;
        File obj = new File(fileParent);
        if (!obj.exists()) {
            obj.mkdirs();
        }
        int length = 0;
        if (obj.isDirectory()) {
            File[] listFiles = obj.listFiles();
            length = listFiles.length;
        }
        String fileName = "anni_" + length + ".png";

        File file = new File(fileParent, fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        Bitmap decodeSampledBitmap = decodeSampledBitmap(getISFromBitmap(bitmap), 100);
        try {
            out = new FileOutputStream(file);
            if (decodeSampledBitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap.recycle();
        decodeSampledBitmap.recycle();
    }

    private static InputStream getISFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return new ByteArrayInputStream(baos.toByteArray());
    }

    ;

    private static Bitmap decodeSampledBitmap(InputStream ins, int quality) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        try {
            byte[] bytes = readStream(ins);

            opts.inJustDecodeBounds = true;

            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

            opts.inJustDecodeBounds = false;

            int picWidth = opts.outWidth;// 得到图片宽度
            int picHeight = opts.outHeight;// 得到图片高度
            Log.e("原图片高度：", picHeight + "");
            Log.e("原图片宽度：", picWidth + "");

            opts.inSampleSize = 2;//设置缩放比例

            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

            int picWidth2 = opts.outWidth;// 得到图片宽度
            int picHeight2 = opts.outHeight;// 得到图片高度

            Log.e("压缩后的图片宽度：", picWidth2 + "");
            Log.e("压缩后的图片高度：", picHeight2 + "");
            Log.e("压缩后的图占用内存：", bm.getByteCount() + "");

            // 开始质量压缩
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, quality, baos);

            byte[] b = baos.toByteArray();
            bm = BitmapFactory.decodeByteArray(b, 0, b.length, opts);

            Log.e("质量压缩后的占用内存：", bm.getByteCount() + "");
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e1) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    /*
 * 得到图片字节流 数组大小
 * */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }
}
