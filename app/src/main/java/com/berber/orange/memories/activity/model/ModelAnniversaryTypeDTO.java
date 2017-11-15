package com.berber.orange.memories.activity.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 2017/11/05.
 */

public class ModelAnniversaryTypeDTO implements Parcelable {
    private String name = "";
    private int imageResource;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.imageResource);
    }

    public ModelAnniversaryTypeDTO() {
    }

    public ModelAnniversaryTypeDTO(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    protected ModelAnniversaryTypeDTO(Parcel in) {
        this.name = in.readString();
        this.imageResource = in.readInt();
    }

    public static final Creator<ModelAnniversaryTypeDTO> CREATOR = new Creator<ModelAnniversaryTypeDTO>() {
        @Override
        public ModelAnniversaryTypeDTO createFromParcel(Parcel source) {
            return new ModelAnniversaryTypeDTO(source);
        }

        @Override
        public ModelAnniversaryTypeDTO[] newArray(int size) {
            return new ModelAnniversaryTypeDTO[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
