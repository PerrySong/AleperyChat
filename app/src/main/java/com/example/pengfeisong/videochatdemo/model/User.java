package com.example.pengfeisong.videochatdemo.model;

/**
 * Created by pengfeisong on 3/25/18.
 */


import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengfeisong on 3/25/18.
 */

public class User {
    public String username;
    public String email;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("password", password);
        result.put("email", email);
        result.put("username", username);
        return result;
    }

}
