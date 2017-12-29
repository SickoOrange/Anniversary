package com.berber.orange.memories.activity.helper;

import com.berber.orange.memories.APP;
import com.berber.orange.memories.dbmodel.Anniversary;
import com.berber.orange.memories.dbmodel.AnniversaryDao;
import com.berber.orange.memories.dbmodel.DaoSession;
import com.berber.orange.memories.dbmodel.GoogleLocation;
import com.berber.orange.memories.dbmodel.GoogleLocationDao;
import com.berber.orange.memories.dbmodel.NotificationSendingDao;
import com.google.android.gms.location.places.Place;

import java.util.List;

/**
 * ya yin
 * Created by orange on 2017/12/6.
 */

public class AnniversaryDaoUtils {

    private AnniversaryDao anniversaryDao;
    private final NotificationSendingDao notificationSendingDao;
    private final GoogleLocationDao googleLocationDao;

    public AnniversaryDaoUtils(APP app) {
        DaoSession daoSession = app.getDaoSession();
        anniversaryDao = daoSession.getAnniversaryDao();
        notificationSendingDao = daoSession.getNotificationSendingDao();
        googleLocationDao = daoSession.getGoogleLocationDao();
    }

    public Anniversary getAnniversary(Long id) {
        List<Anniversary> list = anniversaryDao.queryBuilder().where(AnniversaryDao.Properties.Id.eq(id)).list();
        if (list.size() != 1) {
            throw new IllegalArgumentException("getAnnivesary() list count must equal 1");
        }
        return list.get(0);
    }


    public void updateGoogleLocationTable(Place place, Long anniversaryId) {
        Anniversary anniversary = getAnniversary(anniversaryId);
        GoogleLocation googleLocation = anniversary.getGoogleLocation();
        if (googleLocation == null) {
            throw new IllegalArgumentException("googleLocation can't be null");
        }
        googleLocation.setLocationName(place.getName() == null ? null : place.getName().toString());
        googleLocation.setLocationAddress(place.getAddress() == null ? null : place.getAddress().toString());
        googleLocation.setLocationPhoneNumber(place.getPhoneNumber() == null ? null : place.getPhoneNumber().toString());
        googleLocation.setPlaceId(place.getId());
        googleLocation.setAttribution(place.getAttributions() == null ? null : place.getAttributions().toString());
        googleLocation.setWebSiteUri(place.getAttributions() == null ? null : place.getAttributions().toString());
        googleLocation.setLatitude(place.getLatLng() == null ? 0 : place.getLatLng().latitude);
        googleLocation.setLongitude(place.getLatLng() == null ? 0 : place.getLatLng().longitude);

        googleLocationDao.update(googleLocation);
    }


    public AnniversaryDao getAnniversaryDao() {
        return anniversaryDao;
    }

    public NotificationSendingDao getNotificationSendingDao() {
        return notificationSendingDao;
    }
}
