package com.example.diogo.discoverytrip;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Classe fragment responsavel pelo fragmento inicial (home) na aplicação
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'HomeFragment'
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "HomeFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }

}
