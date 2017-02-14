package com.example.diogo.discoverytrip.DataBase;

import android.content.SharedPreferences;

/**
 * Created by diogo on 11/01/17.
 */

public class RefreshToken {
    //salva refreshToken loginApp
    public static void salvar(String refreshToken, SharedPreferences prefs) {
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("refreshToken", refreshToken);
        ed.commit();
    }

    // recupera refreshToken loginApp
    public static String recuperar(SharedPreferences prefs) {
        String tokenApp = prefs.getString("refreshToken", "");
        return tokenApp;
    }
}