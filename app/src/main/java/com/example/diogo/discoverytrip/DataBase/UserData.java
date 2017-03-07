package com.example.diogo.discoverytrip.DataBase;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diogo on 06/03/17.
 */

public class UserData {

    public static void salvar(String id, String name, String email, SharedPreferences prefs) {
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("userData_id", id);
        ed.putString("userData_name", name);
        ed.putString("userData_email", email);
        ed.commit();
    }

    public static Map<String,String> recuperar(SharedPreferences prefs) {
        Map<String,String> userData = new HashMap<String, String>();
        userData.put("id",prefs.getString("userData_id", ""));
        userData.put("name",prefs.getString("userData_name", ""));
        userData.put("email",prefs.getString("userData_email", ""));
        return userData;
    }
}
