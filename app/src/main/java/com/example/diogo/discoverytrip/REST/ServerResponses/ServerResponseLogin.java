package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 11/12/2016.
 */

public class ServerResponseLogin {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
