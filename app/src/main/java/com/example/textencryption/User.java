package com.example.textencryption;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String userName;
    private String id;
    private String phone;
    public String getPhone() {
        return phone;
    }


    public User() {
    }

    public HashMap<String, Object> userAsHashmap() {
        HashMap<String, Object> userAsHashmap = new HashMap<String, Object>();
        userAsHashmap.put("userName", userName);
        userAsHashmap.put("id", id);
        return userAsHashmap;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }
    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
