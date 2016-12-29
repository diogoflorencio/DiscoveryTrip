package com.example.diogo.discoverytrip;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.diogo.discoverytrip.GPS.GPS;
import com.example.diogo.discoverytrip.GPS.GPSUpdateInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalizacaoFragment extends Fragment implements GPSUpdateInterface {
    private TextView coodenadas, endereco;

    public LocalizacaoFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PontoTuristicoFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_localizacao, container, false);
        coodenadas = (TextView) rootView.findViewById(R.id.coordenadas);
        endereco = (TextView) rootView.findViewById(R.id.endereco);
        GPS.addClient(this);
        return rootView;
    }

    @Override
    public void updateLocation(double latitude, double longitude, String endereco) {
        coodenadas.setText("Latitude: "+latitude+"\n"+"Longitude: "+longitude);
        this.endereco.setText(endereco);
    }

    @Override
    public void onProviderDisabled() {
        coodenadas.setText("GPS Desativado");
        endereco.setText("");
    }

    @Override
    public void onProviderEnabled() {
        coodenadas.setText("GPS Ativado");
    }
}
