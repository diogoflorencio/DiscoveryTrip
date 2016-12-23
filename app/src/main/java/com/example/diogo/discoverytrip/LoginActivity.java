package com.example.diogo.discoverytrip;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.AppLoginJson;
import com.example.diogo.discoverytrip.REST.ServerResponses.AppLoginResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponseLogin;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ApiInterface;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.AccessToken.getCurrentAccessToken;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private GoogleApiClient mGoogleApiClient;
    private EditText emailLogin, senhaLogin;
    private Button btnAppLogin;
    private Button recuperarSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Logger", "LoginActivity Oncreate");

        // iniciando SDK facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        // verificando validade de token facebook
        if(AccessToken.getCurrentAccessToken() != null){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }

        buildGooglePlusConfigs();

        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        findViewById(R.id.login_google).setOnClickListener(this);
        findViewById(R.id.lblCadastreSe).setOnClickListener(this);

        btnAppLogin = (Button) findViewById(R.id.btnLoginApp);
        btnAppLogin.setOnClickListener(this);

       recuperarSenha = (Button) findViewById(R.id.recuperarSenha);
       recuperarSenha.setOnClickListener(this);

    }

    public void buildGooglePlusConfigs() {
        Log.d("Logger", "LoginActivity buildGooglePlusConfigs");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Log.d("Logger", "LoginActivity signIn");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Logger", "LoginActivity handleSignInResult");
        if (result.isSuccess()) {
            GoogleSignInAccount googleUser = result.getSignInAccount();
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

            try {
                Log.d("Logger", "Nome");
                Log.d("Logger", googleUser.getDisplayName());
            }catch (Exception e){}
            try {
                Log.d("Logger", "Id token");
                Log.d("Logger", googleUser.getIdToken());
            }catch (Exception e){}
            try {
                Log.d("Logger", "Id");
                Log.d("Logger", googleUser.getId());
            }catch (Exception e){}
            try {
                Log.d("Logger", "Scopes");
                Log.d("Logger", googleUser.getGrantedScopes().toString());
            }catch (Exception e){}
            try {
                Log.d("Logger", "server authcode");
                Log.d("Logger", googleUser.getServerAuthCode());
            }catch (Exception e){}

            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Logger", "LoginActivity onActivityResult");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "LoginActivity onClick");
        switch (view.getId()) {
            case R.id.login_google:
                Log.d("Logger", "LoginActivity login google");
                signIn();
                break;
            case R.id.loginButton:
                Log.d("Logger", "LoginActivity login facebook");
                //permissões do facebook
                loginButton.setReadPermissions(Arrays.asList("public_profile","email"));

                // criando request facebook
                callbackManager = CallbackManager.Factory.create();
                //logando ao facebook
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        profileTracker = new ProfileTracker() {

                            @Override
                            protected void onCurrentProfileChanged(
                                    Profile oldProfile, Profile currentProfile) {
                                profileTracker.stopTracking();
                                Profile.setCurrentProfile(currentProfile);
                                Profile profile = Profile.getCurrentProfile();
                            }
                        };
                        profileTracker.startTracking();

                        postFacebook(getCurrentAccessToken().getToken());
                        Log.d("Logger", "startActivity facebook");
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(),R.string.login_cancel,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.lblCadastreSe:
                Log.d("Logger", "LoginActivity cadastrar");
                startActivity(new Intent(LoginActivity.this,CadastroActivity.class));
                finish();
                break;
            case R.id.btnLoginApp:
                Log.d("Logger", "LoginActivity login padrão");
                try {
                    loginApp();
                } catch (DataInputException e){
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recuperarSenha:
                Log.d("Logger", "LoginActivity recuperar senha");
                startActivity(new Intent(LoginActivity.this,RecuperarSenhaActivity.class));
                break;
        }
    }

    private void loginApp()throws DataInputException{
        Log.d("Logger", "LoginActivity loginApp");
        emailLogin = (EditText) findViewById(R.id.txtLoginEmail);
        senhaLogin = (EditText) findViewById(R.id.txtLoginSenha);

        if(emailLogin.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_email));
        }

        if(senhaLogin.getText().toString().isEmpty()){
            throw new DataInputException(getString(R.string.validate_password_empty));
        }
        
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<AppLoginResponse> call = apiService.appLogin(new AppLoginJson(emailLogin.getText().toString(),senhaLogin.getText().toString()));
        call.enqueue(new Callback<AppLoginResponse>() {
            @Override
            public void onResponse(Call<AppLoginResponse> call, Response<AppLoginResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("login","Server ok");
                }
                else{
                    Log.d("Server",""+response.code());
                }
            }

            @Override
            public void onFailure(Call<AppLoginResponse> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(LoginActivity.this, R.string.login_unable, Toast.LENGTH_SHORT).show();
                Log.e("App Server Error", t.toString());
            }
        });
    }

    private void postFacebook(String token){
        Log.d("Logger", "LoginActivity postFacebook");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ServerResponseLogin> call = apiService.loginFacebook(new AccessTokenJson(token));
        call.enqueue(new Callback<ServerResponseLogin>() {
            @Override
            public void onResponse(Call<ServerResponseLogin> call, Response<ServerResponseLogin> response) {
                if(response.isSuccessful()) {
                    Log.d("Login","Server OK");
                }
                else{
                    Log.e("Server",""+response.code());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseLogin> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("App Server Error", t.toString());
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Logger", "LoginActivity onConnectionFailed");
        Toast.makeText(this, R.string.conection_failed, Toast.LENGTH_SHORT).show();
    }

    protected void onStart() {
        Log.d("Logger", "LoginActivity onStart");
        super.onStart();
        Log.d("Logger", "onStart");
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
}
