package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 07/02/17.
 */
public class Localizacao {

    @SerializedName("latitude")
    private long latitude;

    @SerializedName("longitude")
    private long longitude;

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }
}
