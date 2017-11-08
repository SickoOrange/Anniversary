package com.berber.orange.memories.dbservice;

import com.berber.orange.memories.activity.NotificationType;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by orange on 2017/11/8.
 */

@Entity
public class NotificationSending {

    @Id(autoincrement = true)
    private Long id;

    private Date sendingDate;

    @Convert(converter = NotificationTypeConverter.class, columnType = String.class)
    private NotificationType notificationType;

    private String recipient;

    @ToOne
    private Anniversary anniversary;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1437953480)
    private transient NotificationSendingDao myDao;

    @Generated(hash = 799519026)
    public NotificationSending(Long id, Date sendingDate,
            NotificationType notificationType, String recipient) {
        this.id = id;
        this.sendingDate = sendingDate;
        this.notificationType = notificationType;
        this.recipient = recipient;
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

    @Generated(hash = 1146833423)
    private transient boolean anniversary__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 551425946)
    public Anniversary getAnniversary() {
        if (anniversary != null || !anniversary__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnniversaryDao targetDao = daoSession.getAnniversaryDao();
            targetDao.refresh(anniversary);
            anniversary__refreshed = true;
        }
        return anniversary;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 583497642)
    public Anniversary peakAnniversary() {
        return anniversary;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2020844778)
    public void setAnniversary(Anniversary anniversary) {
        synchronized (this) {
            this.anniversary = anniversary;
            anniversary__refreshed = true;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1984972466)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNotificationSendingDao() : null;
    }

 
    
}
