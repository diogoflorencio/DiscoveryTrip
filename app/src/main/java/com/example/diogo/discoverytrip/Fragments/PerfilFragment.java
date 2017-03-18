package com.example.diogo.discoverytrip.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.diogo.discoverytrip.DataBase.UserData;
import com.example.diogo.discoverytrip.R;

import java.util.Map;

/**
 * Classe fragment responsavel pelo fragmento perfil na aplicação
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {
    public TextView userName, userEmail;

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

        Map<String,String> map = UserData.recuperar(getContext().getSharedPreferences("userData", Context.MODE_PRIVATE));
        userName.setText(map.get("name"));
        userEmail.setText(map.get("email"));
        PerfilEditFragment.userEmail_value = map.get("email");
        PerfilEditFragment.userName_value = map.get("name");
        return rootView;
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
        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }
}