package com.example.diogo.discoverytrip.Model;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.diogo.discoverytrip.BD.BDRefreshTokenApp;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ApiInterface;
import com.example.diogo.discoverytrip.REST.ServerResponses.LoginResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diogo on 04/02/17.
 */

public class RefreshTokenManeger {
    private static final int timeSleep = 3300000; //55 min
    private static boolean loggedIn = true;

    public static void refreshToken(final SharedPreferences prefs){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (loggedIn) {
                    try {
                        Thread.sleep(timeSleep);
                        refresh(prefs);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    private static void refresh(final SharedPreferences prefs){
        Log.d("Logger", "RefreshTokenManeger postFacebook");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call =
                apiService.refreshToken(new RefreshTokenJson(BDRefreshTokenApp.recuperaRefreshTokenApp(prefs),"clientID","clientSecret"));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    BDRefreshTokenApp.armezenaRefreshTokenApp(response.body().getRefreshtoken(), prefs);
                    Log.d("Logger","Server OK");
                }
                else{

                    try {
                        Log.e("Logger",(ApiClient.errorBodyConverter.convert(response.errorBody()).getErrorDescription()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.e("Logger",""+response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Log.e("Logger", t.toString());
            }
        });
    }

    public static void logout(){
        loggedIn = false;
    }
}
