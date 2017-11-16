package com.berber.orange.memories.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by User on 2017/11/05.
 */

@Entity
public class ModelAnniversaryType implements Serializable{
    private static final long serialVersionUID = 6780163633004994127L;
    @Id(autoincrement = true)
    private Long id;
    private String name = "";
    private int imageResource;
    @Generated(hash = 1498512602)
    public ModelAnniversaryType(Long id, String name, int imageResource) {
        this.id = id;
        this.name = name;
        this.imageResource = imageResource;
    }
    @Generated(hash = 2004628372)
    public ModelAnniversaryType() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getImageResource() {
        return this.imageResource;
    }
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }


}
