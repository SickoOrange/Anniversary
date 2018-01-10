package com.berber.orange.memories.database.firebasemodel;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by orange on 2018/1/4.
 */

public class GoogleLocationModel implements Parcelable{

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locationName);
        dest.writeString(this.locationAddress);
        dest.writeString(this.placeId);
        dest.writeString(this.webSiteUri);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeInt(this.rating);
        dest.writeString(this.attribution);
        dest.writeString(this.locationPhoneNumber);
    }

    public GoogleLocationModel() {
    }

    protected GoogleLocationModel(Parcel in) {
        this.locationName = in.readString();
        this.locationAddress = in.readString();
        this.placeId = in.readString();
        this.webSiteUri = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.rating = in.readInt();
        this.attribution = in.readString();
        this.locationPhoneNumber = in.readString();
    }

    public static final Creator<GoogleLocationModel> CREATOR = new Creator<GoogleLocationModel>() {
        @Override
        public GoogleLocationModel createFromParcel(Parcel source) {
            return new GoogleLocationModel(source);
        }

        @Override
        public GoogleLocationModel[] newArray(int size) {
            return new GoogleLocationModel[size];
        }
    };
}
