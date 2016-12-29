package com.example.diogo.discoverytrip.GPS;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.diogo.discoverytrip.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by diogo on 23/12/16.
 */

public class GPSLocation implements LocationListener {
    private String status;
    private List<GPSUpdateInterface> client;
    private Activity activity;

    public GPSLocation(Activity activity){
        this.activity = activity;
        client = new ArrayList<GPSUpdateInterface>();
    }

    @Override
    public void onLocationChanged(Location loc) {
        // Obtendo coordenadas do GPS
        Log.d("Logger", "GPSLocation onLocationChanged");
        responseOnLocationChanged(loc.getLatitude(), loc.getLongitude(), getEndereco(loc));
    }

    @Override
    public void onProviderDisabled(String provider) {
        // GPS desativado
        Log.d("Logger", "GPSLocation onProviderDisabled");
        status = "GPS Desativado";
    }

    @Override
    public void onProviderEnabled(String provider) {
        // GPS ativado
        Log.d("Logger", "GPSLocation onProviderEnabled");
        status = "GPS Ativado";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Logger", "GPSLocation onStatusChanged");
        // Método de monitoranmento dos serviços do de GPS
        // Status do provedor GPS:
        // OUT_OF_SERVICE -> Servidor fora de serviço
        // TEMPORARILY_UNAVAILABLE -> temporariamente indisponível aguardando serviço ser restabelecido
        // AVAILABLE -> Disponível
    }

    protected void addClient(GPSUpdateInterface gpsUpdateInterface){
        client.add(gpsUpdateInterface);
    }

    private String getEndereco(Location loc) {
        //Rastreando endereço
        Log.d("Logger", "Home getEndereco");
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder((Context) activity, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    return DirCalle.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void responseOnLocationChanged(double latitude,double longitude, String endereco){
        Log.d("Logger", "responseOnLocationChanged");
        if(client.isEmpty()) return;
        Iterator<GPSUpdateInterface> it = client.iterator();
        while (it.hasNext()) it.next().updateLocation(latitude,longitude,endereco);
    }

    protected String getStatus(){
        return status;
    }
}
