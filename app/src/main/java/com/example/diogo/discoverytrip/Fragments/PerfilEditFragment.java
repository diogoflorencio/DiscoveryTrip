package com.example.diogo.discoverytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.R;

/**
 * Classe fragment responsavel pelo fragmento de edição de perfil na aplicação
 */
public class PerfilEditFragment extends Fragment implements View.OnClickListener {
    public EditText userName_edt, userEmail_edt;

    public PerfilEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PerfilEditFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_perfil_edit, container, false);

        rootView.findViewById(R.id.pfeConfirm_btn).setOnClickListener(this);
        rootView.findViewById(R.id.pfeCancel_btn).setOnClickListener(this);

        userName_edt = (EditText) rootView.findViewById(R.id.pfeName_edt);
        userEmail_edt = (EditText) rootView.findViewById(R.id.pfeEmail_edt);

        return rootView;
    }

    private void updateUserData(){
        //precisa fazer um post e mandar os dados atualizados pro servidor
        //funcao pra pegar os dados do perfil do usuário e colocar nos campos
        //TODO
        String userName_value = userName_edt.getText().toString();
        String userEmail_value = userEmail_edt.getText().toString();
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "PerfilEditFragment onClick");
        switch (view.getId()) {
            case R.id.pfeConfirm_btn:
                Log.d("Logger", "PerfilEditFragment botao confirmar");
                try {
                    validateFields();
                    updateUserData();
                    backToHome();
                } catch (DataInputException exception){
                    Toast.makeText(this.getActivity(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.pfeCancel_btn:
                Log.d("Logger", "PerfilEditFragment botao cancelar");
                backToHome();
        }
    }

    private void backToHome() {
        Log.d("Logger", "PerfilEditFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        HomeFragment fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void validateFields() throws DataInputException {
        Log.d("Logger", "PerfilEditFragment validateFields");
        if(userName_edt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_name));
        }

        if(userEmail_edt.getText().toString().trim().isEmpty()){
            throw new DataInputException(getString(R.string.validate_email));
        }
    }
}
