package com.example.diogo.discoverytrip.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.Activities.MapsActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.MultiRequestHelper;
import com.example.diogo.discoverytrip.REST.ServerResponses.AddEventoResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.Util.DatePickerFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class EventoCadastroFragment extends Fragment implements LocationListener, View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerFragment.DatePickerFragmentListener {
    public EditText nameVal_txt, descVal_txt, priceVal_txt;
    private Date dateVal_date;
    public TextView dateVal_txt;
    Spinner evKind_spn;
    private final int CAM_REQUEST = 1313;
    private final int CAM_SELECT = 1234;
    private String mCurrentPhotoPath;
    private Uri foto = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
    private SimpleDateFormat normalDateFormat = new SimpleDateFormat("dd/M/yyyy");
    private double latitude,longitude;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 2;

    public EventoCadastroFragment() {
        Log.d("Logger", "EventoCadastroFragment EventoCadastroFragment");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "EventoCadastroFragment onCreate");
        startGPS();
        View rootView = inflater.inflate(R.layout.fragment_evento_cadastro, container, false);

        rootView.findViewById(R.id.evConfirm_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evCancel_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evCamera_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evento_btnFoto).setOnClickListener(this);
        rootView.findViewById(R.id.evDatePicker_btn).setOnClickListener(this);

        nameVal_txt = (EditText) rootView.findViewById(R.id.evName_edt);
        descVal_txt = (EditText) rootView.findViewById(R.id.evDesc_edt);
        priceVal_txt = (EditText) rootView.findViewById(R.id.evPrice_edt);

        dateVal_txt = (TextView) rootView.findViewById(R.id.dateVal_txt);

        evKind_spn = (Spinner) rootView.findViewById(R.id.evKind_spn);
        evKind_spn.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.event_kind, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evKind_spn.setAdapter(adapter);

        return rootView;
    }

    public void datePicker(View view){
        Log.d("Logger", "EventoCadastroFragment DatePicker");
        DatePickerFragment fragment = DatePickerFragment.newInstance(this);
        fragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void setDate(int year, int month, int dayOfMonth) {
        Log.d("Logger", "EventoCadastroFragment setDate");
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        month = month +1;
        dateVal_txt.setText(dayOfMonth+"/"+month+"/"+year);
        Log.d("Logger", "data " + dateVal_txt.getText());
    }

    @Override
    public void onDateSet(Date date) {
        Log.d("Logger", "EventoCadastroFragment onDateSet");
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int dayOfMonth = date.getDate();
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        setDate(year, month, dayOfMonth);
        dateVal_date = calendar.getTime();
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
                Log.d("Logger","EventoCadastroFragment selecionar foto");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), CAM_SELECT);
                break;
            case R.id.evDatePicker_btn:
                Log.d("Logger", "EventoCadastroFragment botao datePicker");
                datePicker(view);
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
        //isso provavelmente nao funciona porque o path esta incorreto, não funciona
        Log.d("Logger", "EventoCadastroFragment galleryAddPic");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void backToHome() {
        Log.d("Logger", "EventoCadastroFragment backToHome");

        if(getActivity().getClass().equals(HomeActivity.class)){
            FragmentManager fragmentManager = getFragmentManager();
            HomeFragment fragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.content_home, fragment
            ).commit();
        }
        if(getActivity().getClass().equals(MapsActivity.class)){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

            MapsActivity currentActivity = (MapsActivity) getActivity();
            currentActivity.showInterface();
        }
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "EventoCadastroFragment validateFields");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(dateVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_date));
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

        String eventName_value = nameVal_txt.getText().toString();
        String eventDesc_value = descVal_txt.getText().toString();
        String eventDate_value = dateVal_txt.getText().toString();
        Log.d("Logger","Date txt "+eventDate_value);
        String eventDate_formated = null;
        try {
            Date date = normalDateFormat.parse(eventDate_value);
            date.toString();
            eventDate_formated = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Logger","Date formated "+eventDate_formated);
        String eventPrice_value = priceVal_txt.getText().toString();
        eventPrice_value.replaceAll(".",",");
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

        parametersMap.put("endDate",helper.createPartFrom(eventDate_formated));

        parametersMap.put("kind",helper.createPartFrom(eventKind_value));
        parametersMap.put("price",helper.createPartFrom(eventPrice_value));

//        parametersMap.put("latitude",helper.createPartFrom(String.valueOf(latitude)));
//        parametersMap.put("longitude",helper.createPartFrom(String.valueOf(longitude)));

        try{
            parametersMap.put("latitude",helper.createPartFrom(String.valueOf(getArguments().getDouble("Lat"))));
            parametersMap.put("longitude",helper.createPartFrom(String.valueOf(getArguments().getDouble("Lng"))));
        }
        catch (Exception e){
            //o fragmento não veio pela "map activity"
            parametersMap.put("latitude",helper.createPartFrom(String.valueOf(latitude)));
            parametersMap.put("longitude",helper.createPartFrom(String.valueOf(longitude)));
        }

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Log.d("Token",token);
        //List<byte[]> fotos = new ArrayList<>();
        Log.d("Logger", "parametersMap " + parametersMap.toString());
        Log.d("Logger", "name " + eventName_value + " description " + eventDesc_value +
                " endData " + eventDate_formated  + " kind " + eventKind_value  + " price " + eventPrice_value);

        final AlertDialog dialog = createLoadingDialog();
        dialog.show();

        if(foto != null){
            Call<AddEventoResponse> call = ApiClient.API_SERVICE.cadastrarEvento("bearer " + token, parametersMap,helper.loadPhoto("photo",foto));
            call.enqueue(new Callback<AddEventoResponse>() {
                @Override
                public void onResponse(Call<AddEventoResponse> call, Response<AddEventoResponse> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        Log.d("Logger", "Cadastro de evento OK");
                        Toast.makeText(getActivity(), R.string.ev_cadastro_sucesso, Toast.LENGTH_SHORT).show();
                        backToHome();
                    } else {
                        try {
                            ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                            Log.e("Logger", "Server error: "+error.getErrorType()+", "+error.getErrorDescription());
                            Toast.makeText(getContext(),error.getErrorDescription(),Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddEventoResponse> call, Throwable t) {
                    dialog.dismiss();
                    // Log error here since request failed
                    Toast.makeText(getContext(), "Erro ao se conectar com o servidor!", Toast.LENGTH_SHORT).show();
                    Log.e("App Server Error", t.toString());
                }
            });
        }
        else {
            Call<AddEventoResponse> call = ApiClient.API_SERVICE.cadastrarEvento("bearer " + token, parametersMap);
            call.enqueue(new Callback<AddEventoResponse>() {
                @Override
                public void onResponse(Call<AddEventoResponse> call, Response<AddEventoResponse> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        Log.d("Logger", "Cadastro de evento OK");
                        Toast.makeText(getActivity(), R.string.ev_cadastro_sucesso, Toast.LENGTH_SHORT).show();
                        backToHome();
                    } else {
                        try {
                            ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                            Log.e("Logger", "Server error: "+error.getErrorType()+", "+error.getErrorDescription());
                            Toast.makeText(getContext(),error.getErrorDescription(),Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddEventoResponse> call, Throwable t) {
                    dialog.dismiss();
                    // Log error here since request failed
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("App Server Error", t.toString());
                }
            });
        }
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

    private AlertDialog createLoadingDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_aguarde,null);
        dialog.setView(dialogView);
        dialog.setTitle("Enviando");
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Logger", "EventoCadastroFragment onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_SELECT && resultCode == RESULT_OK) {
            Log.d("Logger", "PontoTuristicoCadastroFragment onActivityResult CAM_SELECT " + CAM_SELECT);
            foto = data.getData();
            Log.d("Logger","Seleciona imagem"+foto.getPath());
        }

        if(requestCode == CAM_REQUEST) {
            Log.d("Logger", "EventoCadastroFragment onActivityResult CAM_REQUEST " + CAM_REQUEST);
//            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        }
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