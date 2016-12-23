package com.example.diogo.discoverytrip.GPS;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.diogo.discoverytrip.R;

/**
 * Created by diogo on 23/12/16.
 */

public class Localizacao implements LocationListener {

    private double latitude,longitude;
    private String status_gps;

    public void Localizacao(){

    }


    @Override
    public void onLocationChanged(Location loc) {
        // Obtendo coordenadas do GPS
        Log.d("Logger", "Localizacao onLocationChanged");
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // GPS desativado
        Log.d("Logger", "Localizacao onProviderDisabled");
        status_gps = R.string.gps_deactivated+"";
    }

    @Override
    public void onProviderEnabled(String provider) {
        // GPS ativado
        Log.d("Logger", "Localizacao onProviderEnabled");
       status_gps =  R.string.gps_activated+"";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Logger", "Localizacao onStatusChanged");
        // Método de monitoranmento dos serviços do de GPS
        // Status do provedor GPS:
        // OUT_OF_SERVICE -> Servidor fora de serviço
        // TEMPORARILY_UNAVAILABLE -> temporariamente indisponível aguardando serviço ser restabelecido
        // AVAILABLE -> Disponível
    }
}
