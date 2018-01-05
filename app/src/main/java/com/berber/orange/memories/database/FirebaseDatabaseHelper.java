package com.berber.orange.memories.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.berber.orange.memories.SharedPreferencesHelper;
import com.berber.orange.memories.helper.User;
import com.berber.orange.memories.database.databaseinterface.ChildEventClass;
import com.berber.orange.memories.database.firebasemodel.AnniversaryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * firebase real time database helper class
 * Created by orange on 2018/1/4.
 */

public class FirebaseDatabaseHelper {

    private final FirebaseDatabase database;

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        String uuid = (String) SharedPreferencesHelper.getInstance().getData("user_uuid", "undefined");
        DatabaseReference reference = database.getReference("users" + "/" + uuid + "anniversaries");
    }

    public void buildRootUser(FirebaseUser myUser) {
        Log.e("TAG", "BUILD ROOT USER");
        DatabaseReference reference = database.getReference("users/");
        User user = new User();
        user.setAnniversaries(null);
        user.setEmail(myUser.getEmail());
        user.setName(myUser.getDisplayName());
        user.setPhotoUri(myUser.getPhotoUrl().toString());

        Map<String, Object> map = new HashMap<>();
        map.put(myUser.getUid(), user);
        reference
                .updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("TAG", "SUCCESS");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Failure");
                    }
                });
    }

    public List<AnniversaryModel> querybyChildAttribute(String attribute) {
        if (attribute == null || "".equals(attribute)) {
            throw new IllegalArgumentException("query child attribute can't be null");
        }
        final List<AnniversaryModel> list = new ArrayList<>();
        getAnniversariesReference().orderByChild(attribute).addChildEventListener(new ChildEventClass() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                super.onChildAdded(dataSnapshot, s);
                AnniversaryModel model = dataSnapshot.getValue(AnniversaryModel.class);
                list.add(model);
            }
        });
        return list;
    }

    public List<AnniversaryModel> querybyKey() {
        final List<AnniversaryModel> list = new ArrayList<>();
        getAnniversariesReference().orderByKey().addChildEventListener(new ChildEventClass() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                super.onChildAdded(dataSnapshot, s);
                AnniversaryModel model = dataSnapshot.getValue(AnniversaryModel.class);
                list.add(model);
            }
        });
        return list;
    }

    public List<AnniversaryModel> querybyValue() {
        final List<AnniversaryModel> list = new ArrayList<>();
        getAnniversariesReference().orderByValue().addChildEventListener(new ChildEventClass() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                super.onChildAdded(dataSnapshot, s);
                AnniversaryModel model = dataSnapshot.getValue(AnniversaryModel.class);
                list.add(model);
            }
        });
        return list;
    }


    private String getUserUUID() {
        String uuid = (String) SharedPreferencesHelper.getInstance().getData("user_uuid", "undefined");
        if ("undefinded".equals(uuid)) {
            throw new IllegalStateException("can't find user's uuid");
        }
        return uuid;
    }

    private DatabaseReference getAnniversariesReference() {
        return database.getReference("users/" + getUserUUID() + "/anniversaries");
    }
}
