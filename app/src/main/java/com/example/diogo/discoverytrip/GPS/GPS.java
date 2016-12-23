package com.example.diogo.discoverytrip.GPS;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by diogo on 23/12/16.
 */

public class GPS {

    private static final int REQUEST_LOCATION = 2; //permissoes GPS

    private LocationManager mlocManager; //provedor GPS

    private Localizacao local; // Classe auxiliar para localização

    private Activity activity; // Contexto da permissão

    public GPS(Activity homeActivity, LocationManager locationManager) {
        mlocManager = locationManager;
        activity = homeActivity;
        local = new Localizacao(); //instanciando classe aux para localizacao
        pedirPermissão();
    }

    private void pedirPermissão(){
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION));
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else {
            //requisição de coordenadas ao provedor GPS
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    (LocationListener) local);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Logger", "Home onRequestPermissionsResult");
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED);
                //Requisitando localização do provedor de GPS
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) local);
            } else; //Usuário rejeitou permissões
        }
    }

    public double getLatitude(){
        Log.d("Logger", "Localizacao AQUI" + local.getLatitude());
        return local.getLatitude();
    }

    public double getLongitude(){
        return local.getLongitude();
    }

}
