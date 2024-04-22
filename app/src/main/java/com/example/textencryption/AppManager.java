package com.example.textencryption;

public class AppManager {
    private static AppManager appManager = null;
    private User loggedIn;



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



}
