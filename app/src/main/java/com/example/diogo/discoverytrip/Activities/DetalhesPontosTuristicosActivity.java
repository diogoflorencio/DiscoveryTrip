package com.example.diogo.discoverytrip.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.VisualizationType;
import com.example.diogo.discoverytrip.R;

public class DetalhesPontosTuristicosActivity extends AppCompatActivity {
    public static Atracao pontoTuristico;
    public static VisualizationType visualizationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pontos_turisticos);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        pontoTuristico = null;
    }
}
