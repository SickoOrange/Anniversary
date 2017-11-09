package com.berber.orange.memories.activity.model;

/**
 * Created by User on 2017/11/05.
 */

public class ModelAnniversaryTypeDTO {
    private String name = "";
    private int imageResource;

    @Override
    public String toString() {
        return "ModelAnniversaryTypeDTO{" +
                "name='" + name + '\'' +
                ", imageResource=" + imageResource +
                '}';
    }

    public ModelAnniversaryTypeDTO(String name, int imageResource) {
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
