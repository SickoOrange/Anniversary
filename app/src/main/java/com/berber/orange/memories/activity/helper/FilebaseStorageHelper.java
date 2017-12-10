package com.berber.orange.memories.activity.helper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.berber.orange.memories.activity.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private BaseActivity baseActivity;

    public FilebaseStorageHelper(Context context) {
        if (!(context instanceof BaseActivity)) {
            throw new ClassCastException("baseActivity cant cast to BaseActivity");
        }
        this.baseActivity = (BaseActivity) context;
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void putFiles() {

        File imagesFolder = new File(baseActivity.getFilesDir(), "picture");
        File placeFolder = new File(baseActivity.getFilesDir(), "place");
        File databasePath = baseActivity.getDatabasePath("anniversary-db");
        if (!imagesFolder.exists() || !placeFolder.exists() || !imagesFolder.isDirectory() || !placeFolder.isDirectory() || !databasePath.exists())
            throw new IllegalArgumentException("parent folder not exist");

        Log.e("TAG", imagesFolder.getAbsolutePath());
        Log.e("TAG", placeFolder.getAbsolutePath());
        Log.e("TAG", databasePath.getAbsolutePath());
        Log.e("TAG", "Ready upload file");
        uploadDatabase(databasePath);

//        //upload database
//        String[] list = parentFileFolder.list();
//        for (
//                String s : list)
//
//        {
//            File file = new File(s);
//            Uri fileUri = Uri.fromFile(file);
//            StorageReference reference = firebaseStorage.getReference().child(placeFlag + "/" + fileUri.getLastPathSegment());
//            // Create file metadata including the content type
//            StorageMetadata metadata = new StorageMetadata.Builder()
//                    .setContentType("image/png")
//                    .build();
//            UploadTask uploadTask = reference.putFile(fileUri, metadata);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.e("TAG", "upload addOnFailureListener");
//
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Log.e("TAG", "upload addOnSuccessListener");
//                }
//            });
//
//        }
    }

    private void uploadDatabase(File file) {
        StorageReference reference = firebaseStorage.getReference().child("database" + "/" + file.getName());
        UploadTask uploadTask = reference.putFile(Uri.fromFile(file));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "upload addOnFailureListener");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("TAG", "upload addOnSuccessListener: "+taskSnapshot.getDownloadUrl());
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.e("TAG", "upload addOnCompleteListener");
            }
        });

    }


}
