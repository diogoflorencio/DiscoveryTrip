package com.example.diogo.discoverytrip.Activities;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.DeleteEventoResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeusEventosActivity extends Activity {

    private List<Atracao> userEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos);
        Log.d("Logger","MeusEventosActivity onCreate");
        getUserEventos();
    }

    private void getUserEventos(){
        String token = AcessToken.recuperar(this.getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<SearchResponse> call = ApiClient.API_SERVICE.userPoints("bearer "+token);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","UserEventos ok");
                    userEventos = response.body().getAtracoes();
                }else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "UserEventos ServerResponse "+error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e("Logger","UserEventos error: "+ t.toString());
            }
        });
    }

    private void deleteEventos(String id){
        String token = AcessToken.recuperar(this.getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<DeleteEventoResponse> call = ApiClient.API_SERVICE.deleteEvento("bearer "+token,id);
        call.enqueue(new Callback<DeleteEventoResponse>() {
            @Override
            public void onResponse(Call<DeleteEventoResponse> call, Response<DeleteEventoResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","deleteEventos ok");
                }else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "deleteEventos ServerResponse "+error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteEventoResponse> call, Throwable t) {
                Log.e("Logger","deleteEventos error: "+ t.toString());
            }
        });
    }
}
