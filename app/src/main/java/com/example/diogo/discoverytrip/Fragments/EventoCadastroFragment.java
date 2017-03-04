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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.MultiRequestHelper;
import com.example.diogo.discoverytrip.REST.ServerResponses.AddEventoResponse;

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

public class EventoCadastroFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public EditText nameVal_txt, descVal_txt, dateVal_txt, priceVal_txt;
    Spinner evKind_spn;
    private final int CAM_REQUEST = 1313;
    private final int CAM_SELECT = 1234;
    private String mCurrentPhotoPath;
    private Uri foto = null;

    public EventoCadastroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "EventoCadastroFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_evento_cadastro, container, false);

        rootView.findViewById(R.id.evConfirm_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evCancel_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evCamera_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evento_btnFoto).setOnClickListener(this);

        nameVal_txt = (EditText) rootView.findViewById(R.id.evName_edt);
        descVal_txt = (EditText) rootView.findViewById(R.id.evDesc_edt);
        dateVal_txt = (EditText) rootView.findViewById(R.id.evDate_edt);
        priceVal_txt = (EditText) rootView.findViewById(R.id.evPrice_edt);

        evKind_spn = (Spinner) rootView.findViewById(R.id.evKind_spn);
        evKind_spn.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.event_kind, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evKind_spn.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "EventoCadastroFragment onClick");
        switch (view.getId()) {
            case R.id.evConfirm_btn:
                Log.d("Logger", "EventoCadastroFragment botao confirmar");
                try {
                    validateFields();
                    sendEventData();
                    Toast.makeText(this.getActivity(), R.string.ev_cadastro_sucesso,Toast.LENGTH_SHORT).show();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.evCancel_btn:
                Log.d("Logger", "EventoCadastroFragment botao cancelar");
                backToHome();
                break;
            case R.id.evCamera_btn:
                Log.d("Logger", "EventoCadastroFragment botao camera");
                startCameraActivity();
                galleryAddPic();
                break;
            case R.id.evento_btnFoto:
                Log.d("Looger","EventoCadastroFragment selecionar foto");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), CAM_SELECT);
                break;
        }
    }

    private void startCameraActivity() {
        Log.d("Logger", "EventoCadastroFragment startCameraActivity");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("Logger", "EventoCadastroFragment onActivityResult " + CAM_REQUEST);
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
        Log.d("Logger", "EventoCadastroFragment createImageFile");
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
        Log.d("Logger", "EventoCadastroFragment galleryAddPic");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void backToHome() {
        Log.d("Logger", "EventoCadastroFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "EventoCadastroFragment validateFields");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(descVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_description));
        }

        if(dateVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_date));
        }

        if(priceVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_price));
        }

        if(foto == null){
            throw new DataInputException(getString(R.string.validate_photo));
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.d("Logger", "EventoCadastroFragment onItemSelected");
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("Logger", "EventoCadastroFragment onNothingSelected");
        // Another interface callback
    }

    public void sendEventData(){
        Log.d("Logger", "EventoCadastroFragment sendEventData");
        //precisa fazer um post e mandar os dados atualizados pro servidor

        //TODO corrigir e testar o metodo
        String eventName_value = nameVal_txt.getText().toString();
        String eventDesc_value = descVal_txt.getText().toString();
        String eventDate_value = dateVal_txt.getText().toString();
        String eventPrice_value = priceVal_txt.getText().toString();

        String eventKind_value = null;
        switch (evKind_spn.getSelectedItemPosition()) {
            case 0: eventKind_value = "private";
                break;
            case 1: eventKind_value = "public";
                break;
        }

        Map<String, RequestBody> parametersMap = new HashMap<>();
        MultiRequestHelper helper = new MultiRequestHelper(getContext());

        parametersMap.put("name",helper.createPartFrom(eventName_value));
        parametersMap.put("description",helper.createPartFrom(eventDesc_value));
        parametersMap.put("endData ",helper.createPartFrom(eventDate_value));
        parametersMap.put("kind",helper.createPartFrom(eventKind_value));
        parametersMap.put("price",helper.createPartFrom(eventPrice_value));

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Log.d("Token",token);
        //List<byte[]> fotos = new ArrayList<>();

        Call<AddEventoResponse> call = ApiClient.API_SERVICE.cadastrarEvento("bearer "+token,parametersMap);
        call.enqueue(new Callback<AddEventoResponse>() {
            @Override
            public void onResponse(Call<AddEventoResponse> call, Response<AddEventoResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("Server Evento","Cadastro OK");
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
            public void onFailure(Call<AddEventoResponse> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("App Server Error", t.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Logger", "EventoCadastroFragment onActivityResult");
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
}