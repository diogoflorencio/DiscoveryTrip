package com.example.diogo.discoverytrip;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    // Dados Login facebook
    private ProfileTracker profileTracker;

    // TextView para log GPS
    TextView status_gps;
    TextView localizacao;

    //permissoes GPS
    private static final int REQUEST_LOCATION = 2;

    //provedor GPS
    LocationManager mlocManager;

    Localizacao Local;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // instanciando TextViews de log gps
        status_gps = (TextView) findViewById(R.id.status_gps);
        localizacao = (TextView) findViewById(R.id.localizacao);

        // Instanciando provedor GPS para obter coordenadas
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //instanciando classe aux de localizacao
        Local = new Localizacao();
        Local.setHome(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION));
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            //requisição de coordenadas ao provedor GPS
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    (LocationListener) Local);
            //Set log GPS
            status_gps.setText("Carregando Localização");
            localizacao.setText("");
        }

        profileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile, Profile currentProfile) {
                profileTracker.stopTracking();
                Profile.setCurrentProfile(currentProfile);
                Profile profile = Profile.getCurrentProfile();
                TextView textView = (TextView) findViewById(R.id.user);
                textView.setText(profile.getName());
            }
        };

        buildGooglePlusConfigs();
    }

    public void buildGooglePlusConfigs() {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.logout) {
            signOutGooglePlus();

            LoginManager.getInstance().logOut();
            Intent intent = new Intent(home.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOutGooglePlus() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED);
               //Requisitando localização do provedor de GPS
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
                // Set log GPS
                status_gps.setText("Carregando Localização");
                localizacao.setText("");

              } else; //Usuário rejeitou permissões
          }
      }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Implementando classe localizacao
    public class Localizacao implements LocationListener {
        home home;

        public home getHome() {
            return home;
        }

        public void setHome(home home) {
            this.home = home;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Obtendo coordenadas do GPS
            loc.getLatitude();
            loc.getLongitude();
            String Text = "Coordenadas de localização atual: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            status_gps.setText(Text);
            //OK!
        }

        @Override
        public void onProviderDisabled(String provider) {
            // GPS desativado
            status_gps.setText("GPS Desativado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // GPS ativado
            status_gps.setText("GPS Ativado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Método de monitoranmento dos serviços do de GPS
            // Status do provedor GPS:
            // OUT_OF_SERVICE -> Servidor fora de serviço
            // TEMPORARILY_UNAVAILABLE -> temporariamente indisponível aguardando serviço ser restabelecido
            // AVAILABLE -> Disponível
        }

    } //Fim da classe Localização
}
