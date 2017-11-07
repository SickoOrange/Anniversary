package com.berber.orange.memories.activity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by orange on 2017/11/7.
 */

public class NotificationSendingDTO implements Parcelable {

    private Date sendingDate;

    private NotificationType notificationType;

    private String recipient;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.sendingDate != null ? this.sendingDate.getTime() : -1);
        dest.writeInt(this.notificationType == null ? -1 : this.notificationType.ordinal());
        dest.writeString(this.recipient);
    }

    public NotificationSendingDTO() {
    }

    protected NotificationSendingDTO(Parcel in) {
        long tmpSendingDate = in.readLong();
        this.sendingDate = tmpSendingDate == -1 ? null : new Date(tmpSendingDate);
        int tmpNotificationType = in.readInt();
        this.notificationType = tmpNotificationType == -1 ? null : NotificationType.values()[tmpNotificationType];
        this.recipient = in.readString();
    }

    public static final Creator<NotificationSendingDTO> CREATOR = new Creator<NotificationSendingDTO>() {
        @Override
        public NotificationSendingDTO createFromParcel(Parcel source) {
            return new NotificationSendingDTO(source);
        }

        @Override
        public NotificationSendingDTO[] newArray(int size) {
            return new NotificationSendingDTO[size];
        }
    };

    public Date getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
