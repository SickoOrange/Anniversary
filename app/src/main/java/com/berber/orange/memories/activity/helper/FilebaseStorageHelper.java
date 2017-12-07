package com.berber.orange.memories.activity.helper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * ya yin
 * Created by orange on 2017/12/7.
 */

public class FilebaseStorageHelper {

    private FirebaseStorage firebaseStorage;

    private Context context;

    public FilebaseStorageHelper(Context context) {
        this.context = context;
    }

    public void putFiles(String parentPath, String placeFlag) {

        File parentFileFolder = new File(parentPath);
        if (parentFileFolder.exists()) {
            throw new IllegalArgumentException("parent folder not exist");
        }

        String[] list = parentFileFolder.list();
        for (String s : list) {
            File file = new File(s);
            Uri fileUri = Uri.fromFile(file);
            StorageReference reference = firebaseStorage.getReference().child(placeFlag + "/" + fileUri.getLastPathSegment());
            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/png")
                    .build();
            UploadTask uploadTask = reference.putFile(fileUri, metadata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "upload addOnFailureListener");

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("TAG", "upload addOnSuccessListener");
                }
            });

        }
    }


}
