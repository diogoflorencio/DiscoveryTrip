package com.example.diogo.discoverytrip.REST;

import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.AppLoginJson;
import com.example.diogo.discoverytrip.Model.RefreshTokenJson;
import com.example.diogo.discoverytrip.Model.ReminderJson;
import com.example.diogo.discoverytrip.REST.ServerResponses.LoginResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.LogoutResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ReminderResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseAbst;
import com.example.diogo.discoverytrip.Model.UsuarioEnvio;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;

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
 * Interface da API Rest usada para fazer as requisições ao servidor da aplicação.
 */
public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("api/users")
    Call<ServerResponse> cadastrarUsuario(@Body UsuarioEnvio usuarioEnvio);

    @Headers("Content-Type: application/json")
    @GET("api/users/{id}")
    Call<ServerResponse> getUsuario(@Path("id")String id);

    @Headers("Content-Type: application/json")
    @POST("api/facebook/login")
    Call<LoginResponse> loginFacebook(@Body AccessTokenJson accessToken);

    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<LoginResponse> appLogin(@Body AppLoginJson appLoginJson);

    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<LoginResponse> refreshToken(@Body RefreshTokenJson refreshJson);

    @Headers("Content-Type: application/json")
    @POST("api/login/pwd_reminder")
    Call<ReminderResponse> passwordReminder(@Body ReminderJson reminderJson);

    @Headers("Content-Type: application/json")
    @DELETE("api/login")
    Call<LogoutResponse> logout(@Header("Authorization") String authorization);
}
