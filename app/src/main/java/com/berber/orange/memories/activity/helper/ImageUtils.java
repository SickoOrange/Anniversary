package com.berber.orange.memories.activity.helper;

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
import java.util.UUID;

/**
 * ya yin
 * Created by orange on 2017/11/30.
 * <p>
 * 图片处理类
 *
 * @author Ricko
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
            // File bitmapFile = new File(uri.toString());
            // decodeSampledBitmap = BitmapUtils.getSmallBitmap(bitmapFile.getAbsolutePath(), 300, 300);
            decodeSampledBitmap = decodeSampledBitmap(getBitmapStream(context, uri), 100, file, 1080, 720);
        } else if (resource instanceof Bitmap) {
            Bitmap sourceBitmap = (Bitmap) resource;
            decodeSampledBitmap = decodeSampledBitmap(sourceBitmap, 100, file, 1080, 720);
        }
        try {
            out = new FileOutputStream(file);
            if (decodeSampledBitmap != null) {
                if (decodeSampledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
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
                //opts.inSampleSize = 1;
            }
            opts.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

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
        String fileName = "anni_" + UUID.randomUUID() + ".png";
        File targetFile = new File(fileParent, fileName);
        return targetFile;
    }

    public static boolean deleteFile(Context context, Long anniversaryId, String flag, int index) {
        String fileParent = null;

        switch (flag) {
            case "picture":
                fileParent = context.getFilesDir() + "/picture/anniversary_" + anniversaryId;
                break;
        }

        File obj = new File(fileParent);

        if (obj.exists()) {
            String fileName = "anni_" + index + ".png";
            File targetFile = new File(fileParent, fileName);
            return targetFile.delete();
        }

        return false;
    }
}
