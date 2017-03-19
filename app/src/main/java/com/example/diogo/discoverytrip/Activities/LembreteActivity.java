package com.example.diogo.discoverytrip.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;

import java.util.ArrayList;
import java.util.List;


public class LembreteActivity extends AppCompatActivity {

    private List<Atracao> atracoes = new ArrayList<Atracao>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete);
        DiscoveryTripBD discoveryTripBD = new DiscoveryTripBD(getApplicationContext());
        atracoes = discoveryTripBD.selectDayLembretesTable();
        /*não precisa testar se atracoes é empty*/
    }
}
