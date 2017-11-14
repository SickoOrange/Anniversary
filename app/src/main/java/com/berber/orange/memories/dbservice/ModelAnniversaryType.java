package com.berber.orange.memories.dbservice;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by User on 2017/11/05.
 */

@Entity
public class ModelAnniversaryType implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String name = "";
    private int imageResource;
    @Generated(hash = 1498512602)
    public ModelAnniversaryType(Long id, String name, int imageResource) {
        this.id = id;
        this.name = name;
        this.imageResource = imageResource;
    }
    @Generated(hash = 2004628372)
    public ModelAnniversaryType() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getImageResource() {
        return this.imageResource;
    }
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.imageResource);
    }

    protected ModelAnniversaryType(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.imageResource = in.readInt();
    }

    public static final Creator<ModelAnniversaryType> CREATOR = new Creator<ModelAnniversaryType>() {
        @Override
        public ModelAnniversaryType createFromParcel(Parcel source) {
            return new ModelAnniversaryType(source);
        }

        @Override
        public ModelAnniversaryType[] newArray(int size) {
            return new ModelAnniversaryType[size];
        }
    };
}
