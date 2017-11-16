package com.berber.orange.memories.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by orange on 2017/11/15.
 */

@Entity
public class GoogleLocation {
    @Id(autoincrement = true)
    private Long id;

    private String locationName;

    private String locationAdress;

    private String locationPhoneNumber;

    private Long anniversaryId;

    @ToOne(joinProperty = "anniversaryId")
    private Anniversary anniversary;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 603161226)
    private transient GoogleLocationDao myDao;

    @Generated(hash = 450972880)
    public GoogleLocation(Long id, String locationName, String locationAdress,
            String locationPhoneNumber, Long anniversaryId) {
        this.id = id;
        this.locationName = locationName;
        this.locationAdress = locationAdress;
        this.locationPhoneNumber = locationPhoneNumber;
        this.anniversaryId = anniversaryId;
    }

    @Generated(hash = 863264045)
    public GoogleLocation() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAdress() {
        return this.locationAdress;
    }

    public void setLocationAdress(String locationAdress) {
        this.locationAdress = locationAdress;
    }

    public String getLocationPhoneNumber() {
        return this.locationPhoneNumber;
    }

    public void setLocationPhoneNumber(String locationPhoneNumber) {
        this.locationPhoneNumber = locationPhoneNumber;
    }

    public Long getAnniversaryId() {
        return this.anniversaryId;
    }

    public void setAnniversaryId(Long anniversaryId) {
        this.anniversaryId = anniversaryId;
    }

    @Generated(hash = 1258510271)
    private transient Long anniversary__resolvedKey;

    /** To-one relationship, resolved on first access. */
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

    /** called by internal mechanisms, do not call yourself. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1750685977)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGoogleLocationDao() : null;
    }




}
