package com.berber.orange.memories.activity;

import android.os.Parcel;
import android.os.Parcelable;

import com.berber.orange.memories.dbservice.Anniversary;

import java.util.Date;

public class AnniversaryDTO implements Parcelable{
    private String Title;

    private String Description;

    private String Location;

    private Date date;

    private Date remindDate;

    private Date createDate;

    public AnniversaryDTO(){}

    protected AnniversaryDTO(Parcel in) {
        Title = in.readString();
        Description = in.readString();
        Location = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        remindDate = tmpDate != -1 ? new Date(tmpDate) : null;
        createDate = tmpDate != -1 ? new Date(tmpDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeString(Location);
        dest.writeLong(date != null ? date.getTime() : -1L);
        dest.writeLong(remindDate != null ? remindDate.getTime() : -1L);
        dest.writeLong(createDate != null ? createDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AnniversaryDTO> CREATOR = new Parcelable.Creator<AnniversaryDTO>() {
        @Override
        public AnniversaryDTO createFromParcel(Parcel in) {
            return new AnniversaryDTO(in);
        }

        @Override
        public AnniversaryDTO[] newArray(int size) {
            return new AnniversaryDTO[size];
        }
    };


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
