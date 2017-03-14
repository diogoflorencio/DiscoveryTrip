package com.example.diogo.discoverytrip.Fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Classe fragment responsavel pelo fragmento inicial (home) na aplicação
 */
public class HomeFragment extends Fragment implements LocationListener {
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 2;
    private double latitude, longitude;
    private boolean requestCompleted = false;
    private List<Atracao> atracoes;
    private ListView listView;
    private boolean get = true;

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
        startGPS();
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        atracoes = new ArrayList<>();

        getActivity().setTitle(R.string.home_label);

        listView = (ListView) rootView.findViewById(R.id.fragment_home_list);

        return rootView;
    }

    private void startGPS() {
        Log.d("Logger", "LocalizacaoFragment startGPS");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else
        if(verificaConexao())
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if(get){
            get = false;
            String token = AcessToken.recuperar(getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
            Call<SearchResponse> call = ApiClient.API_SERVICE.searchPontoTuristico("bearer "+token,latitude, longitude,2000);
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    if(response.isSuccessful()){
                        Log.d("Logger","pesquisa de pontos turisticos realizada com sucesso");

                        if(response.body().getAtracoes() != null){
                            atracoes.addAll(response.body().getAtracoes());
                        }

                        if(requestCompleted){
                            ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                                    getActivity().getLayoutInflater(),
                                    atracoes);
                            listView.setAdapter(adapter);
                        }
                        requestCompleted = true;
                    }
                    else{
                        get = true;
                        if(requestCompleted){
                            ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                                    getActivity().getLayoutInflater(),
                                    atracoes);
                            listView.setAdapter(adapter);
                        }
                        requestCompleted = true;
                        try {
                            Log.e("Logger",response.errorBody().string());
                            //ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                            //Log.e("Pesquisa de pontos turisticos", error.getErrorDescription());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    get = true;
                    if(requestCompleted){
                        ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                                getActivity().getLayoutInflater(),
                                atracoes);
                        listView.setAdapter(adapter);
                    }
                    requestCompleted = true;
                    Log.e("Logger","Pesquisa de pontos turisticos error: "+t.toString());
                    Toast.makeText(getActivity(),"Erro ao se conectar com o servidor!",Toast.LENGTH_SHORT).show();
                }
            });

            Call<SearchResponse> callEvents = ApiClient.API_SERVICE.searchEventos("bearer "+token,latitude, longitude,2000);
            callEvents.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    if(response.isSuccessful()){
                        Log.d("Logger","pesquisa de eventos realizada com sucesso");
                        if(response.body().getAtracoes() != null){
                            atracoes.addAll(response.body().getAtracoes());
                        }

                        if(requestCompleted){
                            ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                                    getActivity().getLayoutInflater(),
                                    atracoes);
                            listView.setAdapter(adapter);
                        }
                        requestCompleted = true;
                    }
                    else{
                        get = true;
                        if(requestCompleted){
                            ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                                    getActivity().getLayoutInflater(),
                                    atracoes);
                            listView.setAdapter(adapter);
                        }
                        requestCompleted = true;
                        try {
                            Log.e("Logger", "pesquisa de eventos"+response.errorBody().string());
                            //ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                            //Log.e("Pesquisa de pontos turisticos", error.getErrorDescription());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    get = true;
                    if(requestCompleted){
                        ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                                getActivity().getLayoutInflater(),
                                atracoes);
                        listView.setAdapter(adapter);
                    }
                    requestCompleted = true;
                    Log.e("Logger","pesquisa de eventos error: "+t.toString());
                    Toast.makeText(getActivity(),"Erro ao se conectar com o servidor!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean verificaConexao() {
        //verificando se o dispositivo tem conexão com internet
        ConnectivityManager conectivtyManager =
                (ConnectivityManager) this.getActivity().getSystemService(this.getActivity().CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "LocalizacaoFragment onDestroy");
        super.onDestroy();
        get = true;
        stopGPS();
    }

    private void stopGPS(){
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else locationManager.removeUpdates(this);
    }
}
