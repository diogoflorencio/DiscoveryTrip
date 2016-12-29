package com.example.diogo.discoverytrip.GPS;

/**
 * Created by renato on 23/12/16.
 */
public interface GPSUpdateInterface {

    public void updateLocation(double latitude, double longitude, String endereco);
    public void onProviderDisabled();
    public void onProviderEnabled();
}
