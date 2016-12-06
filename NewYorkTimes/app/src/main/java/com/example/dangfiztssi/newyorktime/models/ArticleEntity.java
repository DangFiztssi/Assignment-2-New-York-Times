package com.example.dangfiztssi.newyorktime.models;

import com.example.dangfiztssi.newyorktime.utils.AppContants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by dangfiztssi on 06/12/2016.
 */

public class ArticleEntity extends RealmObject {
    private String url;
    private String snippet;
    private RealmList<MultimediaEntity> multimedia;

    public ArticleEntity() {
    }

    public void map(Article article){
        this.url = article.getUrl();
        this.snippet = article.getSnippet();

        for (Article.Multimedia multimedia : article.getMultimedia()) {
            MultimediaEntity m = new MultimediaEntity();
            m.map(multimedia);
            this.multimedia.add(m);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setMultimedia(RealmList<MultimediaEntity> multimedia) {
        this.multimedia = multimedia;
    }

    public String getUrl() {
        return url;
    }

    public String getSnippet() {
        return snippet;
    }

    public List<MultimediaEntity> getMultimedia() {
        return multimedia;
    }

}
