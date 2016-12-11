package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 11/12/2016.
 */

public class AcessTokenJson {

    @SerializedName("access_token")
    private String token;

    public AcesseTokenJson(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
