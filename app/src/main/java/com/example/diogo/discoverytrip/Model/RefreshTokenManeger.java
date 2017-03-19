package com.example.diogo.discoverytrip.Model;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.diogo.discoverytrip.DataBase.RefreshToken;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.LoginResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diogo on 04/02/17.
 */

public class RefreshTokenManeger {
    private static final int timeSleep = 2400000; //40 min
    private static boolean loggedIn = true , running = false;
    private static Thread thread;

    public static void refreshToken(final SharedPreferences prefs){
        Log.d("Logger", "refreshToken");
        if(!running) return;
        running = true;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (loggedIn) {
                    try {
                        Log.d("Logger", "refreshToken start");
                        Thread.sleep(timeSleep);
                        refresh(prefs);
                    } catch (Exception e){
                        e.printStackTrace();
                        Log.d("Logger", "refreshToken break");
                        return;
                    }
                }
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    private static void refresh(final SharedPreferences prefs){
        Log.d("Logger", "RefreshTokenManeger postFacebook");
        Call<LoginResponse> call =
                ApiClient.API_SERVICE.refreshToken(new RefreshTokenJson(RefreshToken.recuperar(prefs)));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    RefreshToken.salvar(response.body().getRefreshtoken(), prefs);
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

    public static void stop(){
        loggedIn = false;
        thread.interrupt();
    }

    public static boolean isRunning(){
        return running;
    }
}
