package com.example.diogo.discoverytrip.Activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.diogo.discoverytrip.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, OnMapLongClickListener {

    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 17;
    private final LatLng defaultLocation = new LatLng(-7.212023, -35.9086433); //Embedded
    private static final int REQUEST_MAP = 2;
    private NumberFormat formatter = new DecimalFormat("#0.000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "MapsActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        findViewById(R.id.mapSelect_btn).setOnClickListener(this);

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

    @Override
    protected void onResume() {
        Log.d("Logger", "MapsActivity onResume");
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "MapsActivity onClick");
        switch (view.getId()) {
            case R.id.mapSelect_btn:
                Log.d("Logger", "MapsActivity botao selecionar");
                getLatLng();
                break;
        }
    }

    public void getLatLng() {
        Log.d("Logger", "MapsActivity getLatLng");
        LatLng latLng =  mMap.getCameraPosition().target;
        getIntent().putExtra("Lat", formatter.format(latLng.latitude));
        getIntent().putExtra("Lng", formatter.format(latLng.longitude));
        setResult(RESULT_OK, getIntent());
        finish();
    }
}

