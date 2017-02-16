package com.example.diogo.discoverytrip.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.diogo.discoverytrip.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PontoTuristicoFragment extends Fragment implements View.OnClickListener {


    public PontoTuristicoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "PontoTuristicoFragment onCreate");

        View rootView = inflater.inflate(R.layout.fragment_ponto_turistico, container, false);

        rootView.findViewById(R.id.createPntTuristico_btn).setOnClickListener(this);

//        ListView viewPntsTuristicos = (ListView) rootView.findViewById(R.id.fragment_pntTuristico_listPntTuristico);
        //TODO fazer a chamada ao servidor para pegar os pontos turisticos e setar o adapter da list view
        //ListAdapterEventos adapter = new ListAdapterEventos(inflater, 'lista de pontos turisticos');
        //viewEventos.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Log.d("Logger", "PontoTuristicoFragment onClick");
        switch (v.getId()) {
            case R.id.createPntTuristico_btn:
                Log.d("Logger", "EventoFragment botao confirmar");
                goToPntTuristicoCreation();
                break;
        }
    }

    private void goToPntTuristicoCreation() {
        Log.d("Logger", "PontoTuristicoFragment goToPntTuristicoCreation");
        FragmentManager fragmentManager = getFragmentManager();
        PontoTuristicoCadastroFragment fragment = new PontoTuristicoCadastroFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }
}
