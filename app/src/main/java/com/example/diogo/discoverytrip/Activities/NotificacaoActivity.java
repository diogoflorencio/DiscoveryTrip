package com.example.diogo.discoverytrip.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.Fragments.DetalhesEventoFragment;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.VisualizationType;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

public class NotificacaoActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);
        listView = (ListView) findViewById(R.id.notificacao_listview);
        DiscoveryTripBD bd =  new DiscoveryTripBD(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Atracao atracao = (Atracao) parent.getAdapter().getItem(position);
                DetalhesEventoFragment.atracao = atracao;
                DetalhesEventoFragment.visualizationType = VisualizationType.Visualizar;
                changeFragment(new DetalhesEventoFragment());
            }
        });
        ListAdapterPontosTuristicos adapter = new ListAdapterPontosTuristicos(this, getLayoutInflater(),
                bd.selectDayLembretesTable());
        listView.setAdapter(adapter);
    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            fragment.setArguments(extras);
        }
        fragmentManager.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        fragmentManager.replace(R.id.content_home, fragment);
        fragmentManager.addToBackStack(null);
        fragmentManager.commit();
    }
}