package com.berber.orange.memories.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ya yin
 * Created by orange on 2017/11/30.
 */

public class ImageUtils {

    private static InputStream getBitmapStream(Context context, Uri uri) throws FileNotFoundException {
        return context.getContentResolver().openInputStream(uri);

    }


    public static void saveBitmap(Context context, Object resource, File file) throws IOException {
        FileOutputStream out;
        Bitmap decodeSampledBitmap = null;
        if (resource instanceof Uri) {
            Uri uri = (Uri) resource;
            File bitmapFile = new File(uri.toString());
            // decodeSampledBitmap = BitmapUtils.getSmallBitmap(bitmapFile.getAbsolutePath(), 300, 300);
            decodeSampledBitmap = decodeSampledBitmap(getBitmapStream(context, uri), 100, file,300,300);
        } else if (resource instanceof Bitmap) {
            Bitmap sourceBitmap = (Bitmap) resource;
            decodeSampledBitmap = decodeSampledBitmap(sourceBitmap, 100, file,300,300);
        }
        try {
            out = new FileOutputStream(file);
            if (decodeSampledBitmap != null) {
                if (decodeSampledBitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                    out.flush();
                    out.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            file.delete();
        }
        Log.e("TAG", "Finish");
    }


    private static Bitmap decodeSampledBitmap(Object resource, int quality, File file, int reqHeight, int reqWidth) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        try {
            byte[] bytes = new byte[1024];
            if (resource instanceof InputStream) {
                bytes = readStream((InputStream) resource);
                opts.inSampleSize = 1;//设置缩放比例
            } else if (resource instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) resource;
                ByteArrayOutputStream baosTmp = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baosTmp);
                bytes = baosTmp.toByteArray();
                opts.inSampleSize = 1;//设置缩放比例
            }

            opts.inJustDecodeBounds = true;

            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

            int height = opts.outHeight;
            int width = opts.outWidth;
            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                opts.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            opts.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

//            int picWidth = opts.outWidth;// 得到图片宽度
//            int picHeight = opts.outHeight;// 得到图片高度
//            Log.e("原图片高度：", picHeight + "");
//            Log.e("原图片宽度：", picWidth + "");
//
//
//            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
//
//            int picWidth2 = opts.outWidth;// 得到图片宽度
//            int picHeight2 = opts.outHeight;// 得到图片高度
//
//            Log.e("压缩后的图片宽度：", picWidth2 + "");
//            Log.e("压缩后的图片高度：", picHeight2 + "");
//            Log.e("压缩后的图占用内存：", bm.getByteCount() + "");
//
//            // 开始质量压缩
//            baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
//
//            byte[] b = baos.toByteArray();
//            bm = BitmapFactory.decodeByteArray(b, 0, b.length, opts);
//
//            Log.e("质量压缩后的占用内存：", bm.getByteCount() + "");
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
            file.delete();

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


    public static List<File> readImages(String path) {
        List<File> list = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File child : files) {
                list.add(child);
            }
        }
        return list;
    }

    public static File getFile(Context context, Long anniversaryId, String flag) {
        String fileParent = null;
        switch (flag) {
            case "place":
                fileParent = context.getFilesDir() + "/place/anniversary_" + anniversaryId;

                break;
            case "picture":
                fileParent = context.getFilesDir() + "/picture/anniversary_" + anniversaryId;
                break;
        }
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
        File targetFile = new File(fileParent, fileName);
        return targetFile;
    }
}
