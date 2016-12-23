package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.type;


public class AppLoginJson {
    private static final String id = "discoveryTrip", clientsecret = "0ca9b9b3-1370-44e0-b42e-01cf6f6fc04c";

    @SerializedName("grant_type")
    private String grant_type;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("client_id")
    private String client_id;

    @SerializedName("client_secret")
    private String client_secret;

    public String getIdclient() {
        return client_id;
    }

    public void setIdclient(String idclient) {
        this.client_id = idclient;
    }

    public String getSecret() {
        return client_secret;
    }

    public void setSecret(String secret) {
        this.client_secret = secret;
    }

    public AppLoginJson(String username, String password){
        this.username = username;
        this.password = password;
        grant_type = "password";
        this.client_id = id;
        this.client_secret = clientsecret;
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
