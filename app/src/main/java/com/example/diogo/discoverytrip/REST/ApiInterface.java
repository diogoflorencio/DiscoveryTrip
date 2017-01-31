package com.example.diogo.discoverytrip.REST;

import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.AppLoginJson;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseAbst;
import com.example.diogo.discoverytrip.Model.UsuarioEnvio;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Renato on 11/12/2016.
 */

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("api/users")
    Call<ResponseAbst> cadastrarUsuario(@Body UsuarioEnvio usuarioEnvio);

    @Headers("Content-Type: application/json")
    @GET("api/users/{id}")
    Call<ResponseAbst> getUsuario(@Path("id")String id);

    @Headers("Content-Type: application/json")
    @POST("api/facebook/login")
    Call<ResponseAbst> loginFacebook(@Body AccessTokenJson accessToken);

    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<ResponseAbst> appLogin(@Body AppLoginJson appLoginJson);

    @Headers("Content-Type: application/json")
    @DELETE("api/login")
    Call<ResponseAbst> logout(@Header("Authorization") String authorization);
}
