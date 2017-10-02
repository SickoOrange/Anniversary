package com.berber.orange.memories.login.user;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public class MyFireBaseUser {
    private String displayName;
    private String photoUri;
    private String email;
    private String uid;


    public MyFireBaseUser() {
    }


    public MyFireBaseUser(String displayName, String photoUri, String email, String uid) {

        this.displayName = displayName;
        this.photoUri = photoUri;
        this.email = email;
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
