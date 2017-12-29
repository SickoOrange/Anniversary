package com.berber.orange.memories.dbmodel;



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

    private boolean favorite;

    private String coverUri;

    @ToOne(joinProperty = "modelAnniversaryTypeId")
    private ModelAnniversaryType modelAnniversaryType;

    private Long notificationSendingId;

    @ToOne(joinProperty = "notificationSendingId")
    private NotificationSending notificationSending;


    private Long googleLocationId;

    @ToOne(joinProperty = "googleLocationId")
    private GoogleLocation googleLocation;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1172854706)
    private transient AnniversaryDao myDao;

    @Generated(hash = 1312294671)
    public Anniversary(Long id, String Title, String Description, String Location,
            Date date, Date createDate, Long modelAnniversaryTypeId,
            boolean favorite, String coverUri, Long notificationSendingId,
            Long googleLocationId) {
        this.id = id;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.date = date;
        this.createDate = createDate;
        this.modelAnniversaryTypeId = modelAnniversaryTypeId;
        this.favorite = favorite;
        this.coverUri = coverUri;
        this.notificationSendingId = notificationSendingId;
        this.googleLocationId = googleLocationId;
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

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getCoverUri() {
        return this.coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public Long getNotificationSendingId() {
        return this.notificationSendingId;
    }

    public void setNotificationSendingId(Long notificationSendingId) {
        this.notificationSendingId = notificationSendingId;
    }

    public Long getGoogleLocationId() {
        return this.googleLocationId;
    }

    public void setGoogleLocationId(Long googleLocationId) {
        this.googleLocationId = googleLocationId;
    }

    @Generated(hash = 684027712)
    private transient Long modelAnniversaryType__resolvedKey;

    /** To-one relationship, resolved on first access. */
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

    /** called by internal mechanisms, do not call yourself. */
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

    /** To-one relationship, resolved on first access. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1016003634)
    public void setNotificationSending(NotificationSending notificationSending) {
        synchronized (this) {
            this.notificationSending = notificationSending;
            notificationSendingId = notificationSending == null ? null
                    : notificationSending.getId();
            notificationSending__resolvedKey = notificationSendingId;
        }
    }

    @Generated(hash = 427662291)
    private transient Long googleLocation__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1314426589)
    public GoogleLocation getGoogleLocation() {
        Long __key = this.googleLocationId;
        if (googleLocation__resolvedKey == null
                || !googleLocation__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GoogleLocationDao targetDao = daoSession.getGoogleLocationDao();
            GoogleLocation googleLocationNew = targetDao.load(__key);
            synchronized (this) {
                googleLocation = googleLocationNew;
                googleLocation__resolvedKey = __key;
            }
        }
        return googleLocation;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1669804237)
    public void setGoogleLocation(GoogleLocation googleLocation) {
        synchronized (this) {
            this.googleLocation = googleLocation;
            googleLocationId = googleLocation == null ? null
                    : googleLocation.getId();
            googleLocation__resolvedKey = googleLocationId;
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
