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

import com.example.diogo.discoverytrip.Activities.MapsActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.MultiRequestHelper;
import com.example.diogo.discoverytrip.REST.ServerResponses.AddEventoResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.Util.DatePickerFragment;
import com.example.diogo.discoverytrip.Util.TimePickerFragment;

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

public class EventoCadastroFragment extends Fragment implements LocationListener, View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerFragment.DatePickerFragmentListener, TimePickerFragment.TimePickerFragmentListener {
    public EditText nameVal_txt, descVal_txt, priceVal_txt;
    private Date dateVal_date;
    public TextView dateInicioVal_txt, dateFimVal_txt, timeInicioVal_txt, timeFimVal_txt;
    private Integer horaInicio, horaFim, minutoInicio, minutoFim;
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
    static final int PNTTURISTICOCAD = 0001;

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

        getActivity().setTitle(R.string.evento_label);

        rootView.findViewById(R.id.evConfirm_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evCancel_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evCamera_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evento_btnFoto).setOnClickListener(this);
        rootView.findViewById(R.id.evDatePickerInicio_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evDatePickerFim_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evMap_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evTimePickerInicio_btn).setOnClickListener(this);
        rootView.findViewById(R.id.evTimePickerFim_btn).setOnClickListener(this);

        nameVal_txt = (EditText) rootView.findViewById(R.id.evName_edt);
        descVal_txt = (EditText) rootView.findViewById(R.id.evDesc_edt);
        priceVal_txt = (EditText) rootView.findViewById(R.id.evPrice_edt);

        dateInicioVal_txt = (TextView) rootView.findViewById(R.id.dateValInicio_txt);
        dateFimVal_txt = (TextView) rootView.findViewById(R.id.dateValFim_txt);
        timeInicioVal_txt = (TextView) rootView.findViewById(R.id.evTimeInicioVal_txt);
        timeFimVal_txt = (TextView) rootView.findViewById(R.id.evTimeFimVal_txt);

        evKind_spn = (Spinner) rootView.findViewById(R.id.evKind_spn);
        evKind_spn.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.event_kind, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evKind_spn.setAdapter(adapter);

