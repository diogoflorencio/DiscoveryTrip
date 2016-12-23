package com.example.diogo.discoverytrip.GPS;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.diogo.discoverytrip.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by diogo on 23/12/16.
 */

public class Localizacao implements LocationListener {

    private double latitude ,longitude;
    private String status_gps;
    private gpsUpdateInterface activity;

    public Localizacao(gpsUpdateInterface activity){
        this.activity = activity;
    }

    /*public void getEndereco(Location loc) {
        //Rastreando endereço a partir das coordenadas
        Log.d("Logger", "Home getEndereco");
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                  //  localizacao.setText(R.string.address_found + "\n"
                        //    + DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void onLocationChanged(Location loc) {
        // Obtendo coordenadas do GPS
        Log.d("Logger", "Localizacao onLocationChanged");
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        activity.updateLocation(loc.getLatitude(), loc.getLongitude());

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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
