package com.example.diogo.discoverytrip.Fragments;

import android.os.Bundle;
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
import android.widget.Toast;

import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;

public class EventoCadastroFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public EditText nameVal_txt, descVal_txt, dateVal_txt, priceVal_txt;
    Spinner evKind_spn;

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
        }
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
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.d("Logger", "EventoCadastroFragment onItemSelected");
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
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

//        Map<String, RequestBody> parametersMap = new HashMap<>();
//        MultiRequestHelper helper = new MultiRequestHelper(getContext());
//
//        parametersMap.put("name",helper.createPartFrom(eventName_value));
//        parametersMap.put("description",helper.createPartFrom(eventDesc_value));
//        parametersMap.put("endData ",helper.createPartFrom(eventDate_value));
//        parametersMap.put("kind",helper.createPartFrom(eventKind_value));
//        parametersMap.put("price",helper.createPartFrom(eventPrice_value));
//
//        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
//        Log.d("Token",token);
//        //List<byte[]> fotos = new ArrayList<>();
//
//        Call<EventosResponse> call = ApiClient.API_SERVICE.cadastrarEvento("bearer "+token,parametersMap,helper.loadPhoto("photos",foto));
//        call.enqueue(new Callback<EventosResponse>() {
//            @Override
//            public void onResponse(Call<EventosResponse> call, Response<EventosResponse> response) {
//                if(response.isSuccessful()) {
//                    Log.d("Server Evento","Cadastro OK");
//                }
//                else{
//                    try {
//                        Log.e("Server error",response.errorBody().string());
//                        //ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
//                        //Log.e("Server", error.getErrorDescription());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EventosResponse> call, Throwable t) {
//                // Log error here since request failed
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("App Server Error", t.toString());
//            }
//        });
    }
}