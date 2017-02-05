package com.example.diogo.discoverytrip.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;

/**
 * Classe fragment responsavel pelo fragmento ponto turistico na aplicação
 */
public class PontoTuristicoFragment extends Fragment implements View.OnClickListener {
    public EditText nameVal_txt, catgVal_txt, descVal_txt;

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
                    validateFields();
                    postData();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void postData(){
        //logica aqui
        //precisa fazer um post e mandar o cadastro pro servidor
        //TODO
    }

    private void backToHome() {
        Log.d("Logger", "PontoTuristicoFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

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