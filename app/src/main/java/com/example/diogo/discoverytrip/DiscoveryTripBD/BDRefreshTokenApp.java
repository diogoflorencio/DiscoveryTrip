package com.example.diogo.discoverytrip.DiscoveryTripBD;

import android.content.SharedPreferences;

/**
 * Created by diogo on 11/01/17.
 */

public class BDRefreshTokenApp {
    //armazena refreshToken loginApp
    public static void armezenaRefreshTokenApp(String refreshToken, SharedPreferences prefs) {
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("tokenApp", refreshToken);
        ed.commit();
    }

    // recupera refreshToken loginApp
    public static String recuperaRefreshTokenApp(SharedPreferences prefs) {
        String tokenApp = prefs.getString("tokenApp", "");
        return tokenApp;
    }
}