package com.example.diogo.discoverytrip.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Activities.LoginActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.DataBase.RefreshToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.Model.AccessTokenJson;
import com.example.diogo.discoverytrip.Model.PontoTuristico;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.MultiRequestHelper;
import com.example.diogo.discoverytrip.REST.ServerResponses.AddEventoResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.AttractionResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.LoginResponse;
import com.facebook.AccessToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Classe fragment responsavel pelo fragmento ponto turistico na aplicação
 */
public class PontoTuristicoFragment extends Fragment implements View.OnClickListener {
    public EditText nameVal_txt, catgVal_txt, descVal_txt;
    private Uri foto;

    public PontoTuristicoFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'PontoTuristicoFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PontoTuristicoFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_ponto_turistico, container, false);

        Button cadastrarBtn = (Button) rootView.findViewById(R.id.pntRegister_btn);
        cadastrarBtn.setOnClickListener(this);

        Button selecionarFoto = (Button) rootView.findViewById(R.id.ponto_turistico_btnFoto);
        selecionarFoto.setOnClickListener(this);

        nameVal_txt = (EditText) rootView.findViewById(R.id.pntNameVal_txt);
        catgVal_txt = (EditText) rootView.findViewById(R.id.pntCatgVal_txt);
        descVal_txt = (EditText) rootView.findViewById(R.id.pntDescVal_txt);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "PontoTuristicoFragment onClick");
        switch (view.getId()) {
            case R.id.pntRegister_btn:
                Log.d("Logger", "PontoTuristicoFragment botao registrar");
                try {
                    backToHome();


                    validateFields();
                    postData();
                    Toast.makeText(this.getActivity(), R.string.pt_cadastro_sucesso,Toast.LENGTH_SHORT).show();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ponto_turistico_btnFoto:
                Log.d("Looger","Ponto turistico selecionar foto");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 1234);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1234 && resultCode == RESULT_OK) {
            foto = data.getData();
            Log.d("Logger","Seleciona imagem"+foto.getPath());
        }
    }

    public void postData(){
        //logica aqui
        //precisa fazer um post e mandar o cadastro pro servidor
        //TODO
        String ptName_value = nameVal_txt.getText().toString();
        String ptCatg_value = catgVal_txt.getText().toString();
        String ptDesc_value = descVal_txt.getText().toString();

        Map<String, RequestBody> parametersMap = new HashMap<>();
        MultiRequestHelper helper = new MultiRequestHelper(getContext());

        parametersMap.put("name",helper.createPartFrom(ptName_value));
        parametersMap.put("description",helper.createPartFrom(ptDesc_value));
        parametersMap.put("latitude",helper.createPartFrom("-7.2335"));
        parametersMap.put("longitude",helper.createPartFrom("-35.8727"));

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Log.d("Token",token);
        //List<byte[]> fotos = new ArrayList<>();

        Call<AttractionResponse> call = ApiClient.API_SERVICE.cadastrarPontoTuristico("bearer "+token,parametersMap,helper.loadPhoto("photos",foto));
        call.enqueue(new Callback<AttractionResponse>() {
            @Override
            public void onResponse(Call<AttractionResponse> call, Response<AttractionResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("Server Ponto Turístico","Cadastro OK");
                }
                else{
                    try {
                        Log.e("Server error",response.errorBody().string());
                        //ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        //Log.e("Server", error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AttractionResponse> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("App Server Error", t.toString());
            }
        });
    }

    private void backToHome() {
        Log.d("Logger", "PontoTuristicoFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

//        getActivity().getSupportFragmentManager().popBackStack();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "PontoTuristicoFragment validate");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(catgVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_category));
        }

        if(descVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_description));
        }
    }
}