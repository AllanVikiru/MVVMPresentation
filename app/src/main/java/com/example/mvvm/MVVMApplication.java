package com.example.mvvm;

import android.app.Application;

import com.facebook.stetho.Stetho;

/*
* Main starting point of your application.
* */
public class MVVMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

}
