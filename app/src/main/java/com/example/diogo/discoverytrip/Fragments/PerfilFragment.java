package com.example.diogo.discoverytrip.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.User;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ApiInterface;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.LoginResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe fragment responsavel pelo fragmento perfil na aplicação
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {
    public TextView userName, userEmail;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'PerfilFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PerfilFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        Button confirmarBtn = (Button) rootView.findViewById(R.id.pfConfirm_btn);
        confirmarBtn.setOnClickListener(this);
        userName = (TextView) rootView.findViewById(R.id.userName);
        userEmail = (TextView) rootView.findViewById(R.id.userEmail);

        getUserData();

        return rootView;
    }

    public void getUserData(){
        //funcao pra pegar os dados do perfil do usuário e colocar nos campos
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ServerResponse> call = apiService.getUsuario(
                AcessToken.recuperar(this.getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE)));
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Perfil", "Server OK");
                    ServerResponse serverResponse = response.body();
                    User user = serverResponse.getUsuario();
                    userName.setText(user.getNome());
                    userEmail.setText(user.getEmail());
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
                this.getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE)));
    }



    public void updateUserData(){
        //precisa fazer um post e mandar os dados atualizados pro servidor
        //funcao pra pegar os dados do perfil do usuário e colocar nos campos
        //TODO
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "PerfilFragment onClick");
        switch (view.getId()) {
            case R.id.pfConfirm_btn:
                Log.d("Logger", "PerfilFragment botao confirmar");
               /* try {
                    validateFields();
                    updateUserData();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }*/
                break;
        }
    }

    private void backToHome() {
        Log.d("Logger", "PerfilFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    /*private void validateFields() throws DataInputException {
        Log.d("Logger", "PerfilFragment validateFields");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(emailVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_email));
        }
    }*/
}