package com.berber.orange.memories.model.db;

import com.berber.orange.memories.activity.model.NotificationType;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by orange on 2017/11/8.
 */

@Entity
public class NotificationSending implements Serializable {

    private static final long serialVersionUID = -1411549005283372646L;
    @Id(autoincrement = true)
    private Long id;

    private Date sendingDate;

    private Date sentDate;

    @Convert(converter = NotificationTypeConverter.class, columnType = String.class)
    private NotificationType notificationType;

    private String recipient;

    private Long anniversaryId;

    @ToOne(joinProperty = "anniversaryId")
    private Anniversary anniversary;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1437953480)
    private transient NotificationSendingDao myDao;

    @Generated(hash = 358785660)
    public NotificationSending(Long id, Date sendingDate, Date sentDate,
            NotificationType notificationType, String recipient, Long anniversaryId) {
        this.id = id;
        this.sendingDate = sendingDate;
        this.sentDate = sentDate;
        this.notificationType = notificationType;
        this.recipient = recipient;
        this.anniversaryId = anniversaryId;
    }

    @Generated(hash = 634010185)
    public NotificationSending() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSendingDate() {
        return this.sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Long getAnniversaryId() {
        return this.anniversaryId;
    }

    public void setAnniversaryId(Long anniversaryId) {
        this.anniversaryId = anniversaryId;
    }

    @Generated(hash = 1258510271)
    private transient Long anniversary__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 426657723)
    public Anniversary getAnniversary() {
        Long __key = this.anniversaryId;
        if (anniversary__resolvedKey == null
                || !anniversary__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnniversaryDao targetDao = daoSession.getAnniversaryDao();
            Anniversary anniversaryNew = targetDao.load(__key);
            synchronized (this) {
                anniversary = anniversaryNew;
                anniversary__resolvedKey = __key;
            }
        }
        return anniversary;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 500642566)
    public void setAnniversary(Anniversary anniversary) {
        synchronized (this) {
            this.anniversary = anniversary;
            anniversaryId = anniversary == null ? null : anniversary.getId();
            anniversary__resolvedKey = anniversaryId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public Date getSentDate() {
        return this.sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1984972466)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNotificationSendingDao() : null;
    }


}
