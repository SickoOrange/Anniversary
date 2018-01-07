package com.berber.orange.memories.database;

import android.icu.lang.UScript;
import android.util.Log;

import com.berber.orange.memories.SharedPreferencesHelper;
import com.berber.orange.memories.helper.User;
import com.berber.orange.memories.database.firebasemodel.AnniversaryModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * firebase real time database helper class
 * Created by orange on 2018/1/4.
 */

public class FirebaseDatabaseHelper {

    private static FirebaseDatabaseHelper instance;
    private final FirebaseDatabase database;


    private FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
    }

    public static synchronized void init() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
    }

    public static FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            throw new RuntimeException("class should init!");
        }
        return instance;
    }

    public void buildRootUser(FirebaseUser myUser) {
        Log.e("TAG", "BUILD ROOT USER");
        DatabaseReference reference = database.getReference("users").child(getUserUUID());

        Map<String, Object> map = new HashMap<>();
//        User user = new User();
//        user.setPhotoUri(myUser.getPhotoUrl().toString());
//        user.setName(myUser.getDisplayName());
//        user.setEmail(myUser.getEmail());
        map.put("photoUri", myUser.getPhotoUrl().toString());
        reference
                .updateChildren(map, (databaseError, databaseReference) -> {
                    map.clear();
                    map.put("Email",myUser.getEmail());
                    map.put("Name",myUser.getDisplayName());
                    databaseReference.updateChildren(map,(databaseError1,databaseReference1)->Log.e("TAG","update info finish"));
                });

    }

    public void deleteChildByKey(final String key) {
        if (key == null || "".equals(key)) {
            throw new IllegalArgumentException("query child attribute can't be null");
        }

        getAnniversaryReference(key).removeValue((databaseError, databaseReference) -> Log.e("TAG", "delete anniversary id: " + key + " Successfully"));
    }

    public void updateAnniversaryAttributeByKey(final String key, Map<String, Object> map) {
        if (key == null || "".equals(key)) {
            throw new IllegalArgumentException("query child attribute can't be null");
        }
        getAnniversaryReference(key).updateChildren(map, (databaseError, databaseReference) -> Log.e("TAG", "update attribute with anniversary id: " + key + " Successfully"));
    }

//    public void updateAnniversaryAttributeByKey(final String key, HashMap<String, Object> map) {
//        if (key == null || "".equals(key)) {
//            throw new IllegalArgumentException("query child attribute can't be null");
//        }
//        getAnniversaryReference(key).updateChildren(map, (databaseError, databaseReference) -> Log.e("TAG", "update attribute with anniversary id: " + key + " Successfully"));
//    }

//    public void queryByChildAttribute(String attr) {
//        if (attr == null || "".equals(attr)) {
//            throw new IllegalArgumentException("query child attribute can't be null");
//        }
//        getAnniversariesReference().orderByChild(attr)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        List<AnniversaryModel> list = new ArrayList<>();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            AnniversaryModel model = snapshot.getValue(AnniversaryModel.class);
//                            if (model != null) {
//                                model.setAnniuuid(snapshot.getKey());
//                                list.add(model);
//                            }
//                        }
//                        if (list.size() == dataSnapshot.getChildrenCount()) {
//                           // queryResultListener.queryResult(list);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//    }

//    public List<AnniversaryModel> querybyKey() {
//        final List<AnniversaryModel> list = new ArrayList<>();
//        getAnniversariesReference().orderByKey().addChildEventListener(new ChildEventListenerWrapper() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                AnniversaryModel model = dataSnapshot.getValue(AnniversaryModel.class);
//                list.add(model);
//            }
//        });
//        return list;
//    }
//
//    public List<AnniversaryModel> querybyValue() {
//        final List<AnniversaryModel> list = new ArrayList<>();
//        getAnniversariesReference().orderByValue().addChildEventListener(new ChildEventListenerWrapper() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                AnniversaryModel model = dataSnapshot.getValue(AnniversaryModel.class);
//                list.add(model);
//            }
//        });
//        return list;
//    }


    private String getUserUUID() {
        String uuid = (String) SharedPreferencesHelper.getInstance().getData("user_uuid", "undefined");
        if ("undefinded".equals(uuid)) {
            throw new IllegalStateException("can't find user's uuid");
        }
        return uuid;
    }

    public DatabaseReference getAnniversariesReference() {
        return database.getReference("users/" + getUserUUID() + "/anniversaries");
    }

    private DatabaseReference getAnniversaryReference(String key) {
        return database.getReference("users/" + getUserUUID() + "/anniversaries" + "/" + key);
    }


    public Map<String, List<AnniversaryModel>> groupData(List<AnniversaryModel> list) {
        Map<String, List<AnniversaryModel>> sortedMap = new LinkedHashMap<>();
        com.annimon.stream.Stream<AnniversaryModel> stream = com.annimon.stream.Stream.of(list);
        stream
                .sortBy(model -> DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(model.getDate()).withZone(DateTimeZone.getDefault()))
                .chunkBy(model -> {
                    String date = model.getDate();
                    DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(date).withZone(DateTimeZone.getDefault());
                    return dateTime.getMonthOfYear() + "-" + dateTime.getYear();
                })
                .forEach(subList -> {
                    AnniversaryModel model = subList.get(0);
                    DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(model.getDate()).withZone(DateTimeZone.getDefault());
                    sortedMap.put(dateTime.getMonthOfYear() + "-" + dateTime.getYear(), subList);
                });
        return sortedMap;
    }

    public Map<ItemType, List<Object>> flateDate(Map<String, List<AnniversaryModel>> map) {
        Map<ItemType, List<Object>> positionMapping = new HashMap<>();
        positionMapping.put(ItemType.DATE, new ArrayList<>());
        positionMapping.put(ItemType.NONE, new ArrayList<>());
        positionMapping.put(ItemType.ALL, new ArrayList<>());
        positionMapping.put(ItemType.HEAD, new ArrayList<>());
        positionMapping.put(ItemType.TAIL, new ArrayList<>());
        positionMapping.put(ItemType.DATA, new ArrayList<>());
        for (Map.Entry<String, List<AnniversaryModel>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<AnniversaryModel> value = entry.getValue();

            positionMapping.get(ItemType.DATE).add(key);

            if (value.size() == 1) {
                //NONE
                positionMapping.get(ItemType.NONE).add(value.get(0));
            } else {
                int listSize = value.size();
                positionMapping.get(ItemType.HEAD).add(value.get(0));
                positionMapping.get(ItemType.TAIL).add(value.get(listSize - 1));
                for (int i = 1; i < listSize - 1; i++) {
                    positionMapping.get(ItemType.ALL).add(value.get(i));
                }
            }
            positionMapping.get(ItemType.DATA).add(value.get(0).getDate());
            positionMapping.get(ItemType.DATA).addAll(value);
        }
        return positionMapping;
    }

    public ItemType getItemType(Object o, Map<ItemType, List<Object>> map) {
        List<ItemType> itemTypes = Arrays.asList(ItemType.HEAD, ItemType.TAIL, ItemType.ALL, ItemType.NONE);
        for (ItemType type : itemTypes) {
            List<Object> list = map.get(type);
            if (list.contains(o)) {
                return type;
            }
        }
        return ItemType.UNDEFINED;
    }


}



