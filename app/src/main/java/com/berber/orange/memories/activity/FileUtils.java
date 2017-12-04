package com.berber.orange.memories.activity;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * ya yin
 * java NIO file copy
 * Created by orange on 2017/12/4.
 */

public class FileUtils {

    public static void nioCopy(String sourcePath, String destPathParent) throws Exception {

        File source = new File(sourcePath);
        if (!source.exists()) {
            return;
        }
        String destName = source.getName();
        File destPathParentFolder = new File(destPathParent);
        if (!destPathParentFolder.exists()) {
            destPathParentFolder.mkdirs();
        }
        File dest = new File(destPathParentFolder, destName);

        if (!dest.exists()) {

            dest.createNewFile();

        }

        FileInputStream fis = new FileInputStream(source);

        FileOutputStream fos = new FileOutputStream(dest);

        FileChannel sourceCh = fis.getChannel();

        FileChannel destCh = fos.getChannel();

        MappedByteBuffer mbb = sourceCh.map(FileChannel.MapMode.READ_ONLY, 0, sourceCh.size());

        destCh.write(mbb);

        sourceCh.close();
        destCh.close();
        Log.e("TAG", "write file finish: " + destName);
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

}
