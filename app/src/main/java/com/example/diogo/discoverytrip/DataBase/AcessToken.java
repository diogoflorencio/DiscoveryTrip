package com.example.diogo.discoverytrip.DataBase;

import android.content.SharedPreferences;

/**
 * Created by diogo on 05/02/17.
 */

public class AcessToken {
    //salva acessToken
    public static void salvar(String acessToken, SharedPreferences prefs) {
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("acessToken", acessToken);
        ed.commit();
    }

    // recupera acessToken
    public static String recuperar(SharedPreferences prefs) {
        String tokenApp = prefs.getString("acessToken", "");
        return tokenApp;
    }
}
