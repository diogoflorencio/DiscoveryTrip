package com.example.diogo.discoverytrip.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.VisualizationType;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.DeleteAttractionResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.diogo.discoverytrip.Activities.HomeActivity.EVENT_TYPE;

/**
 * Classe fragment responsavel pelo fragmento evento na aplicação
 */
public class EventoFragment extends Fragment implements View.OnClickListener {
    private static final String TAB_MEUS_EVENTOS = "tab1", TAB_LEMBRETES = "tab2";
    private TabHost tabHost;
    private List<Atracao> lembretes;
    private List<Atracao> meusEventos;
    private ListView listViewMeusEventos;
    private ListView listViewLembretes;

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

        tabHost = (TabHost) rootView.findViewById(R.id.tabhost_eventos);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec(TAB_LEMBRETES);
        tab1.setContent(R.id.tab_lembretes);
        tab1.setIndicator("Lembretes");
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec(TAB_MEUS_EVENTOS);
        tab2.setContent(R.id.tab_meus_eventos);
        tab2.setIndicator("Meus Eventos");
        tabHost.addTab(tab2);

        listViewMeusEventos = (ListView) rootView.findViewById(R.id.meus_eventos_list);
        listViewLembretes = (ListView) rootView.findViewById(R.id.lembretes_eventos_list);

        listViewMeusEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Atracao atracao = (Atracao) parent.getAdapter().getItem(position);

                DetalhesEventoFragment.atracao = atracao;
                DetalhesEventoFragment.visualizationType = VisualizationType.Editar;

                HomeActivity activity = (HomeActivity) getActivity();
                activity.changeFragment(new DetalhesEventoFragment());
            }
        });

        listViewLembretes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Atracao atracao = (Atracao) parent.getAdapter().getItem(position);

                DetalhesEventoFragment.atracao = atracao;
                DetalhesEventoFragment.visualizationType = VisualizationType.Visualizar;

                HomeActivity activity = (HomeActivity) getActivity();
                activity.changeFragment(new DetalhesEventoFragment());
            }
        });

        getUserPoints();
        DiscoveryTripBD discoveryTripBD = new DiscoveryTripBD(getContext());
        lembretes = discoveryTripBD.selectAllLembretesTable();

        ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                getActivity().getLayoutInflater(),
                lembretes);
        listViewLembretes.setAdapter(adapter);

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

    private void setMeusEventosAdapter(){
        for (int i = meusEventos.size() - 1; i >= 0; i--){
            if(!meusEventos.get(i).getType().equals(EVENT_TYPE)){
                meusEventos.remove(i);
            }
        }

        ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(getActivity(),
                getActivity().getLayoutInflater(),
                meusEventos);
        listViewMeusEventos.setAdapter(adapter);
    }

    private void goToEventCreation() {
        Log.d("Logger", "EventoFragment goToEventCreation");
        FragmentManager fragmentManager = getFragmentManager();
        EventoCadastroFragment fragment = new EventoCadastroFragment();

        fragmentManager.beginTransaction().replace(R.id.content_home, fragment
        ).commit();
    }

    private void getUserPoints(){
        String token = AcessToken.recuperar(getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<SearchResponse> call = ApiClient.API_SERVICE.userPoints("bearer "+token);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","UserPoints ok");
                    meusEventos = response.body().getAtracoes();
                    setMeusEventosAdapter();
                }else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "UserPoints ServerResponse "+error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e("Logger","UserPoints error: "+t.toString());
            }
        });
    }

    private void deletePoints(String id){
        String token = AcessToken.recuperar(getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<DeleteAttractionResponse> call = ApiClient.API_SERVICE.deleteAttraction("bearer "+token,id);
        call.enqueue(new Callback<DeleteAttractionResponse>() {
            @Override
            public void onResponse(Call<DeleteAttractionResponse> call, Response<DeleteAttractionResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("Logger", "deletePoints ok");
                }else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "deletePoints ServerResponse "+error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteAttractionResponse> call, Throwable t) {
                Log.e("Logger","deletePoints error: "+t.toString());
            }
        });
    }
}