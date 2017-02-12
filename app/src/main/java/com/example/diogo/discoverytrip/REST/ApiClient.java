package com.example.diogo.discoverytrip.REST;

import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Renato on 11/12/2016.
 */

public class ApiClient {
    public static final String BASE_URL = "https://discovery-trip-api.herokuapp.com/";
    private static Retrofit retrofit = null;
    public static final ApiInterface API_SERVICE = ApiClient.getClient().create(ApiInterface.class);

    public static final Converter<ResponseBody, ErrorResponse> errorBodyConverter = getClient().responseBodyConverter(ErrorResponse.class, new Annotation[0]);

    private static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
