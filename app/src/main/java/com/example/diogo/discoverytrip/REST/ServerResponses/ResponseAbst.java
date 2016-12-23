package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 18/12/2016.
 */

public abstract class ResponseAbst {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
