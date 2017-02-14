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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
public class PontoTuristicoFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public EditText nameVal_txt, catgVal_txt, descVal_txt;
    private Uri foto;
    Spinner ptCategory_spn;

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
        descVal_txt = (EditText) rootView.findViewById(R.id.pntDescVal_txt);

        ptCategory_spn = (Spinner) rootView.findViewById(R.id.ptCategory_spn);
        ptCategory_spn.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.attraction_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ptCategory_spn.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "PontoTuristicoFragment onClick");
        switch (view.getId()) {
            case R.id.pntRegister_btn:
                Log.d("Logger", "PontoTuristicoFragment botao registrar");
                try {
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
        Log.d("Looger","PontoTuristicoFragment onActivityResult");
        if(requestCode == 1234 && resultCode == RESULT_OK) {
            foto = data.getData();
            Log.d("Logger","Seleciona imagem"+foto.getPath());
        }
    }

    public void postData(){
        Log.d("Looger","PontoTuristicoFragment postData");
        String ptName_value = nameVal_txt.getText().toString();
        String ptDesc_value = descVal_txt.getText().toString();
        String ptCatg_value = null;
        switch (ptCategory_spn.getSelectedItemPosition()) {
            case 0: ptCatg_value = "beaches";
                break;
            case 1: ptCatg_value = "island resorts";
                break;
            case 2: ptCatg_value = "parks";
                break;
            case 3: ptCatg_value = "forests";
                break;
            case 4: ptCatg_value = "monuments";
                break;
            case 5: ptCatg_value = "temples";
                break;
            case 6: ptCatg_value = "zoos";
                break;
            case 7: ptCatg_value = "aquariums";
                break;
            case 8: ptCatg_value = "museums";
                break;
            case 9: ptCatg_value = "art galleries";
                break;
            case 10: ptCatg_value = "botanical";
                break;
            case 11: ptCatg_value = "gardens";
                break;
            case 12: ptCatg_value = "castles";
                break;
            case 13: ptCatg_value = "libraries";
                break;
            case 14: ptCatg_value = "prisons";
                break;
            case 15: ptCatg_value = "skyscrapers";
                break;
            case 16: ptCatg_value = "bridges";
                break;
        }

        Map<String, RequestBody> parametersMap = new HashMap<>();
        MultiRequestHelper helper = new MultiRequestHelper(getContext());

        parametersMap.put("name",helper.createPartFrom(ptName_value));
        parametersMap.put("description",helper.createPartFrom(ptDesc_value));

        parametersMap.put("latitude",helper.createPartFrom(String.valueOf(LocalizacaoFragment.latitude)));
        parametersMap.put("longitude",helper.createPartFrom(String.valueOf(LocalizacaoFragment.longitude)));

        parametersMap.put("category ",helper.createPartFrom(ptCatg_value));

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
                        //Log.e("Server error",response.errorBody().string());
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Server", error.getErrorDescription());
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

        if(descVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_description));
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.d("Logger", "PontoTuristicoFragment onItemSelected");
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}