package com.example.diogo.discoverytrip.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Activities.CadastroActivity;
import com.example.diogo.discoverytrip.Activities.LoginActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.Model.UsuarioEnvio;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.MultiRequestHelper;
import com.example.diogo.discoverytrip.REST.ServerResponses.AttractionResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe fragment responsavel pelo fragmento de edição de perfil na aplicação
 */
public class PerfilEditFragment extends Fragment implements View.OnClickListener {
    public EditText userName_edt, userEmail_edt;

    public PerfilEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PerfilEditFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_perfil_edit, container, false);

        rootView.findViewById(R.id.pfeConfirm_btn).setOnClickListener(this);
        rootView.findViewById(R.id.pfeCancel_btn).setOnClickListener(this);

        userName_edt = (EditText) rootView.findViewById(R.id.pfeName_edt);
        userEmail_edt = (EditText) rootView.findViewById(R.id.pfeEmail_edt);

        return rootView;
    }

    private void updateUserData(){
        Log.d("Looger","PerfilEditFragment updateUserData");
        //TODO testar o metodo e falta adicionar o id ao url
        String userName_value = userName_edt.getText().toString();
        String userEmail_value = userEmail_edt.getText().toString();
        String userPassword_value = null;
        String userId_value = null;

        Map<String, RequestBody> parametersMap = new HashMap<>();
        MultiRequestHelper helper = new MultiRequestHelper(getContext());

        parametersMap.put("password",helper.createPartFrom(userPassword_value));
        parametersMap.put("username",helper.createPartFrom(userName_value));
        parametersMap.put("email",helper.createPartFrom(userEmail_value));

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Log.d("Token",token);

        Call<ServerResponse> call = ApiClient.API_SERVICE.setUsuario(new UsuarioEnvio(userName_value, userEmail_value, userPassword_value));
        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();
                    Log.d("Server Response", serverResponse.getMessage());
                    Toast.makeText(getActivity(), R.string.pf_edicao_sucesso, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Toast.makeText(getActivity(), error.getErrorDescription(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getActivity(), R.string.pf_edicao_falha, Toast.LENGTH_SHORT).show();
                Log.e("App Server Error", t.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "PerfilEditFragment onClick");
        switch (view.getId()) {
            case R.id.pfeConfirm_btn:
                Log.d("Logger", "PerfilEditFragment botao confirmar");
                try {
                    validateFields();
//                    updateUserData();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.pfeCancel_btn:
                Log.d("Logger", "PerfilEditFragment botao cancelar");
                backToHome();
        }
    }

    private void backToHome() {
        Log.d("Logger", "PerfilEditFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "PerfilEditFragment validateFields");
        if(userName_edt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(userEmail_edt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_email));
        }
    }
}
