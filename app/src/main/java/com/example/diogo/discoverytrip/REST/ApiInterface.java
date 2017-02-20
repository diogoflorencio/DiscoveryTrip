package com.example.diogo.discoverytrip.REST;

import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.AppLoginJson;
import com.example.diogo.discoverytrip.Model.RefreshTokenJson;
import com.example.diogo.discoverytrip.Model.ReminderJson;
import com.example.diogo.discoverytrip.REST.ServerResponses.AddEventoResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.AttractionResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.LoginResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.LogoutResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ReminderResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseAbst;
import com.example.diogo.discoverytrip.Model.UsuarioEnvio;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Renato on 11/12/2016.
 * Interface da API Rest usada para fazer as requisições ao servidor da aplicação.
 */
public interface ApiInterface {

    @POST("api/users")
    Call<ServerResponse> cadastrarUsuario(@Body UsuarioEnvio usuarioEnvio);

    @GET("api/users/")
    Call<ServerResponse> getUsuario(@Header("Authorization") String accessToken);

    @PUT("api/users/")
    Call<ServerResponse> setUsuario(@Header("Authorization")String accessToken, @Body UsuarioEnvio usuarioEnvio);

    @POST("api/facebook/login")
    Call<LoginResponse> loginFacebook(@Body AccessTokenJson accessToken);

    @POST("api/login")
    Call<LoginResponse> appLogin(@Body AppLoginJson appLoginJson);

    @POST("api/login")
    Call<LoginResponse> refreshToken(@Body RefreshTokenJson refreshJson);

    @POST("api/login/pwd_reminder")
    Call<ReminderResponse> passwordReminder(@Body ReminderJson reminderJson);

    @DELETE("api/login")
    Call<LogoutResponse> logout(@Header("Authorization") String authorization);

    /**
     * Cadastra um ponto turístico no servidor
     * @param token access token
     * @param parametersMap mapa que contém os nomes dos parâmetros da chamada como chave e seus respectivos valores. Os parâmetros possíveis nessa chamada são:
     *                      Required:
     *                      name = [string]
     *                      description = [string]
     *                      latitude = [string] <- In ISO 6709 format
     *                      longitude = [string] <- In ISO 6709 format
     *                      photos = [blob] <- At least one photo and a maximum of 10 photos
     * @param fotos parte da request que contém as fotos do ponto turistico
     * @return objeto contendo a resposta do servidor. Caso seja uma resposta de erro deve-se usar o errorBodyConverter da classe ApiClient.
     */
    @Multipart
    @POST("api/attractions")
    Call<AttractionResponse> cadastrarPontoTuristico(@Header("Authorization") String token, @PartMap Map<String, RequestBody> parametersMap, @Part MultipartBody.Part fotos);

    /**
     * Cadastra um evento no servidor
     * @param token access token
     * @param parametersMap mapa que contém os nomes dos parâmetros da chamada como chave e seus respectivos valores. Os parâmetros possíveis nessa chamada são:
     *                      Required:
     *                      name = [string]
     *                      description = [string]
     *                      endData = [string] <- In ISO Date format
     *
     *                      Optional:
     *                      photo = [file]
     *                      kind = [String] <- Public|Private
     *                      price = [Number]
     *                      keywords = [Array of Strings]
     *                      startDate = [String] <- In ISO Date format
     * @param foto
     * @return objeto contendo a resposta do servidor. Caso seja uma resposta de erro deve-se usar o errorBodyConverter da classe ApiClient.
     */
    @Multipart
    @POST("api/events")
    Call<AddEventoResponse> cadastrarEvento(@Header("Authorization") String token, @PartMap Map<String, RequestBody> parametersMap, @Part MultipartBody.Part foto);

    @Multipart
    @POST("api/events")
    Call<AddEventoResponse> cadastrarEvento(@Header("Authorization") String token, @PartMap Map<String, RequestBody> parametersMap);

    @GET("api/search/attractions")
    Call<SearchResponse> searchPontoTuristico(@Header("Authorization") String token, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") int distance);
}