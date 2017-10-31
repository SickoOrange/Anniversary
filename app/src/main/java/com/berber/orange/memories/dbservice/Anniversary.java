package com.berber.orange.memories.dbservice;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class Anniversary {

    @Id(autoincrement = true)
    private Long id;

    private String Title;

    private String Description;

    private String Location;

    private Date date;

    private Date remindDate;

    private Date createDate;



    public Anniversary() {
    }



    @Generated(hash = 1130164621)
    public Anniversary(Long id, String Title, String Description, String Location,
            Date date, Date remindDate, Date createDate) {
        this.id = id;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.date = date;
        this.remindDate = remindDate;
        this.createDate = createDate;
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



    public Date getRemindDate() {
        return this.remindDate;
    }



    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }



    public Date getCreateDate() {
        return this.createDate;
    }



    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    
}
