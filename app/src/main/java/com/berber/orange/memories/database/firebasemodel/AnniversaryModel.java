package com.berber.orange.memories.database.firebasemodel;

import com.berber.orange.memories.activity.model.ModelAnniversaryTypeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * model for real time database
 * Created by orange on 2018/1/4.
 */

public class AnniversaryModel {
    private String Title;

    private String Description;

    private String Location;

    private String date;

    private String createDate;


    private boolean favorite;

    private String coverUri;

    private List<String> photos = new ArrayList<>();


    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }


    private ModelAnniversaryTypeDTO anniversaryTypeModel;

    private GoogleLocationModel googleLocation;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public ModelAnniversaryTypeDTO getAnniversaryTypeModel() {
        return anniversaryTypeModel;
    }

    public void setAnniversaryTypeModel(ModelAnniversaryTypeDTO anniversaryTypeModel) {
        this.anniversaryTypeModel = anniversaryTypeModel;
    }

    public GoogleLocationModel getGoogleLocation() {
        return googleLocation;
    }

    public void setGoogleLocation(GoogleLocationModel googleLocation) {
        this.googleLocation = googleLocation;
    }

    @Override
    public String toString() {
        return "AnniversaryModel{" +
                "Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Location='" + Location + '\'' +
                ", date='" + date + '\'' +
                ", createDate='" + createDate + '\'' +
                ", favorite=" + favorite +
                ", coverUri='" + coverUri + '\'' +
                ", photos=" + photos +
                ", anniversaryTypeModel=" + anniversaryTypeModel +
                ", googleLocation=" + googleLocation +
                '}';
    }
}
