package com.berber.orange.memories.dbservice;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;


@Entity
public class Anniversary {

    @Id(autoincrement = true)
    private Long id;

    private String Title;

    private String Description;

    private String Location;

    private Date date;

    private Date createDate;

    @ToOne
    private NotificationSending notificationSending;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1172854706)
    private transient AnniversaryDao myDao;

    @Generated(hash = 1013947693)
    public Anniversary(Long id, String Title, String Description, String Location,
            Date date, Date createDate) {
        this.id = id;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.date = date;
        this.createDate = createDate;
    }

    @Generated(hash = 302179509)
    public Anniversary() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getLocation() {
        return this.Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Generated(hash = 167227976)
    private transient boolean notificationSending__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1748885429)
    public NotificationSending getNotificationSending() {
        if (notificationSending != null || !notificationSending__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NotificationSendingDao targetDao = daoSession
                    .getNotificationSendingDao();
            targetDao.refresh(notificationSending);
            notificationSending__refreshed = true;
        }
        return notificationSending;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 1057740344)
    public NotificationSending peakNotificationSending() {
        return notificationSending;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 564910311)
    public void setNotificationSending(NotificationSending notificationSending) {
        synchronized (this) {
            this.notificationSending = notificationSending;
            notificationSending__refreshed = true;
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
    @Generated(hash = 1868176712)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnniversaryDao() : null;
    }


}
