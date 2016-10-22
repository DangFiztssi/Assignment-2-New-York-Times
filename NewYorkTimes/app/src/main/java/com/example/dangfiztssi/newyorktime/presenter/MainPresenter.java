package com.example.dangfiztssi.newyorktime.presenter;

import android.util.Log;
import android.view.View;

import com.example.dangfiztssi.newyorktime.activity.MainActivity;
import com.example.dangfiztssi.newyorktime.adapter.ItemArticleAdapter;
import com.example.dangfiztssi.newyorktime.api.SearchApi;
import com.example.dangfiztssi.newyorktime.models.Article;
import com.example.dangfiztssi.newyorktime.models.ArticleResult;
import com.example.dangfiztssi.newyorktime.models.SearchRequest;
import com.example.dangfiztssi.newyorktime.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DangF on 10/21/16.
 */

public class MainPresenter {
    List<Article> articleList;
    ItemArticleAdapter adapter;
    MainActivity activity;

    SearchApi api;
    public SearchRequest request = new SearchRequest();

    private interface Listener{
        void onResponse(ArticleResult result);
    }

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
        api = AppUtils.getRetrofit().create(SearchApi.class);
        adapter = new ItemArticleAdapter();
        adapter.setListener(new ItemArticleAdapter.Listener() {
            @Override
            public void onLoadMore() {
                searchMore();
            }
        });
    }

    public ItemArticleAdapter getAdapter(){
        return this.adapter;
    }

    public void search(){
        request.resetPage();

        fetchArticles(new Listener() {
            @Override
            public void onResponse(ArticleResult result) {
                adapter.setArticles(result.getArticleList());
                if(activity.waiting.isShowing())
                    activity.waiting.dismiss();

                activity.refreshMain.setRefreshing(false);
                activity.rvMain.scrollToPosition(0);
            }
        });
    }

    public void searchMore(){
        request.nextPage();
        activity.loadMore.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResponse(ArticleResult result) {
                adapter.addArticles(result.getArticleList());

                activity.loadMore.setVisibility(View.GONE);
            }
        });
    }

    private void fetchArticles(Listener listener){
        api.search(request.toQueryMay()).enqueue(new Callback<ArticleResult>() {
            @Override
            public void onResponse(Call<ArticleResult> call, Response<ArticleResult> response) {
                if(response.body() != null)
                    listener.onResponse(response.body());
                else{
                    activity.loadMore.setVisibility(View.GONE);
                    if(activity.waiting.isShowing())
                        activity.waiting.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArticleResult> call, Throwable t) {
                Log.e("failed", t.getMessage() + "");

            }
        });
    }

}
