package com.example.diogo.discoverytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diogo.discoverytrip.R;

/**
 * Classe fragment responsavel pelo fragmento evento na aplicação
 */
public class EventoFragment extends Fragment implements View.OnClickListener {

    public EventoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Logger", "EventoFragment onCreate");
        View rootView = inflater.inflate(R.layout.fragment_evento, container, false);

        rootView.findViewById(R.id.createEvent_btn).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        Log.d("Logger", "EventoFragment onClick");
        switch (view.getId()) {
            case R.id.createEvent_btn:
                Log.d("Logger", "EventoFragment botao confirmar");
                goToEventCreation();
                break;
        }
    }

    private void goToEventCreation() {
        Log.d("Logger", "EventoFragment backToHome");
        FragmentManager fragmentManager = getFragmentManager();
        EventoCadastroFragment fragment = new EventoCadastroFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }
}
