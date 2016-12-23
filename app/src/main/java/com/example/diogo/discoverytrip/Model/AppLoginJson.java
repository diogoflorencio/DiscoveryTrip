package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.type;


public class AppLoginJson {
    private static final String GRANT_TYPE = "client_credentials";

    @SerializedName("grant_type")
    private String grant_type;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public AppLoginJson(String username, String password){
        this.username = username;
        this.password = password;
        grant_type = GRANT_TYPE;
    }

    public String getType() {
        return grant_type;
    }

    public void setType(String type) {
        this.grant_type = type;
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
