package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 31/01/17.
 */
public class RefreshTokenJson {

    @SerializedName("grant_type")
    private String grant_type;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;

    public RefreshTokenJson(String refreshToken){
        this.grant_type = "refresh_token";
        this.refreshToken = refreshToken;
        this.clientId = "discoveryTrip";
        this.clientSecret = "0ca9b9b3-1370-44e0-b42e-01cf6f6fc04c";
    }
}
