package com.berber.orange.memories.helper.firebasemodel;

/**
 * Created by orange on 2018/1/4.
 */

public class AnniversaryTypeModel {
    private String name = "";
    private int imageResource = -1;


    public AnniversaryTypeModel(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
