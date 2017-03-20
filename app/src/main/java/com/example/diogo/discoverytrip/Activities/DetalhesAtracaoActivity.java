package com.example.diogo.discoverytrip.Activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.ViewGroup;

import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.VisualizationType;
import com.example.diogo.discoverytrip.R;

public class DetalhesAtracaoActivity extends Activity {
    public static Atracao atracao;
    public static VisualizationType visualizationType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_atracao);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        atracao = null;
    }

}
