package com.example.diogo.discoverytrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Classe activity responsavel pela activity de recuperacao de senha na aplicação
 */
public class RecuperarSenhaActivity extends AppCompatActivity {

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'RecuperarSenhaActivity'
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "RecuperarSenhaActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
    }
}
