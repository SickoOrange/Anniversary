package com.berber.orange.memories.model.db;

import com.berber.orange.memories.dbservice.AnniversaryDao;
import com.berber.orange.memories.dbservice.DaoSession;
import com.berber.orange.memories.dbservice.ModelAnniversaryTypeDao;
import com.berber.orange.memories.dbservice.NotificationSendingDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


@Entity
public class Anniversary implements Serializable {


    private static final long serialVersionUID = 4820432240728394100L;
    @Id(autoincrement = true)
    private Long id;

    private String Title;

    private String Description;

    private String Location;

    private Date date;

    private Date createDate;

    private Long modelAnniversaryTypeId;

    @ToOne(joinProperty = "modelAnniversaryTypeId")
    private ModelAnniversaryType modelAnniversaryType;

    private Long notificationSendingId;

    @ToOne(joinProperty = "notificationSendingId")
    private NotificationSending notificationSending;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1172854706)
    private transient AnniversaryDao myDao;

    @Generated(hash = 1092846448)
    public Anniversary(Long id, String Title, String Description, String Location,
                       Date date, Date createDate, Long modelAnniversaryTypeId,
                       Long notificationSendingId) {
        this.id = id;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.date = date;
        this.createDate = createDate;
        this.modelAnniversaryTypeId = modelAnniversaryTypeId;
        this.notificationSendingId = notificationSendingId;
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

    public Long getModelAnniversaryTypeId() {
        return this.modelAnniversaryTypeId;
    }

    public void setModelAnniversaryTypeId(Long modelAnniversaryTypeId) {
        this.modelAnniversaryTypeId = modelAnniversaryTypeId;
    }

    public Long getNotificationSendingId() {
        return this.notificationSendingId;
    }

    public void setNotificationSendingId(Long notificationSendingId) {
        this.notificationSendingId = notificationSendingId;
    }

    @Generated(hash = 684027712)
    private transient Long modelAnniversaryType__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1637771025)
    public ModelAnniversaryType getModelAnniversaryType() {
        Long __key = this.modelAnniversaryTypeId;
        if (modelAnniversaryType__resolvedKey == null
                || !modelAnniversaryType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ModelAnniversaryTypeDao targetDao = daoSession
                    .getModelAnniversaryTypeDao();
            ModelAnniversaryType modelAnniversaryTypeNew = targetDao.load(__key);
            synchronized (this) {
                modelAnniversaryType = modelAnniversaryTypeNew;
                modelAnniversaryType__resolvedKey = __key;
            }
        }
        return modelAnniversaryType;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1722141903)
    public void setModelAnniversaryType(ModelAnniversaryType modelAnniversaryType) {
        synchronized (this) {
            this.modelAnniversaryType = modelAnniversaryType;
            modelAnniversaryTypeId = modelAnniversaryType == null ? null
                    : modelAnniversaryType.getId();
            modelAnniversaryType__resolvedKey = modelAnniversaryTypeId;
        }
    }

    @Generated(hash = 1392270529)
    private transient Long notificationSending__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 829092209)
    public NotificationSending getNotificationSending() {
        Long __key = this.notificationSendingId;
        if (notificationSending__resolvedKey == null
                || !notificationSending__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NotificationSendingDao targetDao = daoSession
                    .getNotificationSendingDao();
            NotificationSending notificationSendingNew = targetDao.load(__key);
            synchronized (this) {
                notificationSending = notificationSendingNew;
                notificationSending__resolvedKey = __key;
            }
        }
        return notificationSending;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1016003634)
    public void setNotificationSending(NotificationSending notificationSending) {
        synchronized (this) {
            this.notificationSending = notificationSending;
            notificationSendingId = notificationSending == null ? null
                    : notificationSending.getId();
            notificationSending__resolvedKey = notificationSendingId;
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

    @Override
    public String toString() {
        return "Anniversary{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Location='" + Location + '\'' +
                ", date=" + date +
                ", createDate=" + createDate +
                ", modelAnniversaryTypeId=" + modelAnniversaryTypeId +
                ", modelAnniversaryType=" + modelAnniversaryType +
                ", notificationSendingId=" + notificationSendingId +
                ", notificationSending=" + notificationSending +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                ", modelAnniversaryType__resolvedKey=" + modelAnniversaryType__resolvedKey +
                ", notificationSending__resolvedKey=" + notificationSending__resolvedKey +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1868176712)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnniversaryDao() : null;
    }
}
