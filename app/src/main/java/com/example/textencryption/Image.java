package com.example.textencryption;

import android.graphics.Bitmap;

public class Image {
    private Bitmap bitmap;
    private String userName;

    // Constructor, getters, and setters
    public Image(Bitmap bitmap, String userName) {
        this.bitmap = bitmap;
        this.userName = userName;
    }

    public Bitmap getImageUrl() {
        return bitmap;
    }

    public String getUserName() {
        return userName;
    }
}
