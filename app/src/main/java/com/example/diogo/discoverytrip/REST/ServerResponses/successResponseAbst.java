package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 16/12/2016.
 */

public abstract class successResponseAbst extends ResponseAbst{

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
