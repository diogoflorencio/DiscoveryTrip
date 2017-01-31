package com.example.diogo.discoverytrip;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Classe fragment responsavel pelo fragmento localização na aplicação
 */
public class LocalizacaoFragment extends Fragment {


    public LocalizacaoFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'LocalizacaoFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PontoTuristicoFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_localizacao, container, false);

        return rootView;
    }

}
