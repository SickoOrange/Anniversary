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
import com.google.firebase.storage.OnProgressListener;
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

    public void SyncToCloud() {

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
        uploadImages(imagesFolder, "picture");
        uploadImages(placeFolder, "place");
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
                Log.e("TAG", "upload addOnSuccessListener: " + taskSnapshot.getDownloadUrl());
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.e("TAG", "upload addOnCompleteListener");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long bytesTransferred = taskSnapshot.getBytesTransferred();
                Log.e("TAG", "upload on progress:  " + String.valueOf(bytesTransferred));
            }
        });

    }

    private void uploadImages(File parentFolder, String position) {
        StorageReference reference = firebaseStorage.getReference().child(position + "/");
        String[] subFolders = parentFolder.list();
        for (String subFolder : subFolders) {
            String lastPathSegment = Uri.parse(subFolder).getLastPathSegment();
            StorageReference storageReference = reference.child(lastPathSegment + "/");
            File file = new File(parentFolder, subFolder);
            if (file.isDirectory()) {
                String[] strings = file.list();
                for (String string : strings) {
                    Uri parse = Uri.fromFile(new File(file, string));
                    String lastPathSegment1 = parse.getLastPathSegment();
                    StorageReference childReference = storageReference.child(lastPathSegment1);
                    UploadTask uploadTask = childReference.putFile(parse);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Log.e("TAG", "on upload images complete " + task.getResult().getDownloadUrl());
                        }
                    });
                }
            }
        }
    }

}
