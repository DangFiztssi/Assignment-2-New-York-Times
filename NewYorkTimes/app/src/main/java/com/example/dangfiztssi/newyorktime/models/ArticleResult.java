package com.example.dangfiztssi.newyorktime.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DangF on 10/20/16.
 */

public class ArticleResult {
    @SerializedName("docs")
    List<Article> articleList;

    public List<Article> getArticleList() {
        return articleList;
    }
}
