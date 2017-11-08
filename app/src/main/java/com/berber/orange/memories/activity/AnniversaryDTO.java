package com.berber.orange.memories.activity;

import android.os.Parcel;
import android.os.Parcelable;

import com.berber.orange.memories.dbservice.Anniversary;

import java.util.Date;

public class AnniversaryDTO implements Parcelable {
    private String Title;

    private String Description;

    private String Location;

    private Date date;

    //private Date remindDate;

    private Date createDate;

    private NotificationSendingDTO notificationSendingDTO;

    public AnniversaryDTO() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.Description);
        dest.writeString(this.Location);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeLong(this.createDate != null ? this.createDate.getTime() : -1);
        dest.writeParcelable(this.notificationSendingDTO, flags);
    }

    protected AnniversaryDTO(Parcel in) {
        this.Title = in.readString();
        this.Description = in.readString();
        this.Location = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        long tmpCreateDate = in.readLong();
        this.createDate = tmpCreateDate == -1 ? null : new Date(tmpCreateDate);
        this.notificationSendingDTO = in.readParcelable(NotificationSendingDTO.class.getClassLoader());
    }

    public static final Creator<AnniversaryDTO> CREATOR = new Creator<AnniversaryDTO>() {
        @Override
        public AnniversaryDTO createFromParcel(Parcel source) {
            return new AnniversaryDTO(source);
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public NotificationSendingDTO getNotificationSendingDTO() {
        return notificationSendingDTO;
    }

    public void setNotificationSendingDTO(NotificationSendingDTO notificationSendingDTO) {
        this.notificationSendingDTO = notificationSendingDTO;
    }

    @Override
    public String toString() {
        return "AnniversaryDTO{" +
                "Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Location='" + Location + '\'' +
                ", date=" + date +
                ", createDate=" + createDate +
                ", notificationSendingDTO=" + notificationSendingDTO +
                '}';
    }
}
