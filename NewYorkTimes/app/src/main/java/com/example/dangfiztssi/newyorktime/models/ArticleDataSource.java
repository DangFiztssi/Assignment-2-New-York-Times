package com.example.dangfiztssi.newyorktime.models;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dangfiztssi on 06/12/2016.
 */

public class ArticleDataSource {

    public void store(List<Article> articles){
        Realm realm = Realm.getDefaultInstance();

        //Transaction can go back when fail. like transfer money
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(ArticleEntity.class)
                        .findAll()
                        .deleteAllFromRealm();

                for (Article article: articles) {
                    ArticleEntity entity = realm.createObject(ArticleEntity.class);
                    entity.map(article);
                }

            }
        });

        realm.close();
    }

    public List<Article> getAll(){
        Realm realm = Realm.getDefaultInstance();
        List<Article> articles = new ArrayList<>();

        RealmResults<ArticleEntity> results = realm.where(ArticleEntity.class)
                .findAll();

        for (ArticleEntity entity: results){
            articles.add(new Article(entity));
        }

        realm.close();
        return articles;
    }
}
