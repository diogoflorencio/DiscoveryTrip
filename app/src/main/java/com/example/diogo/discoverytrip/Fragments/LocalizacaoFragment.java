package com.example.diogo.discoverytrip.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.TextView;

import com.example.diogo.discoverytrip.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Classe fragment responsavel pelo fragmento localização na aplicação
 */
public class LocalizacaoFragment extends Fragment implements LocationListener {
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 2;
    private TextView coordenadas, endereco;

    public static double latitude, longitude;

    public LocalizacaoFragment() {
        // Required empty public constructor
    }
    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'LocalizacaoFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "LocalizacaoFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_localizacao, container, false);

        getActivity().setTitle(R.string.localizacao_label);

        coordenadas = (TextView) rootView.findViewById(R.id.coordenadas);
        endereco = (TextView) rootView.findViewById(R.id.local_endereco);
        startGPS();
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
        coordenadas.setText("carregando...");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Logger", "LocalizacaoFragment onLocationChanged");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        coordenadas.setText("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
        endereco.setText(getEndereco(location));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Logger", "LocalizacaoFragment onStatusChanged " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Logger", "LocalizacaoFragment onProviderEnabled");
        coordenadas.setText("carregando...");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Logger", "LocalizacaoFragment onProviderDisabled");
        coordenadas.setText("GPS desligado");
    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "LocalizacaoFragment onDestroy");
        super.onDestroy();
        stopGPS();
    }

    private String getEndereco(Location location) {
        if (!verificaConexao()) return "Endereço indisponível sem conexão";
        //Rastreando endereço a partir das coordenadas
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            try {
                Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    return DirCalle.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Endereço não encontrado";
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
