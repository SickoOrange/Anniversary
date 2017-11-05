package com.berber.orange.memories.activity;

/**
 * Created by User on 2017/11/05.
 */

class ModelAnniversaryType {
    private String name = "";
    private int imageResource;

    public ModelAnniversaryType(String name, int imageResource) {
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
