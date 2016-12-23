package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 12/12/2016.
 */

public class AppLoginResponse extends successResponseAbst{

    @SerializedName("access_token")
    private String accesstoken;

    @SerializedName("refresh_token")
    private String refreshtoken;

    @SerializedName("expires_in")
    private int expires;

    @SerializedName("token_type")
    private String tokentype;

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public String getTokentype() {
        return tokentype;
    }

    public void setTokentype(String tokentype) {
        this.tokentype = tokentype;
    }
}
