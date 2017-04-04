package com.example.diogo.discoverytrip.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.DeleteAttractionResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PontoTuristicoFragment extends Fragment implements View.OnClickListener {

    private List<Atracao> meusPontosTuristicos;
    private ListView listViewMeusPontosTuristicos;

    public PontoTuristicoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PontoTuristicoFragment onCreate");

        View rootView = inflater.inflate(R.layout.fragment_ponto_turistico, container, false);

        getActivity().setTitle(R.string.ponto_turistico_label);

        rootView.findViewById(R.id.createPntTuristico_btn).setOnClickListener(this);

        listViewMeusPontosTuristicos = (ListView) rootView.findViewById(R.id.meus_ponto_turistico_list);

        getUserPoints();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Log.d("Logger", "PontoTuristicoFragment onClick");
        switch (v.getId()) {
            case R.id.createPntTuristico_btn:
                Log.d("Logger", "EventoFragment botao confirmar");
                goToPntTuristicoCreation();
                break;

        }
    }

    private void goToPntTuristicoCreation() {
        Log.d("Logger", "PontoTuristicoFragment goToPntTuristicoCreation");
        FragmentManager fragmentManager = getFragmentManager();
        PontoTuristicoCadastroFragment fragment = new PontoTuristicoCadastroFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void getUserPoints(){
        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<SearchResponse> call = ApiClient.API_SERVICE.userPoints("bearer "+token);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","UserPoints ok");
                    List<Atracao> userPoints = response.body().getAtracoes();
                    userPoints = filtroPontoTuristico(userPoints);
                    listViewMeusPontosTuristicos.setAdapter(new ListAdapterPontosTuristicos(getActivity(),getActivity().getLayoutInflater(),userPoints));
                }else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "UserPoints ServerResponse "+error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e("Logger","UserPoints error: "+t.toString());
            }
        });
    }

    private void deletePoints(String id){
        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<DeleteAttractionResponse> call = ApiClient.API_SERVICE.deleteAttraction("bearer "+token,id);
        call.enqueue(new Callback<DeleteAttractionResponse>() {
            @Override
            public void onResponse(Call<DeleteAttractionResponse> call, Response<DeleteAttractionResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("Logger", "deletePoints ok");
                }else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "deletePoints ServerResponse "+error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteAttractionResponse> call, Throwable t) {
                Log.e("Logger","deletePoints error: "+t.toString());
            }
        });
    }

    private List<Atracao> filtroPontoTuristico(List<Atracao> atracoes){
        List<Atracao> pontosTuristicos = new ArrayList<>();
        for (Atracao atracao: atracoes) {
            if(atracao.getType().equals(HomeActivity.POINT_TYPE)){
                pontosTuristicos.add(atracao);
            }
        }
        return pontosTuristicos;
    }
}
