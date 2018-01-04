package com.berber.orange.memories.helper;

import com.berber.orange.memories.dbmodel.Anniversary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orange on 2018/1/4.
 */

public class User {

    private String email;
    private String name;
    private String photoUri;

    private List<Anniversary> anniversaries;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public List<Anniversary> getAnniversaries() {
        return anniversaries;
    }

    public void setAnniversaries(List<Anniversary> anniversaries) {
        this.anniversaries = anniversaries == null ? new ArrayList<Anniversary>() : anniversaries;
    }
}