        return rootView;
    }

    private void timPicker(String timeKind) {
        Log.d("Logger", "EventoCadastroFragment timPicker");
        TimePickerFragment fragment = TimePickerFragment.newInstance(this);
        getActivity().getIntent().putExtra("TimeKind", timeKind);
        fragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(int hour, int minute) {
        Log.d("Logger", "EventoCadastroFragment onTimeSet");
        setTime(hour, minute);
    }

    public void setTime(int hour, int minute) {
        Log.d("Logger", "EventoCadastroFragment setTime");

        try {
            String dateKind = getActivity().getIntent().getStringExtra("TimeKind");
            switch (dateKind){
                case "Inicio":
                    horaInicio = hour;
                    minutoInicio = minute;
                    timeInicioVal_txt.setText(horaInicio+":"+minutoInicio);
                    Log.d("Logger", "horaInicio " + timeInicioVal_txt.getText());
                    break;
                case "Fim":
                    horaFim = hour;
                    minutoFim = minute;
                    timeFimVal_txt.setText(horaFim+":"+minutoFim);
                    Log.d("Logger", "horaFim " + timeFimVal_txt.getText());
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void datePicker(View view, String dateKind){
        Log.d("Logger", "EventoCadastroFragment DatePicker");
        DatePickerFragment fragment = DatePickerFragment.newInstance(this);
        getActivity().getIntent().putExtra("DateKind", dateKind);
        fragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void setDate(int year, int month, int dayOfMonth) {
        Log.d("Logger", "EventoCadastroFragment setDate");
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        month = month +1;

        try {
            String dateKind = getActivity().getIntent().getStringExtra("DateKind");
            switch (dateKind){
                case "Inicio":
                    dateInicioVal_txt.setText(dayOfMonth+"/"+month+"/"+year);
                    break;
                case "Fim":
                    dateFimVal_txt.setText(dayOfMonth+"/"+month+"/"+year);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.d("Logger", "data " + dateInicioVal_txt.getText());
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
                break;
            case R.id.evento_btnFoto:
                Log.d("Logger","EventoCadastroFragment selecionar foto");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), CAM_SELECT);
                break;
            case R.id.evDatePickerInicio_btn:
                Log.d("Logger", "EventoCadastroFragment botao datePickerInicio");
                datePicker(view, "Inicio");
                break;
            case R.id.evDatePickerFim_btn:
                Log.d("Logger", "EventoCadastroFragment botao datePickerFim");
                datePicker(view, "Fim");
                break;
            case R.id.evMap_btn:
                Log.d("Logger", "EventoCadastroFragment botao mapa");
                openMapActivity();
                break;
            case R.id.evTimePickerInicio_btn:
                Log.d("Logger", "EventoCadastroFragment botao timePickerInicio");
                timPicker("Inicio");
                break;
            case R.id.evTimePickerFim_btn:
                Log.d("Logger", "EventoCadastroFragment botao timePickerFim");
                timPicker("Fim");
                break;
        }
    }

    private void openMapActivity(){
        Log.d("Logger", "PontoTuristicoCadastroFragment openMapActivity");
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        startActivityForResult(mapIntent, PNTTURISTICOCAD);
    }

    private void startCameraActivity() {
        Log.d("Logger", "EventoCadastroFragment startCameraActivity");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException e) {
//                // Error occurred while creating the File
//                e.printStackTrace();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(getContext(),
//                        "com.example.android.fileprovider",
//                        photoFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));
                startActivityForResult(intent, CAM_REQUEST);
//            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Logger","EventoCadastroFragment onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_SELECT && resultCode == RESULT_OK) {
            Log.d("Logger", "EventoCadastroFragment onActivityResult CAM_SELECT " + CAM_SELECT);
            foto = data.getData();
            Log.d("Logger","Seleciona imagem" + foto.getPath());
        }

        if(requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
            Log.d("Logger", "EventoCadastroFragment onActivityResult CAM_REQUEST " + CAM_REQUEST);
            try{
                foto = data.getData();
            } catch (Exception e){
                e.printStackTrace();
                File file = new File("/sdcard/tmp");
                try {
                    foto = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), null, null));
                    Log.d("Logger","Seleciona imagem" + foto.getPath());
                    if (!file.delete()) {
                        Log.d("logMarker", "Failed to delete " + file);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
//            galleryAddPic();
        }

        if(requestCode == PNTTURISTICOCAD  && resultCode == RESULT_OK) {
            Log.d("Logger", "EventoCadastroFragment onActivityResult PNTTURISTICOCAD " + PNTTURISTICOCAD);

            try{
                Log.d("Logger", "EventoCadastroFragment onActivityResult PNTTURISTICOCAD1 " + data.getStringExtra("Lat"));
                getActivity().getIntent().putExtra("Lat", data.getStringExtra("Lat"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                Log.d("Logger", "EventoCadastroFragment onActivityResult PNTTURISTICOCAD2 " + data.getStringExtra("Lng"));
                getActivity().getIntent().putExtra("Lng", data.getStringExtra("Lng"));
            } catch (Exception e) {
                e.printStackTrace();
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
        try{
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        } catch (Exception e){
            //path não existe
            e.printStackTrace();
        }
    }

    private void backToHome() {
        Log.d("Logger", "EventoCadastroFragment backToHome");
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
        Log.d("Logger", "EventoCadastroFragment validateFields");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException("Digite o nome do evento!");
        }

        if(dateFimVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_date_end));
        }

        try {
            Date dateBegin = normalDateFormat.parse(dateInicioVal_txt.getText().toString());
            Date dateEnd = normalDateFormat.parse(dateFimVal_txt.getText().toString());

            if(dateBegin.compareTo(dateEnd) > 0){
                throw new DataInputException(getString(R.string.validate_date_dontMatch));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(timeInicioVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_time));
        }

        if(descVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_description));
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
        String eventDateBegin_value = dateInicioVal_txt.getText().toString();
        String eventDateEnd_value = dateFimVal_txt.getText().toString();

        Log.d("Logger","DateBegin txt "+ eventDateBegin_value);
        Log.d("Logger","DateEnd txt "+ eventDateEnd_value);

        String eventPrice_value = null;

        try{
            eventPrice_value = priceVal_txt.getText().toString().replace('.', ',');

        } catch (Exception e){
            eventPrice_value = "0";
            e.printStackTrace();
        }

        String eventKind_value = null;
        switch (evKind_spn.getSelectedItemPosition()) {
            case 0: eventKind_value = "private";
                break;
            case 1: eventKind_value = "public";
                break;
        }

        String eventDateBegin_formated = null;
        String eventDateEnd_formated = null;

        if (dateInicioVal_txt.getText().toString().trim().isEmpty()){
            Date dateBegin = new Date();
            dateBegin.toString();
            dateBegin.setHours(0);
            dateBegin.setMinutes(59);
            dateBegin.setSeconds(59);
            eventDateBegin_formated = dateFormat.format(dateBegin);
        }else{
            try {
                Date dateBegin = normalDateFormat.parse(eventDateBegin_value);
                dateBegin.toString();
                dateBegin.setHours(horaInicio);
                dateBegin.setMinutes(minutoInicio);
                dateBegin.setSeconds(0);
                eventDateBegin_formated = dateFormat.format(dateBegin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.d("Logger","DateBegin formated "+ eventDateBegin_formated);


        try {
            Date dateEnd = normalDateFormat.parse(eventDateEnd_value);
            dateEnd.toString();
            dateEnd.setHours(horaFim);
            dateEnd.setMinutes(minutoFim);
            dateEnd.setSeconds(1);
            eventDateEnd_formated = dateFormat.format(dateEnd);
            Log.d("Logger","DateEnd formated "+ eventDateEnd_formated);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, RequestBody> parametersMap = new HashMap<>();
        MultiRequestHelper helper = new MultiRequestHelper(getContext());

        parametersMap.put("name",helper.createPartFrom(eventName_value));
        parametersMap.put("description",helper.createPartFrom(eventDesc_value));

        parametersMap.put("startDate",helper.createPartFrom(eventDateBegin_formated));
        parametersMap.put("endDate",helper.createPartFrom(eventDateEnd_formated));

        parametersMap.put("kind",helper.createPartFrom(eventKind_value));
        parametersMap.put("price",helper.createPartFrom(eventPrice_value));
        Log.d("Logger","DateEnd price "+ eventPrice_value);

        try{
            //o fragmento veio pela "map activity"
            parametersMap.put("latitude",helper.createPartFrom(getArguments().getString("Lat")));
            parametersMap.put("longitude",helper.createPartFrom(getArguments().getString("Lng")));
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
                " startDate " + eventDateBegin_formated  + " endDate " + eventDateEnd_formated  +
                " kind " + eventKind_value  + " price " + eventPrice_value);

        final AlertDialog dialog = createLoadingDialog();
        dialog.show();

        if(foto != null){
            Log.d("Logger","Tem foto");
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

    private AlertDialog createLoadingDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_aguarde,null);
        dialog.setView(dialogView);
        dialog.setTitle("Enviando");
        dialog.setCancelable(false);
        return dialog;
    }

    private void startGPS() {
        Log.d("Logger", "EventoCadastroFragment startGPS");
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
        Log.d("Logger","EventoCadastroFragment stopGPS");
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