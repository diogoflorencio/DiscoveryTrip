package com.example.diogo.discoverytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.diogo.discoverytrip.R;

/**
 * Classe fragment responsavel pelo fragmento perfil na aplicação
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {
    public static TextView userName, userEmail;
    private String name, email, id;

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

        getActivity().setTitle(R.string.perfil_label);

        Button confirmarBtn = (Button) rootView.findViewById(R.id.pfConfirm_btn);
        confirmarBtn.setOnClickListener(this);
        userName = (TextView) rootView.findViewById(R.id.userName);
        userEmail = (TextView) rootView.findViewById(R.id.userEmail);

        receiveDataFromHome();

        return rootView;
    }

    public void receiveDataFromHome(){
        Log.d("Logger", "PerfilFragment receiveDataFromHome");

        if (getArguments() != null) {
            name = getArguments().getString("name");
            email = getArguments().getString("email");
            id = getArguments().getString("id");
        }

        userName.setText(name);
        userEmail.setText(email);
    }

    public void sendDatatoEdit(String name, String email, String id, Fragment fragment){
        Log.d("Logger", "PerfilFragment sendDatatoEdit");

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "PerfilFragment onClick");
        switch (view.getId()) {
            case R.id.pfConfirm_btn:
                Log.d("Logger", "PerfilFragment botao confirmar");
                goToPerfilCreation();
                break;
        }
    }

    private void goToPerfilCreation() {
        Log.d("Logger", "PerfilEditFragment goToPerfilCreation");
        FragmentManager fragmentManager = getFragmentManager();
        PerfilEditFragment fragment = new PerfilEditFragment();

        sendDatatoEdit(name, email, id, fragment);

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    public static void refreshPerfil(String name, String email){
        Log.d("Logger", "PerfilEditFragment refreshPerfil");
        userName.setText(name);
        userEmail.setText(email);
    }
}