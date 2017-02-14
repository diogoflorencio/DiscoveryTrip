package com.example.diogo.discoverytrip.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Model.Localizacao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe fragment responsavel pelo fragmento inicial (home) na aplicação
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'HomeFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "HomeFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.fragment_home_list);

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<SearchResponse> call = ApiClient.API_SERVICE.searchPontoTuristico("bearer "+token,LocalizacaoFragment.latitude, LocalizacaoFragment.longitude,2000);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","pesquisa de pontos turisticos realizada com sucesso");
                    ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getContext(),
                            getActivity().getLayoutInflater(),
                            response.body().getPontosTuristicos());
                    listView.setAdapter(adapter);
                }
                else{
                    try {
                        Log.e("Pesquisa de pontos turisticos",response.errorBody().string());
                        //ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        //Log.e("Pesquisa de pontos turisticos", error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e("Pesquisa de ponto turisticos",t.toString());
            }
        });


        return rootView;
    }

}
