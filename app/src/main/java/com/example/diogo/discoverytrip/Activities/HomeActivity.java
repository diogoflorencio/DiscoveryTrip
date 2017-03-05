package com.example.diogo.discoverytrip.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Fragments.EventoFragment;
import com.example.diogo.discoverytrip.Fragments.HomeFragment;
import com.example.diogo.discoverytrip.Fragments.LocalizacaoFragment;
import com.example.diogo.discoverytrip.Fragments.PerfilFragment;
import com.example.diogo.discoverytrip.Fragments.PontoTuristicoFragment;
import com.example.diogo.discoverytrip.Model.RefreshTokenManeger;
import com.example.diogo.discoverytrip.Model.User;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;
import com.example.diogo.discoverytrip.Service.ServiceLembrete;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Classe activity responsavel pela activity home (principal) na aplicação
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    User user;

    private GoogleApiClient mGoogleApiClient;

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'HomeActivity'
     */
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

        /* start Thread refreshToken */
        RefreshTokenManeger.refreshToken(getSharedPreferences("refreshToken", Context.MODE_PRIVATE));

        buildGooglePlusConfigs();

        try{
            getUserData();
        } catch (Exception e){
            e.printStackTrace();
        }

        createHomeFragment();

        /*start ServiceLembrete*/
        if(!ServiceLembrete.isRun())
            startService(new Intent(HomeActivity.this, ServiceLembrete.class));
    }

    public void sendDatatoPerfil(String name, String email, String id, Fragment fragment){
        Log.d("Logger", "Home sendDatatoPerfil");

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
    }

    public void getUserData(){
        Log.d("Logger", "Home getUserData");
        //funcao pra pegar os dados do perfil do usuário e colocar nos campos
        user = null;
        Call<ServerResponse> call = ApiClient.API_SERVICE.getUsuario("bearer "+
                AcessToken.recuperar(this.getSharedPreferences("acessToken", Context.MODE_PRIVATE)));
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Perfil", "Server OK");
                    ServerResponse serverResponse = response.body();
                    user = serverResponse.getUsuario();
                    try{
                        Log.d("Perfil", user.getId());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    // try {
                    // ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                    try {
                        Log.e("Perfil", "" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // } catch (IOException e) {
                    //  e.printStackTrace();
                    // }
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("Perfil", "Server" + t.toString());
            }

        });

        Log.d("Perfil", "AcessToken " + AcessToken.recuperar(
                this.getSharedPreferences("acessToken", Context.MODE_PRIVATE)));
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
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("Logger", "Home onCreateOptionsMenu");
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

                if (getCurrentAccessToken() != null)
                    AccessToken.setCurrentAccessToken(null);/*logout facebook*/
                else if(RefreshTokenManeger.isRunning()) RefreshTokenManeger.stop(); /*stop thread RefreshTokenManeger*/
                else signOutGooglePlus();/*logout google plus*/

                stopService(new Intent(HomeActivity.this, ServiceLembrete.class));/*stop ServiceLembrete*/

                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
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

    //pra adicionar uma opção aqui (menu lateral), basta colocar um item no "activity_home_drawer", botar um case nesse switch
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("Logger", "Home onNavigationItemSelected");
        int id = item.getItemId();
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                Log.d("Logger", "Home localizacao");
                fragment = new HomeFragment();
                break;
            case R.id.nav_localizacao:
                Log.d("Logger", "Home localizacao");
                fragment = new LocalizacaoFragment();
                break;
            case R.id.nav_perfil:
                Log.d("Logger", "Home localizacao");
                String name = null;
                String email = null;
                String password = null;
                String user_id = null;
                try{
                    name = user.getNome();
                    email = user.getEmail();
                    user_id = user.getId();
                } catch (Exception e) {
                    //o usuário nao foi recuperado no servidor
                }
                fragment = new PerfilFragment();
                sendDatatoPerfil(name, email, user_id, fragment);
                break;
            case R.id.nav_ponto_turistico:
                Log.d("Logger", "Home localizacao");
                fragment = new PontoTuristicoFragment();
                break;
            case R.id.nav_evento:
                Log.d("Logger", "Home localizacao");
                fragment = new EventoFragment();
                break;
            case R.id.nav_map:
                Log.d("Logger", "Home map");
                startActivity(new Intent(HomeActivity.this,MapsActivity.class));
                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
            Bundle extras = getIntent().getExtras();
            if(extras !=null) {
                fragment.setArguments(extras);
            }
            fragmentManager.setCustomAnimations(R.anim.left_in, R.anim.right_out);
            fragmentManager.replace(R.id.content_home, fragment);
            fragmentManager.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createHomeFragment() {
        Log.d("Logger", "Home createHomeFragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();

        transaction.add(R.id.content_home, fragment);

        transaction.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Logger", "Home onConnectionFailed");
    }
}
