package com.example.diogo.discoverytrip.REST;

import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.AppLoginJson;
import com.example.diogo.discoverytrip.REST.ServerResponses.AppLoginResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.LogoutResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponseLogin;
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

    @POST("api/users")
    Call<ServerResponse> cadastrarUsuario(@Body UsuarioEnvio usuarioEnvio);

    @GET("api/users/{id}")
    Call<ServerResponse> getUsuario(@Path("id")String id);

    @POST("api/facebook/login")
    Call<ServerResponseLogin> loginFacebook(@Body AccessTokenJson accessToken);

    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<AppLoginResponse> appLogin(@Body AppLoginJson appLoginJson);

    @DELETE("api/login")
    Call<LogoutResponse> logout(@Header("Authorization") String authorization);
}
