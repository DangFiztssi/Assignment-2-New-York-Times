package com.example.dangfiztssi.newyorktime.models;

import com.example.dangfiztssi.newyorktime.utils.AppContants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DangF on 10/19/16.
 */

public class Article {

    @SerializedName("web_url")
    private String url;

    @SerializedName("snippet")
    private String snippet;

    @SerializedName("multimedia")
    private List<Multimedia> multimedia;

    public Article(ArticleEntity entity){
        this.url = entity.getUrl();
        this.snippet = entity.getSnippet();
        multimedia = new ArrayList<>();
        for (MultimediaEntity m : entity.getMultimedia()) {
            multimedia.add(new Multimedia(m));
        }
    }

    public String getUrl() {
        return url;
    }

    public String getSnippet() {
        return snippet;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    @Override
    public String toString() {
        return "Article{" +
                "url='" + url + '\'' +
                ", snippet='" + snippet + '\'' +
                '}';
    }

    public int getHeight(){
        int result = 0;
        for(Multimedia mul : multimedia){
            if(mul.getHeight() > result)
                result = mul.getHeight();
        }

        return result;
    }


    public class Multimedia{
        @SerializedName("height")
        private int height;

        @SerializedName("url")
        private String url;

        public Multimedia(MultimediaEntity entity){
            this.height = entity.getHeight();
            this.url = entity.getUrl();
        }

        public int getHeight() {
            return height;
        }

        public String getUrl() {
            return AppContants.BASE_URL_IMAGE + url;
        }
    }
}
