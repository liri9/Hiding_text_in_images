package com.example.textencryption;

public class Image {
    private String imageUrl;
    private String userName;

    // Constructor, getters, and setters
    public Image(String imageUrl, String userName) {
        this.imageUrl = imageUrl;
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserName() {
        return userName;
    }
}
