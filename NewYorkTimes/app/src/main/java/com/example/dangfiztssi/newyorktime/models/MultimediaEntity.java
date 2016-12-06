package com.example.dangfiztssi.newyorktime.models;

import io.realm.RealmObject;

/**
 * Created by dangfiztssi on 06/12/2016.
 */
public class MultimediaEntity extends RealmObject {
    private int height;
    private String url;

    public MultimediaEntity() {}

    public void map(Article.Multimedia multimedia){
        this.height = multimedia.getHeight();
        this.url = multimedia.getUrl();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
