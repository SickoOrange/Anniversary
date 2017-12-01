package com.berber.orange.memories.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
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
    public static InputStream getBitmapStream(Context context, Uri uri) throws FileNotFoundException {
//        Bitmap bitmap = null;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver().get, uri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return context.getContentResolver().openInputStream(uri);

    }


    public static void saveBitmap(Context context, Uri uri, File file) throws IOException {
        FileOutputStream out;
        Bitmap decodeSampledBitmap = decodeSampledBitmap(getBitmapStream(context, uri), 100, file);
        try {
            out = new FileOutputStream(file);
            if (decodeSampledBitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            file.delete();
        }
        Log.e("TAG", "Finish");
    }

    private static InputStream getISFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return new ByteArrayInputStream(baos.toByteArray());
    }

//    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri imageUri, int reqWidth, int reqHeight) throws FileNotFoundException {
//
//        // Get input stream of the image
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        InputStream iStream = null;
//        try {
//            iStream = context.getContentResolver().openInputStream(imageUri);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(iStream, null, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeStream(iStream, null, options);
//    }

//    private static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) >= reqHeight
//                    && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }


    private static Bitmap decodeSampledBitmap(InputStream ins, int quality, File file) {

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

            opts.inSampleSize = 4;//设置缩放比例

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

    public static File getFile(Context context, Long anniversaryId) {

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
        File targetFile = new File(fileParent, fileName);
        return targetFile;
    }
}
