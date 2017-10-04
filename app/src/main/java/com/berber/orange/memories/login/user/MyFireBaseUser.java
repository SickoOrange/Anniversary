package com.berber.orange.memories.login.user;

import android.net.Uri;

import java.net.URI;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public class MyFireBaseUser {
    private String displayName;
    private Uri photoUri;
    private String email;
    private String passworld;
    private String confirmPassworld;
    private String uid;


    public MyFireBaseUser() {
    }


    public String getPassworld() {
        return passworld;
    }

    public void setPassworld(String passworld) {
        this.passworld = passworld;
    }

    public String getConfirmPassworld() {
        return confirmPassworld;
    }

    public void setConfirmPassworld(String confirmPassworld) {
        this.confirmPassworld = confirmPassworld;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
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
