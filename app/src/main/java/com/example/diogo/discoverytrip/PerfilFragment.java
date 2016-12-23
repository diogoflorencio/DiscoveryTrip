package com.example.diogo.discoverytrip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Exceptions.DataInputException;

public class PerfilFragment extends Fragment implements View.OnClickListener {
    public EditText nameVal_txt, emailVal_txt;
    private Button confirmarBtn;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PerfilFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        confirmarBtn = (Button) rootView.findViewById(R.id.pfConfirm_btn);
        confirmarBtn.setOnClickListener(this);

        nameVal_txt = (EditText) rootView.findViewById(R.id.pfNameVal_txt);
        emailVal_txt = (EditText) rootView.findViewById(R.id.pfEmailVal_txt);

        return rootView;
    }

    public void onClick(View view) {
        Log.d("Logger", "PontoTuristicoFragment onClick");
        switch (view.getId()) {
            case R.id.pfConfirm_btn:
                Log.d("Logger", "PontoTuristicoFragment botao confirmar");
                try {
                    validateFields();
                    backToHome();
                    //logica aqui
                    //precisa fazer um post e mandar os dados atualizados pro servidor
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void backToHome() {
        Log.d("Logger", "PontoTuristicoFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "PontoTuristicoFragment validateFields");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException("Informe um nome");
        }

        if(emailVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException("Informe um email");
        }
    }
}
