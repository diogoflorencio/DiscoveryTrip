package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 12/12/2016.
 */

public class AppLoginJson {

    @SerializedName("grant_type")
    private String type;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public AppLoginJson(String username, String password){
        this.username = username;
        this.password = password;
        type = "password";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
