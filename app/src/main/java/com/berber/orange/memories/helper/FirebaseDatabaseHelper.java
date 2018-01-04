package com.berber.orange.memories.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.berber.orange.memories.loginservice.user.MyFireBaseUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * firebase real time database helper class
 * Created by orange on 2018/1/4.
 */

public class FirebaseDatabaseHelper {

    private final FirebaseDatabase database;

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
    }

    public void buildRootUser(FirebaseUser myUser) {
        Log.e("TAG","BUILD ROOT USER");
        DatabaseReference reference = database.getReference("users/");
        User user = new User();
        user.setAnniversaries(null);
        user.setEmail(myUser.getEmail());
        user.setName(myUser.getDisplayName());
        user.setPhotoUri(myUser.getPhotoUrl().toString());
        reference.child(myUser.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("TAG","SUCCESS");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG","Failure");
            }
        });
    }
}
