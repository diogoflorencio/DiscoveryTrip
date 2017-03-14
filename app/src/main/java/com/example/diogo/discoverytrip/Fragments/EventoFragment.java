package com.example.diogo.discoverytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.Util.ListAdapterEventos;
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

import java.util.List;

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

        getActivity().setTitle(R.string.evento_label);

        rootView.findViewById(R.id.createEvent_btn).setOnClickListener(this);
        rootView.findViewById(R.id.pesquisa_evento_btnOK).setOnClickListener(this);
        rootView.findViewById(R.id.pesquisa_evento_btnLocalizacao).setOnClickListener(this);

        ListView viewEventos = (ListView) rootView.findViewById(R.id.fragment_evento_listEvento);

        DiscoveryTripBD bd = new DiscoveryTripBD(getContext());
        List<Atracao> atracoes = bd.selectAllLembretesTable();

       /* ListAdapterEventos adapter = new ListAdapterEventos(getActivity(),
                getActivity().getLayoutInflater(),
                atracoes);
        viewEventos.setAdapter(adapter);
        *
        * erro ao instanciar o List Adapter
        */


        //TODO fazer a chamada ao servidor para pegar os eventos e setar o adapter da list view
        //ListAdapterEventos adapter = new ListAdapterEventos(inflater, 'lista de eventos');
        //viewEventos.setAdapter(adapter);
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
            case R.id.pesquisa_evento_btnOK:
                break;
            case R.id.pesquisa_evento_btnLocalizacao:
                break;
        }
    }

    private void goToEventCreation() {
        Log.d("Logger", "EventoFragment goToEventCreation");
        FragmentManager fragmentManager = getFragmentManager();
        EventoCadastroFragment fragment = new EventoCadastroFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }
}