package com.example.textencryption;

import android.app.Application;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.init();
    }
}