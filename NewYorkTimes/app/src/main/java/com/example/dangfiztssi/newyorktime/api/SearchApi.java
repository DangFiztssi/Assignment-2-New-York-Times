package com.example.dangfiztssi.newyorktime.api;

import java.util.Map;

import com.example.dangfiztssi.newyorktime.models.ArticleResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by DangF on 10/20/16.
 */

public interface SearchApi {
    @GET("articlesearch.json")
    Call<ArticleResult> search(@QueryMap(encoded = true) Map<String, String> options);
}
