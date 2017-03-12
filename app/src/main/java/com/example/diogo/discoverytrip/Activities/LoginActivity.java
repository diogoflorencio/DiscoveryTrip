package com.example.diogo.discoverytrip.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.DataBase.RefreshToken;
import com.example.diogo.discoverytrip.DataBase.UserData;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.AppLoginJson;
import com.example.diogo.discoverytrip.Model.User;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.LoginResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.AccessToken.getCurrentAccessToken;

;

/**
 * Classe activity responsavel pelo login na aplicação
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private GoogleApiClient mGoogleApiClient;
    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'LoginActivity'
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Logger", "LoginActivity Oncreate");
        /* iniciando SDK facebook*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        loggedIn();
        /*iniciando SDK google*/
        buildGooglePlusConfigs();
        /*instanciando objetos da activity*/
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        findViewById(R.id.login_google).setOnClickListener(this);
        findViewById(R.id.lblCadastreSe).setOnClickListener(this);
        findViewById(R.id.btnLoginApp).setOnClickListener(this);
        findViewById(R.id.recuperarSenha).setOnClickListener(this);
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
                loginFacebook();
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

    private void loginFacebook(){
        /*permissões do facebook*/
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        /*criando request facebook*/
        callbackManager = CallbackManager.Factory.create();
        /*logando ao facebook*/
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Logger", "LoginActivity loginFacebook success");
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
                AcessToken.salvar(getCurrentAccessToken().getToken(),
                        getSharedPreferences("acessToken", Context.MODE_PRIVATE));
                postTokenFacebook(getCurrentAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.d("Logger", "LoginActivity loginFacebook cancel");
                Toast.makeText(getApplicationContext(),R.string.login_cancel,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Logger", "LoginActivity loginFacebook error");
                Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postTokenFacebook(final String token){
        Log.d("Logger", "LoginActivity postFacebook");
        Call<LoginResponse> call = ApiClient.API_SERVICE.loginFacebook(new AccessTokenJson(token));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) getUserData();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("App Server Error", t.toString());
            }
        });
    }

    private void loggedIn(){
        if (!AcessToken.recuperar(this.getSharedPreferences("acessToken", Context.MODE_PRIVATE)).equals("")){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void loginApp()throws DataInputException{
        Log.d("Logger", "LoginActivity loginApp");
        EditText emailLogin = (EditText) findViewById(R.id.txtLoginEmail);
        EditText senhaLogin = (EditText) findViewById(R.id.txtLoginSenha);

        if(emailLogin.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_email));
        }

        if(senhaLogin.getText().toString().isEmpty()){
            throw new DataInputException(getString(R.string.validate_password_empty));
        }

        final AlertDialog waitDialog = new AlertDialog.Builder(this).create();
        waitDialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.dialog_aguarde,null);
        TextView textView = (TextView) view.findViewById(R.id.dialog_aguarde_txtStatus);
        textView.setText("Conectando ao servidor...");
        waitDialog.setView(view);
        waitDialog.show();

        Call<LoginResponse> call = ApiClient.API_SERVICE.appLogin(new AppLoginJson(emailLogin.getText().toString(), senhaLogin.getText().toString()));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                waitDialog.dismiss();
                if(response.isSuccessful()) {
                    Log.d("Logger","Sucess login");
                    LoginResponse loginResponse = response.body();
                    RefreshToken.salvar(loginResponse.getRefreshtoken(),
                            getSharedPreferences("refreshToken", Context.MODE_PRIVATE));
                    AcessToken.salvar(loginResponse.getAccesstoken(),
                            getSharedPreferences("acessToken", Context.MODE_PRIVATE));
                    getUserData();
                }
                else{
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Toast.makeText(LoginActivity.this,error.getErrorDescription(),Toast.LENGTH_SHORT).show();
                        Log.e("Server Error",error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                waitDialog.dismiss();
                Toast.makeText(LoginActivity.this, R.string.falha_conexao, Toast.LENGTH_SHORT).show();
                Log.e("App Server Error", t.toString());
            }
        });
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
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void getUserData(){
        //funcao pra pegar os dados do perfil do usuário e colocar nos campos
        Call<ServerResponse> call = ApiClient.API_SERVICE.getUsuario("bearer "+
                AcessToken.recuperar(this.getSharedPreferences("acessToken", Context.MODE_PRIVATE)));
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Perfil", "Server OK");
                    ServerResponse serverResponse = response.body();
                    User user = serverResponse.getUsuario();
                    UserData.salvar(user.getId(),user.getNome(),user.getEmail(),
                            getSharedPreferences("userData", Context.MODE_PRIVATE));
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                } else {
                    try {
                        Log.e("Perfil", "" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("Perfil", "Server" + t.toString());
            }
        });
    }
}
