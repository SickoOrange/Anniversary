package com.berber.orange.memories.dbservice;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;


@Entity
public class Anniversary {

    @Id
    private int id;
    private String Title;
    private String Description;
    private String Location;
    private Date date;
    private boolean isRemind;

    public Anniversary() {
    }

    @Generated(hash = 1133400132)
    public Anniversary(int id, String Title, String Description, String Location, Date date, boolean isRemind) {
        this.id = id;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.date = date;
        this.isRemind = isRemind;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public void setRemind(boolean remind) {
        isRemind = remind;
    }

    public boolean getIsRemind() {
        return this.isRemind;
    }

    public void setIsRemind(boolean isRemind) {
        this.isRemind = isRemind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String
    toString() {
        return "Anniversary{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Location='" + Location + '\'' +
                ", date=" + date +
                ", isRemind=" + isRemind +
                '}';
    }
}
