package com.example.diogo.discoverytrip;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.GPS.GPS;
import com.example.diogo.discoverytrip.GPS.GPSUpdateInterface;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.facebook.AccessToken.getCurrentAccessToken;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "Home onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        GPS gps = new GPS(this,(LocationManager) getSystemService(Context.LOCATION_SERVICE));
        buildGooglePlusConfigs();
        createHomeFragment();
    }

    public void buildGooglePlusConfigs() {
        Log.d("Logger", "Home buildGooglePlusConfigs");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onBackPressed() {
        Log.d("Logger", "Home onBackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Logger", "Home onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("Logger", "Home onOptionsItemSelected");
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Log.d("Logger", "Home action_settings");
                return true;
            case R.id.logout:
                Log.d("Logger", "Home logout");
                if (getCurrentAccessToken() != null) {
                    AccessToken.setCurrentAccessToken(null);
                } else signOutGooglePlus();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOutGooglePlus() {
        Log.d("Logger", "Home signOutGooglePlus");
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("Logger", "Home onNavigationItemSelected");
        int id = item.getItemId();
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_localizacao:
                Log.d("Logger", "Home localizacao");
                fragment = new LocalizacaoFragment();
                break;
            case R.id.nav_perfil:
                Log.d("Logger", "Home perfil");
                fragment = new PerfilFragment();
                break;
            case R.id.nav_ponto_turistico:
                Log.d("Logger", "Home ponto turistico");
                fragment = new PontoTuristicoFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle extras = getIntent().getExtras();

            if(extras !=null) {
                fragment.setArguments(extras);
            }
            fragmentManager.beginTransaction().replace(R.id.content_home, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createHomeFragment() {
        Log.d("Logger", "Home createHomeFragment");
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment fragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.content_home, fragment).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Logger", "Home onConnectionFailed");
    }
}
