package com.example.diogo.discoverytrip.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
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

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.MultiRequestHelper;
import com.example.diogo.discoverytrip.REST.ServerResponses.AttractionResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Classe fragment responsavel pelo fragmento ponto turistico na aplicação
 */
public class PontoTuristicoCadastroFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public EditText nameVal_txt, descVal_txt;
    private Uri foto = null;
    Spinner ptCategory_spn;
    private final int CAM_REQUEST = 1313;
    private final int CAM_SELECT = 1234;
    private String mCurrentPhotoPath;

    public PontoTuristicoCadastroFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'PontoTuristicoCadastroFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PontoTuristicoCadastroFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_ponto_turistico_cadastro, container, false);

        Button cadastrarBtn = (Button) rootView.findViewById(R.id.pntRegister_btn);
        cadastrarBtn.setOnClickListener(this);
        rootView.findViewById(R.id.pntCancel_btn).setOnClickListener(this);

        rootView.findViewById(R.id.pntCamera_btn).setOnClickListener(this);
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
        Log.d("Logger", "PontoTuristicoCadastroFragment onClick");
        switch (view.getId()) {
            case R.id.pntRegister_btn:
                Log.d("Logger", "PontoTuristicoCadastroFragment botao registrar");
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
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), CAM_SELECT);
                break;
            case R.id.pntCancel_btn:
                Log.d("Logger", "PontoTuristicoCadastroFragment botao cancelar");
                backToHome();
                break;
            case R.id.pntCamera_btn:
                Log.d("Logger", "PontoTuristicoCadastroFragment botao camera");
                startCameraActivity();
                galleryAddPic();
                break;
        }
    }

    private void startCameraActivity(){
        Log.d("Logger", "PontoTuristicoCadastroFragment startCameraActivity");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("Logger", "PontoTuristicoCadastroFragment onActivityResult " + CAM_REQUEST);
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // Error occurred while creating the File
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAM_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        Log.d("Logger", "PontoTuristicoCadastroFragment createImageFile");
        // Create a collision-resistant image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Log.d("Logger", "PontoTuristicoCadastroFragment galleryAddPic");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Log.d("Logger", "PontoTuristicoCadastroFragment galleryAddPic1");
        try{
            File f = new File(mCurrentPhotoPath);
            Log.d("Logger", "PontoTuristicoCadastroFragment galleryAddPic2");
            Uri contentUri = Uri.fromFile(f);
            Log.d("Logger", "PontoTuristicoCadastroFragment galleryAddPic3");
            mediaScanIntent.setData(contentUri);
            Log.d("Logger", "PontoTuristicoCadastroFragment galleryAddPic4");
            getActivity().sendBroadcast(mediaScanIntent);
            Log.d("Logger", "PontoTuristicoCadastroFragment galleryAddPic5");
        } catch (Exception e){
            //path não existe
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Looger","PontoTuristicoCadastroFragment onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_SELECT && resultCode == RESULT_OK) {
            Log.d("Logger", "PontoTuristicoCadastroFragment onActivityResult " + CAM_SELECT);
            foto = data.getData();
            Log.d("Logger","Seleciona imagem"+foto.getPath());
        }

//        if(requestCode == CAM_REQUEST) {
//            Log.d("Logger", "EventoCadastroFragment onActivityResult " + CAM_REQUEST);
//            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//        }
    }

    public void postData(){
        Log.d("Looger","PontoTuristicoCadastroFragment postData");
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
        parametersMap.put("latitude",helper.createPartFrom("-7.2335"));
        parametersMap.put("longitude",helper.createPartFrom("-35.8727"));
        parametersMap.put("category ",helper.createPartFrom(ptCatg_value));

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Log.d("Token",token);
        //List<byte[]> fotos = new ArrayList<>();

        Call<AttractionResponse> call = ApiClient.API_SERVICE.cadastrarPontoTuristico("bearer "+token,parametersMap,helper.loadPhoto("photos",foto));
        call.enqueue(new Callback<AttractionResponse>() {
            @Override
            public void onResponse(Call<AttractionResponse> call, Response<AttractionResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("Cadastro de ponto turístico","Cadastro OK");
                    Toast.makeText(getActivity(), R.string.pt_cadastro_sucesso,Toast.LENGTH_SHORT).show();
                    backToHome();
                }
                else{
                    try {
                        Log.e("Cadastro de ponto turístico",response.errorBody().string());
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
                Log.e("Cadastro de ponto turístico", t.toString());
            }
        });
    }

    private void backToHome() {
        Log.d("Logger", "PontoTuristicoCadastroFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "PontoTuristicoCadastroFragment validate");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(descVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_description));
        }

        if(foto == null){
            throw new DataInputException(getString(R.string.validate_photo));
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.d("Logger", "PontoTuristicoCadastroFragment onItemSelected");
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}