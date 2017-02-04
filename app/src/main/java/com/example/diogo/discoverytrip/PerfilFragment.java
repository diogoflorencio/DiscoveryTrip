package com.example.diogo.discoverytrip;

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

/**
 * Classe fragment responsavel pelo fragmento perfil na aplicação
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {
    public EditText nameVal_txt, emailVal_txt;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'PerfilFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PerfilFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        Button confirmarBtn = (Button) rootView.findViewById(R.id.pfConfirm_btn);
        confirmarBtn.setOnClickListener(this);

        getUserData();

        nameVal_txt = (EditText) rootView.findViewById(R.id.pfNameVal_txt);
        emailVal_txt = (EditText) rootView.findViewById(R.id.pfEmailVal_txt);

        return rootView;
    }

    public void getUserData(){
        //funcao pra pegar os dados do perfil do usuário e colocar nos campos
        //TODO
    }

    public void updateUserData(){
        //precisa fazer um post e mandar os dados atualizados pro servidor
        //funcao pra pegar os dados do perfil do usuário e colocar nos campos
        //TODO
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "PerfilFragment onClick");
        switch (view.getId()) {
            case R.id.pfConfirm_btn:
                Log.d("Logger", "PerfilFragment botao confirmar");
                try {
                    validateFields();
                    updateUserData();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void backToHome() {
        Log.d("Logger", "PerfilFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "PerfilFragment validateFields");
        if(nameVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(emailVal_txt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_email));
        }
    }
}
