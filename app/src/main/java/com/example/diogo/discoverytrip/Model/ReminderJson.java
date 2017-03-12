package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 31/01/17.
 */
public class ReminderJson {

    @SerializedName("email")
    private String email;

    public ReminderJson(String email){
        this.email = email;
    }
}
