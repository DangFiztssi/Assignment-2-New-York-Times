package com.example.dangfiztssi.newyorktime.utils;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by dangfiztssi on 06/12/2016.
 */

public class myApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
