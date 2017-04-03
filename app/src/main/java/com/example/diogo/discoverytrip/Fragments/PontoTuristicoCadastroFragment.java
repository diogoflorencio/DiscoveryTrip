package com.example.diogo.discoverytrip.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Activities.MapsActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.MultiRequestHelper;
import com.example.diogo.discoverytrip.REST.ServerResponses.AttractionResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Classe fragment responsavel pelo fragmento ponto turistico na aplicação
 */
public class PontoTuristicoCadastroFragment extends Fragment implements LocationListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    public EditText nameVal_txt, descVal_txt;
    private TextView pntPictureCnt_txt;
    private ImageView pictureImgView;
    private List<Uri> selectedPictures;
    Spinner ptCategory_spn;
    private final int CAM_REQUEST = 1313;
    private final int CAM_SELECT = 1234;
    private double latitude,longitude;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 2;
    static final int PNTTURISTICOCAD = 1;

    public PontoTuristicoCadastroFragment() {
        Log.d("Logger", "PontoTuristicoCadastroFragment PontoTuristicoCadastroFragment");
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

        getActivity().setTitle(R.string.cadastro_ponto_turistico_label);
        selectedPictures = new ArrayList<Uri>();

        startGPS();
        Button cadastrarBtn = (Button) rootView.findViewById(R.id.pntRegister_btn);
        cadastrarBtn.setOnClickListener(this);
        rootView.findViewById(R.id.pntCancel_btn).setOnClickListener(this);

        rootView.findViewById(R.id.pntCamera_btn).setOnClickListener(this);
        Button selecionarFoto = (Button) rootView.findViewById(R.id.ponto_turistico_btnFoto);
        selecionarFoto.setOnClickListener(this);
        rootView.findViewById(R.id.pntMap_btn).setOnClickListener(this);

        nameVal_txt = (EditText) rootView.findViewById(R.id.pntNameVal_txt);
        descVal_txt = (EditText) rootView.findViewById(R.id.pntDescVal_txt);

        pictureImgView = (ImageView) rootView.findViewById(R.id.pntPictureImgView);
        pntPictureCnt_txt = (TextView) rootView.findViewById(R.id.pntPictureCnt_txt);

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
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ponto_turistico_btnFoto:
                Log.d("Logger","Ponto turistico selecionar foto");
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
                break;
            case R.id.pntMap_btn:
                Log.d("Logger", "PontoTuristicoCadastroFragment botao mapa");
                openMapActivity();
                break;
        }
    }

    private void openMapActivity(){
        Log.d("Logger", "PontoTuristicoCadastroFragment openMapActivity");
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        startActivityForResult(mapIntent, PNTTURISTICOCAD);
    }

    private void startCameraActivity(){
        Log.d("Logger", "PontoTuristicoCadastroFragment startCameraActivity");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));
                startActivityForResult(intent, CAM_REQUEST);
        }
    }

    private void addPicture(Uri pictureUri) throws DataInputException {
        Log.d("Logger","PontoTuristicoCadastroFragment addPicture");
        if (selectedPictures.size() < 10){
            selectedPictures.add(pictureUri);
        }
        else {
            throw new DataInputException(getString(R.string.validate_max_photo));
        }
    }

    private void updatePictureCounter(){
        Log.d("Logger","PontoTuristicoCadastroFragment updatePictureCounter");
        pntPictureCnt_txt.setText(selectedPictures.size()+"/10");
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void displayPicture(Uri pictureUri){
        Log.d("Logger", "PontoTuristicoCadastroFragment displayPicture");
        Bitmap imageBitmap = BitmapFactory.decodeFile(getPathFromURI(pictureUri));
        pictureImgView.setImageBitmap(imageBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Logger","PontoTuristicoCadastroFragment onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        Uri foto = null;
        if(requestCode == CAM_SELECT && resultCode == RESULT_OK) {
            Log.d("Logger", "PontoTuristicoCadastroFragment onActivityResult CAM_SELECT " + CAM_SELECT);
            foto = data.getData();
            try {
                addPicture(foto);
                displayPicture(foto);
                updatePictureCounter();
            } catch (DataInputException exception){
                Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
            Log.d("Logger","Seleciona imagem" + foto.getPath());
            Log.d("Logger","Seleciona imagem real" + getPathFromURI(foto));
        }

        if(requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
            Log.d("Logger", "PontoTuristicoCadastroFragment onActivityResult CAM_REQUEST " + CAM_REQUEST);
            try{
                foto = data.getData();
            } catch (Exception e){
                e.printStackTrace();
                File file = new File("/sdcard/tmp");
                try {
                    foto = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), null, null));
                    try {
                        addPicture(foto);
                        displayPicture(foto);
                        updatePictureCounter();
                    } catch (DataInputException exception){
                        Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    Log.d("Logger","Seleciona imagem" + foto.getPath());
                    if (!file.delete()) {
                        Log.d("logMarker", "Failed to delete " + file);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        if(requestCode == PNTTURISTICOCAD  && resultCode == RESULT_OK) {
            Log.d("Logger", "PontoTuristicoCadastroFragment onActivityResult PNTTURISTICOCAD");

            try{
                getActivity().getIntent().putExtra("Lat", data.getStringExtra("Lat"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                getActivity().getIntent().putExtra("Lng", data.getStringExtra("Lng"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String translateCategory(){
        Log.d("Logger", "PontoTuristicoCadastroFragment translateCategory");
        String categoryVal = "";

        switch (ptCategory_spn.getSelectedItemPosition()) {
            case 0: categoryVal = "beaches";
                break;
            case 1: categoryVal = "island resorts";
                break;
            case 2: categoryVal = "parks";
                break;
            case 3: categoryVal = "forests";
                break;
            case 4: categoryVal = "monuments";
                break;
            case 5: categoryVal = "temples";
                break;
            case 6: categoryVal = "zoos";
                break;
            case 7: categoryVal = "aquariums";
                break;
            case 8: categoryVal = "museums";
                break;
            case 9: categoryVal = "art galleries";
                break;
            case 10: categoryVal = "botanical";
                break;
            case 11: categoryVal = "gardens";
                break;
            case 12: categoryVal = "castles";
                break;
            case 13: categoryVal = "libraries";
                break;
            case 14: categoryVal = "prisons";
                break;
            case 15: categoryVal = "skyscrapers";
                break;
            case 16: categoryVal = "bridges";
                break;
        }

        return categoryVal;
    }

    public void postData(){
        Log.d("Logger","PontoTuristicoCadastroFragment postData");
        createLoadingDialog();
        String ptName_value = nameVal_txt.getText().toString();
        String ptDesc_value = descVal_txt.getText().toString();
        String ptCatg_value = translateCategory();
        
        Map<String, RequestBody> parametersMap = new HashMap<>();
        MultiRequestHelper helper = new MultiRequestHelper(getContext());

        parametersMap.put("name",helper.createPartFrom(ptName_value));
        parametersMap.put("description",helper.createPartFrom(ptDesc_value));

        try{
            //o coordenadas escolhidas pela "map activity"
            parametersMap.put("latitude",helper.createPartFrom(getArguments().getString("Lat")));
            parametersMap.put("longitude",helper.createPartFrom(getArguments().getString("Lng")));
        }
        catch (Exception e){
            //o coordenadas não foram escolhidas pela "map activity"
            parametersMap.put("latitude",helper.createPartFrom(String.valueOf(latitude)));
            parametersMap.put("longitude",helper.createPartFrom(String.valueOf(longitude)));
        }

        parametersMap.put("category",helper.createPartFrom(ptCatg_value));

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Log.d("Token",token);
        Log.d("Logger", "name " + ptName_value + " description " + ptDesc_value + " category " + ptCatg_value);

        final AlertDialog dialog = createLoadingDialog();
        dialog.show();
        MultipartBody.Part picture1 = null;
        MultipartBody.Part picture2 = null;
        MultipartBody.Part picture3 = null;
        MultipartBody.Part picture4 = null;
        MultipartBody.Part picture5 = null;
        MultipartBody.Part picture6 = null;
        MultipartBody.Part picture7 = null;
        MultipartBody.Part picture8 = null;
        MultipartBody.Part picture9 = null;
        MultipartBody.Part picture10 = null;

        try {
            Log.d("Logger","Carregando imagem getPathFromURI" + getPathFromURI(selectedPictures.get(0)));
            picture1 = helper.loadPhoto("photos", selectedPictures.get(0));
            picture2 = helper.loadPhoto("photos", selectedPictures.get(1));
            picture3 = helper.loadPhoto("photos", selectedPictures.get(2));
            picture4 = helper.loadPhoto("photos", selectedPictures.get(3));
            picture5 = helper.loadPhoto("photos", selectedPictures.get(4));
            picture6 = helper.loadPhoto("photos", selectedPictures.get(5));
            picture7 = helper.loadPhoto("photos", selectedPictures.get(6));
            picture8 = helper.loadPhoto("photos", selectedPictures.get(7));
            picture9 = helper.loadPhoto("photos", selectedPictures.get(8));
            picture10 = helper.loadPhoto("photos", selectedPictures.get(9));
        }
        catch (Exception e){
            //nao tinham as 10 fotos, então so instaciou as que tinham
            e.printStackTrace();
        }

        Call<AttractionResponse> call = ApiClient.API_SERVICE.cadastrarPontoTuristico("bearer "+token,parametersMap, picture1, picture2, picture3, picture4, picture5, picture6, picture7, picture8, picture9, picture10);
        call.enqueue(new Callback<AttractionResponse>() {
            @Override
            public void onResponse(Call<AttractionResponse> call, Response<AttractionResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getActivity(), R.string.pt_cadastro_sucesso,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    backToHome();
                }
                else{
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Server", error.getErrorDescription());
                        Toast.makeText(getActivity(), error.getErrorDescription(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AttractionResponse> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getContext(), "Erro ao se conectar com o servidor!", Toast.LENGTH_SHORT).show();
                Log.e("Looger",t.toString());
                dialog.dismiss();
            }
        });
    }

    private void backToHome() {
        Log.d("Logger", "PontoTuristicoCadastroFragment backToHome");

        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();

//        if(getActivity().getClass().equals(HomeActivity.class)){
//            FragmentManager fragmentManager = getFragmentManager();
//            HomeFragment fragment = new HomeFragment();
//            fragmentManager.beginTransaction().replace(R.id.content_home, fragment
//            ).commit();
//        }
//        if(getActivity().getClass().equals(MapsActivity.class)){
//            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//
//            MapsActivity currentActivity = (MapsActivity) getActivity();
//            currentActivity.showInterface();
//        }
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "PontoTuristicoCadastroFragment validate");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_pnt_name));
        }

        if(descVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_description));
        }

        if(selectedPictures.size() == 0){
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

    private AlertDialog createLoadingDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_aguarde,null);
        dialog.setView(dialogView);
        dialog.setTitle("Enviando");
        dialog.setCancelable(false);
        return dialog;
    }

    private void startGPS() {
        Log.d("Logger", "LocalizacaoFragment startGPS");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else
        if(verificaConexao())
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
    }

    private boolean verificaConexao() {
        //verificando se o dispositivo tem conexão com internet
        ConnectivityManager conectivtyManager =
                (ConnectivityManager) this.getActivity().getSystemService(this.getActivity().CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "LocalizacaoFragment onDestroy");
        super.onDestroy();
        stopGPS();
    }

    private void stopGPS(){
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else locationManager.removeUpdates(this);
    }
}