package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;


public class AccessTokenJson {

    @SerializedName("access_token")
    private String token;

    public AccessTokenJson(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
