package com.example.textencryption;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class AppManager {
    private static AppManager appManager = null;
    private User loggedIn;
    private static ArrayList<Image> allImages = new ArrayList<>();


    private AppManager() {
    }

    public static void init() {
        if (appManager == null) appManager = new AppManager();
    }

    public static AppManager getInstance() {
        return appManager;
    }


    public User getLoggedIn() {

        return loggedIn;
    }

    public void setLoggedIn(User loggedIn) {
        this.loggedIn = loggedIn;
    }

    public ArrayList<Image> getAllImages() {
        return allImages;
    }

    public void addImage(Image image) {
        allImages.add(image);
    }

}
