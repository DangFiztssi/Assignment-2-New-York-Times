package com.example.dangfiztssi.newyorktime.presenter;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.dangfiztssi.newyorktime.R;
import com.example.dangfiztssi.newyorktime.activity.MainActivity;
import com.example.dangfiztssi.newyorktime.activity.ViewDetailArticleActivity;
import com.example.dangfiztssi.newyorktime.adapter.ItemArticleAdapter;
import com.example.dangfiztssi.newyorktime.api.SearchApi;
import com.example.dangfiztssi.newyorktime.models.Article;
import com.example.dangfiztssi.newyorktime.models.ArticleDataSource;
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

    ArticleDataSource dataSource;

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

            @Override
            public void onClick(Article article) {
                Intent i = new Intent(activity, ViewDetailArticleActivity.class);
                i.putExtra("data",article.getUrl());
                activity.startActivity(i);
            }
        });

        dataSource = new ArticleDataSource();

    }

    public ItemArticleAdapter getAdapter(){
        return this.adapter;
    }

    public boolean isConnected(){
        return AppUtils.checkNetwork(activity);
    }

    public void search(){
        new AsynctaskConnection().execute();

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

        new AsynctaskConnection().execute();

        request.nextPage();
        activity.loadMore.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResponse(ArticleResult result) {
                adapter.addArticles(result.getArticleList());

                dataSource.store(result.getArticleList());
                activity.showSnackBar("Saved data");

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

    public class AsynctaskConnection extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return AppUtils.checkNetwork(activity.getApplicationContext());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result){
                activity.showSnackBar(activity.getString(R.string.message_error_network));

                Log.e("restore data", "onPostExecute: ...................." );
                activity.showSnackBar("restoring data....");
                List<Article> articles = dataSource.getAll();
                adapter.setArticles(articles);

            }
        }
    }


}
