package com.example.diogo.discoverytrip.REST;

import com.example.diogo.discoverytrip.Model.ServerResponse;
import com.example.diogo.discoverytrip.Model.User;
import com.example.diogo.discoverytrip.Model.UsuarioEnvio;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Renato on 11/12/2016.
 */

public interface ApiInterface {

    @POST("api/users")
    Call<ServerResponse> cadastrarUsuario(@Body UsuarioEnvio usuarioEnvio);

    @GET("api/users/{id}")
    Call<ServerResponse> getUsuario(@Path("id")String id);
}
