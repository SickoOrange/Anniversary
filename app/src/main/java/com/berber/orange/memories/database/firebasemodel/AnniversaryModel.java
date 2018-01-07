package com.berber.orange.memories.database.firebasemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.berber.orange.memories.activity.model.ModelAnniversaryTypeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * model for real time database
 * Created by orange on 2018/1/4.
 */

public class AnniversaryModel implements Parcelable {
    private String Title;

    private String Description;

    private String Location;

    private String date;

    private String createDate;


    private boolean favorite;

    private String coverUri;

    private List<String> photos = new ArrayList<>();
    private String anniuuid;


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

    public void setAnniuuid(String anniuuid) {
        this.anniuuid = anniuuid;
    }

    public String getAnniuuid() {
        return anniuuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.Description);
        dest.writeString(this.Location);
        dest.writeString(this.date);
        dest.writeString(this.createDate);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.coverUri);
        dest.writeStringList(this.photos);
        dest.writeString(this.anniuuid);
        dest.writeParcelable(this.anniversaryTypeModel, flags);
        dest.writeParcelable(this.googleLocation, flags);
    }

    public AnniversaryModel() {
    }

    protected AnniversaryModel(Parcel in) {
        this.Title = in.readString();
        this.Description = in.readString();
        this.Location = in.readString();
        this.date = in.readString();
        this.createDate = in.readString();
        this.favorite = in.readByte() != 0;
        this.coverUri = in.readString();
        this.photos = in.createStringArrayList();
        this.anniuuid = in.readString();
        this.anniversaryTypeModel = in.readParcelable(ModelAnniversaryTypeDTO.class.getClassLoader());
        this.googleLocation = in.readParcelable(GoogleLocationModel.class.getClassLoader());
    }

    public static final Creator<AnniversaryModel> CREATOR = new Creator<AnniversaryModel>() {
        @Override
        public AnniversaryModel createFromParcel(Parcel source) {
            return new AnniversaryModel(source);
        }

        @Override
        public AnniversaryModel[] newArray(int size) {
            return new AnniversaryModel[size];
        }
    };
}
