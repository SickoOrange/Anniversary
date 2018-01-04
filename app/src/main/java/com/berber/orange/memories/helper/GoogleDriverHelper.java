package com.berber.orange.memories.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Driver Helper Class
 * Created by orange on 2018/1/4.
 */

public class GoogleDriverHelper {

    private DriveResourceClient driveResourceClient;

    public GoogleDriverHelper(DriveResourceClient driveResourceClient) {
        this.driveResourceClient = driveResourceClient;
    }


    public void createFolder() {
        Log.e("TAG", "Create......");
        Task<DriveFolder> rootFolderTask = driveResourceClient.getRootFolder();
        rootFolderTask.continueWithTask(new Continuation<DriveFolder, Task<DriveFolder>>() {
            @Override
            public Task<DriveFolder> then(@NonNull Task<DriveFolder> task) throws Exception {
                Log.e("TAG", "Create Folder");
                DriveFolder parentFolder = task.getResult();
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("TESTGOOGLE1")
                        .setMimeType(DriveFolder.MIME_TYPE)
                        .setStarred(true)
                        .build();
                return driveResourceClient.createFolder(parentFolder, changeSet);
            }
        }).addOnSuccessListener(new OnSuccessListener<DriveFolder>() {
            @Override
            public void onSuccess(DriveFolder driveFolder) {
                Log.e("TAG", "Create Folder:" + driveFolder.getDriveId().encodeToString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Create Folder Error", e);
            }
        }).addOnCompleteListener(new OnCompleteListener<DriveFolder>() {
            @Override
            public void onComplete(@NonNull Task<DriveFolder> task) {
                Log.e("TAG", "Create Folder:" + task.isSuccessful());
            }
        });
    }

    public void query() {
        Query query = new Query.Builder()
                .addFilter(Filters.and(Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE), Filters.eq(SearchableField.TITLE, "old")))
                .build();

        Task<MetadataBuffer> queryTask = driveResourceClient.query(query);
                queryTask.addOnSuccessListener(new OnSuccessListener<MetadataBuffer>() {
                    @Override
                    public void onSuccess(MetadataBuffer metadata) {
                        Log.e("TAG", "query successful"+ metadata.getCount());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "query failed", e);
                    }
                });
    }
}
