package com.example.diogo.discoverytrip.Activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.diogo.discoverytrip.Fragments.EventoCadastroFragment;
import com.example.diogo.discoverytrip.Fragments.PontoTuristicoCadastroFragment;
import com.example.diogo.discoverytrip.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, OnMapLongClickListener {

    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 17;
    private final LatLng defaultLocation = new LatLng(-7.212023, -35.9086433); //Ebedded
    private static final int REQUEST_MAP = 2;

//    private GoogleMap.OnMapLongClickListener OnMapLongClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "MapsActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        findViewById(R.id.mapCreateEv_btn).setOnClickListener(this);
        findViewById(R.id.mapCreatePnt_btn).setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Log.d("Logger", "MapsActivity onMapLongClick");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Logger", "MapsActivity onMapReady");
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        setUpMap();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        startingZoom();
    }

    private void setUpMap() {
        Log.d("Logger", "MapsActivity setUpMap");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_MAP);
        } else
             mMap.setMyLocationEnabled(true);
    }

    private void startingZoom(){
        Log.d("Logger", "MapsActivity startingZoom");
        mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
    }

    private void hideInterface(){
        Log.d("Logger", "MapsActivity hideInterface");
        findViewById(R.id.mapCreateEv_btn).setVisibility(View.INVISIBLE);
        findViewById(R.id.mapCreatePnt_btn).setVisibility(View.INVISIBLE);
    }

    public void showInterface(){
        Log.d("Logger", "MapsActivity showInterface");
        findViewById(R.id.mapCreateEv_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.mapCreatePnt_btn).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        Log.d("Logger", "MapsActivity onResume");
        super.onResume();
    }

    private void goToEventCreation(){
        Log.d("Logger", "MapsActivity goToEventCreation");
        LatLng latLng =  mMap.getCameraPosition().target;
        EventoCadastroFragment fragment = new EventoCadastroFragment();
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        Bundle extras = new Bundle();

        extras.putDouble("Lat", latLng.latitude);
        extras.putDouble("Lng", latLng.longitude);
        if(extras !=null) {
            fragment.setArguments(extras);
        }

        fragmentManager.replace(R.id.content_map, fragment);
        fragmentManager.commit();
        hideInterface();
    }

    private void goToAttractionCreation(){
        Log.d("Logger", "MapsActivity goToAttractionCreation");
        LatLng latLng =  mMap.getCameraPosition().target;
        PontoTuristicoCadastroFragment fragment = new PontoTuristicoCadastroFragment();
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        Bundle extras = new Bundle();

        extras.putDouble("Lat", latLng.latitude);
        extras.putDouble("Lng", latLng.longitude);
        if(extras !=null) {
            fragment.setArguments(extras);
        }
        fragmentManager.replace(R.id.content_map, fragment);
        fragmentManager.commit();
        hideInterface();
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "MapsActivity onClick");
        switch (view.getId()) {
            case R.id.mapCreateEv_btn:
                Log.d("Logger", "MapsActivity botao criar evento");
                goToEventCreation();
                break;
            case R.id.mapCreatePnt_btn:
                Log.d("Logger", "MapsActivity botao criar ponto turistico");
                goToAttractionCreation();
                break;
        }
    }
}

