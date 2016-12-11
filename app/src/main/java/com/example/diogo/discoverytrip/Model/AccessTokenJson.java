package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 11/12/2016.
 */

public class AccessTokenJson {

    @SerializedName("access_token")
    private String token;

    @SerializedName("refresh_token")
    private String refreshtoken;

    public AccessTokenJson(String token, String refreshtoken){
        this.token = token;
        this.refreshtoken = refreshtoken;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
