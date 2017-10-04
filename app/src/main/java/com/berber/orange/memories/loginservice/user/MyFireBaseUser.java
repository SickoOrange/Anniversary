package com.berber.orange.memories.loginservice.user;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public class MyFireBaseUser implements Parcelable {
    private String displayName;
    private String photoUri;
    private String email;
    private String password;
    private String confirmPassword;
    private String uid;


    public MyFireBaseUser() {
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayName);
        parcel.writeString(photoUri);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(confirmPassword);
        parcel.writeString(uid);
    }

    public static final Parcelable.Creator<MyFireBaseUser> CREATOR = new Creator<MyFireBaseUser>() {
        @Override
        public MyFireBaseUser[] newArray(int size) {
            return new MyFireBaseUser[size];
        }

        @Override
        public MyFireBaseUser createFromParcel(Parcel in) {
            return new MyFireBaseUser(in);
        }
    };

    public MyFireBaseUser(Parcel in) {
        displayName = in.readString();
        photoUri = in.readString();
        email = in.readString();
        password = in.readString();
        confirmPassword = in.readString();
        uid = in.readString();
    }
}
