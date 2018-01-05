package com.berber.orange.memories.database.firebasemodel;

import com.berber.orange.memories.dbmodel.Anniversary;

import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by orange on 2018/1/4.
 */

public class GoogleLocationModel {

    private String locationName;

    private String locationAddress;

    private String placeId;

    private String webSiteUri;

    private Double latitude;

    private Double longitude;

    private int rating;
    private String attribution;

    private String locationPhoneNumber;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getWebSiteUri() {
        return webSiteUri;
    }

    public void setWebSiteUri(String webSiteUri) {
        this.webSiteUri = webSiteUri;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getLocationPhoneNumber() {
        return locationPhoneNumber;
    }

    public void setLocationPhoneNumber(String locationPhoneNumber) {
        this.locationPhoneNumber = locationPhoneNumber;
    }
}
